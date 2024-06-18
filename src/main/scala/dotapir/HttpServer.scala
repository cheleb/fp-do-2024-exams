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

  /**
   * Liste les ressources publiques (images, etc.) comme endpoints HTTP
   */
  private val webJarRoutes = staticResourcesGetServerEndpoint[Task]("public")(
    this.getClass.getClassLoader,
    "public"
  )

  /**
   * Customise les options du serveur ZIO.
   * En l'occurence, ajoute un intercepteur CORS.
   */
  val serverOptions: ZioHttpServerOptions[Any] =
    ZioHttpServerOptions.customiseInterceptors
      .appendInterceptor(
        CORSInterceptor.default
      )
      .options

  /**
   * Lance les migrations en utilisant le {@link FlywayService}
   */
  private val runMigrations = for {
    flyway <- ZIO.service[FlywayService]
    _ <- flyway
      .runMigrations()  // lance les migrations
      .catchSome { case e =>
        // si on rencontre un problème
        ZIO.logError(s"Error running migrations: ${e.getMessage}")
          // on essaye de réparer, puis on relance les migrations
          *> flyway.runRepair() *> flyway.runMigrations()
      }
  } yield ()

  /**
   * Programme pour le serveur HTTP ZIO.
   * Boucle infinie jusqu'à terminaison du programme.
   */
  private val serrverProgram =
    for {
      _ <- ZIO.succeed(println("Hello world"))

      // charge les endpoints HTTP des controllers
      endpoints <- HttpApi.endpointsZIO

      // définit les endpoints de documentation swagger à partir des endpoints
      docEndpoints = SwaggerInterpreter()
        .fromServerEndpoints(endpoints, "zio-laminar-demo", "1.0.0")

      // lance le serveur HTTP ZIO avec toutes les routes collectées au dessus
      _ <- Server.serve(
        ZioHttpInterpreter(serverOptions)
          .toHttp(webJarRoutes :: endpoints ::: docEndpoints)
      )
    } yield ()

  /**
   * Programme principal. Lance les migrations puis le serveur HTTP.
   */
  private val program =
    for {
      _ <- runMigrations
      _ <- serrverProgram
    } yield ()

  /**
   * Le main ZIO avec le programme et les layers nécessaires
   */
  override def run: ZIO[Any, Throwable, Unit] =
    program
      .provide(
        // la serveur platform HTTP ZIO
        Server.default,

        // Service layers

        // Flyway configuré
        FlywayServiceLive.configuredLayer,
        // Le UserRepository pour communiquer en DAO
        UserRepositoryLive.layer,
        // Le Repository pour accéder à la base de données
        Repository.dataLayer
      )
}
