package dotapir.services

import zio.*
import org.flywaydb.core.Flyway

import dotapir.config.FlywayConfig
import dotapir.config.Configs

// This is the service that will be used to run the Flyway migrations
trait FlywayService {
  def runClean(): Task[Unit]
  def runBaseline(): Task[Unit]
  def runMigrations(): Task[Unit]
  def runRepair(): Task[Unit]
}

// This is the implementation of the Flyway service that delegates the calls to the Flyway Java library
class FlywayServiceLive private (flyway: Flyway) extends FlywayService {
  override def runClean(): Task[Unit] = ZIO.attemptBlocking(flyway.clean())
  override def runBaseline(): Task[Unit] =
    ZIO.attemptBlocking(flyway.baseline())
  override def runMigrations(): Task[Unit] =
    ZIO.attemptBlocking(flyway.migrate())
  override def runRepair(): Task[Unit] = ZIO.attemptBlocking(flyway.repair())
}

object FlywayServiceLive {
  // This declares the global layer that will be used to provide the Flyway service
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

  // This seems to get the data source configuration from the configuration layer,
  // but I don't really know how it's used afterwards
  val configuredLayer =
    Configs.makeConfigLayer[FlywayConfig]("db.dataSource") >>> live
}
