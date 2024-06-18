package dotapir.services

import zio.*
import org.flywaydb.core.Flyway

import dotapir.config.FlywayConfig
import dotapir.config.Configs

trait FlywayService {
  def runClean(): Task[Unit]
  def runBaseline(): Task[Unit]
  def runMigrations(): Task[Unit]
  def runRepair(): Task[Unit]
}

class FlywayServiceLive private (flyway: Flyway) extends FlywayService {
  override def runClean(): Task[Unit] = ZIO.attemptBlocking(flyway.clean())
  override def runBaseline(): Task[Unit] =
    ZIO.attemptBlocking(flyway.baseline())
  override def runMigrations(): Task[Unit] =
    ZIO.attemptBlocking(flyway.migrate())
  override def runRepair(): Task[Unit] = ZIO.attemptBlocking(flyway.repair())
}

/** Composition verticale de layers ZIO, en prenant la configuration Flyway et
  * en créant un service Flyway. On se retrouve avec un layer ZIO avec les
  * dépendances du layer à gauche du `>>>` et les ressources du layer à droite
  * du `>>>` (ses dépendances ont été consommées).
  *
  * C'est pourquoi le type sortant est ZLayer[Any, Throwable, FlywayService]
  *
  * On remarque qu'il à gardé le Throwable dans le type sortant, dans notre cas,
  * si les erreurs étaient plus précises, il aurait trouvé le type d'erreur
  * commune entre les erreurs possibles.
  */
object FlywayServiceLive {
  def live: ZLayer[FlywayConfig, Throwable, FlywayService] = ZLayer(
    for {
      config <- ZIO.service[FlywayConfig]
      flyway <- ZIO.attempt(
        Flyway
          .configure()
          .dataSource(config.url, config.user, config.password)
          .load()
      )
    } yield new FlywayServiceLive(flyway)
  )

  val configuredLayer =
    Configs.makeConfigLayer[FlywayConfig]("db.dataSource") >>> live
}
