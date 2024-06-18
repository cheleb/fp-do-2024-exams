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

  // Endpoint pour servir les ressources web statiques
  private val webJarRoutes = staticResourcesGetServerEndpoint[Task]("public")(
    this.getClass.getClassLoader,
    "public"
  )

  // Options du serveur HTTP ZIO avec intercepteur CORS par défaut, pratique
  val serverOptions: ZioHttpServerOptions[Any] =
    ZioHttpServerOptions.customiseInterceptors
      .appendInterceptor(
        CORSInterceptor.default
      )
      .options

  // Programme pour exécuter les migrations de base de données avec flyway
  private val runMigrations = for {
    flyway <- ZIO.service[FlywayService]
    _ <- flyway
      .runMigrations()
      .catchSome { case e =>
        ZIO.logError(s"Error running migrations: ${e.getMessage()}")
          *> flyway.runRepair() *> flyway.runMigrations() // En cas d'erreur, réparer et réessayer les migrations
      }
  } yield ()

  // Programme principal du serveur HTTP
  private val serverProgram =
    for {
      _ <- ZIO.succeed(println("Hello world")) // Affichage d'un message de démarrage
      endpoints <- HttpApi.endpointsZIO // Récupération des endpoints définis dans HttpApi
      docEndpoints = SwaggerInterpreter()
        .fromServerEndpoints(endpoints, "zio-laminar-demo", "1.0.0") // Génération de la documentation Swagger à partir des endpoints
      _ <- Server.serve(
        ZioHttpInterpreter(serverOptions)
          .toHttp(webJarRoutes :: endpoints ::: docEndpoints)
      )
    } yield ()

  // Programme principal combinant les migrations et le serveur HTTP
  private val program =
    for {
      _ <- runMigrations // Exécution des migrations de base de données
      _ <- serverProgram // Démarrage du serveur HTTP
    } yield ()

  // Fonction d'exec principale
  override def run =
    program
      .provide(
        Server.default,
        FlywayServiceLive.configuredLayer,
        UserRepositoryLive.layer,
        Repository.dataLayer
      )
}
