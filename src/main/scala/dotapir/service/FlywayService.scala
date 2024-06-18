package dotapir.services

import zio.*
import org.flywaydb.core.Flyway

import dotapir.config.FlywayConfig
import dotapir.config.Configs

// Trait définissant les opérations du service Flyway
trait FlywayService {
  def runClean(): Task[Unit] // Nettoie la base de données avec Flyway
  def runBaseline(): Task[Unit] // Applique une migration de base avec Flyway
  def runMigrations(): Task[Unit] // Exécute les migrations de la base de données avec Flyway
  def runRepair(): Task[Unit] // Répare la table de métadonnées Flyway si nécessaire
}

// Implémentation concrète de FlywayService
class FlywayServiceLive private (flyway: Flyway) extends FlywayService {
  override def runClean(): Task[Unit] = ZIO.attemptBlocking(flyway.clean())
  override def runBaseline(): Task[Unit] =
    ZIO.attemptBlocking(flyway.baseline())
  override def runMigrations(): Task[Unit] =
    ZIO.attemptBlocking(flyway.migrate())
  override def runRepair(): Task[Unit] = ZIO.attemptBlocking(flyway.repair())
}

object FlywayServiceLive {
  // Crée un ZLayer fournissant FlywayService configuré avec FlywayConfig
  def live: ZLayer[FlywayConfig, Throwable, FlywayService] = ZLayer(
    for {
      config <- ZIO.service[FlywayConfig] // Récupère FlywayConfig depuis l'environnement
      flyway <- ZIO.attempt(
        Flyway
          .configure()
          .dataSource(config.url, config.user, config.password)
          .load() // Configure et charge Flyway avec la configuration fournie
      )
    } yield new FlywayServiceLive(flyway) // Retourne une nouvelle instance de FlywayServiceLive
  )

  // Configure un ZLayer pour FlywayService en utilisant la configuration de "db.dataSource"
  val configuredLayer =
    Configs.makeConfigLayer[FlywayConfig]("db.dataSource") >>> live
}
