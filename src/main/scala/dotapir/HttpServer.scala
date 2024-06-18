package dotapir

import zio.*
import zio.http.*

import sttp.tapir.*
import sttp.tapir.files.*
import sttp.tapir.server.ziohttp.*
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.server.interceptor.cors.CORSInterceptor

import dotapir.server.HttpApi
import dotapir.services.FlywayService
import dotapir.services.FlywayServiceLive
import dotapir.repository.UserRepositoryLive
import dotapir.repository.Repository

object HttpServer extends ZIOAppDefault {

  private val webJarRoutes = staticResourcesGetServerEndpoint[Task]("public")(
    this.getClass.getClassLoader,
    "public"
  )

  val serverOptions: ZioHttpServerOptions[Any] =
    ZioHttpServerOptions.customiseInterceptors
      .appendInterceptor(
        CORSInterceptor.default
      )
      .options

  private val runMigrations = for {
    flyway <- ZIO.service[FlywayService]
    _ <- flyway
      .runMigrations()
      .catchSome { case e =>
        ZIO.logError(s"Error running migrations: ${e.getMessage()}")
          *> flyway.runRepair() *> flyway.runMigrations()
      }
  } yield ()

  private val serverProgram =
    for {
      _ <- ZIO.succeed(println("Hello world"))
      endpoints <- HttpApi.endpointsZIO
      docEndpoints = SwaggerInterpreter()
        .fromServerEndpoints(endpoints, "zio-laminar-demo", "1.0.0")
      _ <- Server.serve(
        ZioHttpInterpreter(serverOptions)
          .toHttp(webJarRoutes :: endpoints ::: docEndpoints)
      )
    } yield ()

  private val program =
    for {
      _ <- runMigrations
      _ <- serverProgram
    } yield ()

  override def run =
    program
      .provide(
        Server.default,
        // Service layers
        FlywayServiceLive.configuredLayer,
        UserRepositoryLive.layer,
        Repository.dataLayer
      )
}
