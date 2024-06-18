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

// This object defines the ZIO entry point
object HttpServer extends ZIOAppDefault {
  // Those defines the routes for the assets that can be included in the resources
  private val webJarRoutes = staticResourcesGetServerEndpoint[Task]("public")(
    this.getClass.getClassLoader,
    "public"
  )

  // Those are the options for the HTTP server
  val serverOptions: ZioHttpServerOptions[Any] =
    ZioHttpServerOptions.customiseInterceptors
      .appendInterceptor(
        CORSInterceptor.default
      )
      .options

  // This is used to run the migrations using Flyway - this will first run the repairs
  // to ensure that there are no failed migrations, and then run the migrations
  private val runMigrations = for {
    flyway <- ZIO.service[FlywayService]
    _ <- flyway
      .runMigrations()
      .catchSome { case e =>
        ZIO.logError(s"Error running migrations: ${e.getMessage()}")
          *> flyway.runRepair() *> flyway.runMigrations()
      }
  } yield ()

  // This creates the server program that holds the business logic, with the endpoints
  // defined in this application and the generated documentation endpoints
  // In the end, the HTTP server is started with the options defined above
  private val serrverProgram =
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

  // This is the main program that will run the migrations and then start the server
  private val program =
    for {
      _ <- runMigrations
      _ <- serrverProgram
    } yield ()

  // This is the program that is run when the ZIO application starts
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
