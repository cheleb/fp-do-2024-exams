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

  // Récupération des Routes statiques déjà définies dans le code
  private val webJarRoutes = staticResourcesGetServerEndpoint[Task]("public")(
    this.getClass.getClassLoader,
    "public"
  )

  // Les options du serveur HTTP qui contient des Intercepteurs, les intercepteurs c'est des middlewares, exécutés en amont
  val serverOptions: ZioHttpServerOptions[Any] =
    ZioHttpServerOptions.customiseInterceptors
      .appendInterceptor(
        CORSInterceptor.default
      )
      .options

  // Le système de Migrations pour la DB, j'en sais pas plus
  private val runMigrations = for {
    flyway <- ZIO.service[FlywayService]
    _ <- flyway
      .runMigrations()
      .catchSome { case e =>
        ZIO.logError(s"Error running migrations: ${e.getMessage()}")
          *> flyway.runRepair() *> flyway.runMigrations()
      }
  } yield ()

  private val serrverProgram =
    for {
      _ <- ZIO.succeed(println("Hello world"))
      endpoints <- HttpApi.endpointsZIO
      // Mise en place du Swagger avec les endpoints existants
      docEndpoints = SwaggerInterpreter()
        .fromServerEndpoints(endpoints, "zio-laminar-demo", "1.0.0")
        // Démarrage du serveur HTTP avec les bonnes propriétés
      _ <- Server.serve(
        ZioHttpInterpreter(serverOptions)
          .toHttp(webJarRoutes :: endpoints ::: docEndpoints)
      )
    } yield ()

  // Contient la séquence de lancement, et dans quel ordre ces fonctions doivent être exécutées
  private val program =
    for {
      _ <- runMigrations
      _ <- serrverProgram
    } yield ()

  // Utilise la séquence de lancement juste avant et injecte les dépendances nécessaires autrement appellées Service Layers
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
