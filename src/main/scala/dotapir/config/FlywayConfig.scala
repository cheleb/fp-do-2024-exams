package dotapir.config

import zio.Config
import zio.config.magnolia.deriveConfig

// Configuration for Flyway using DB credentials and URL
final case class FlywayConfig(url: String, user: String, password: String)

// Define a configuration for Flyway
object FlywayConfig:
  given Config[FlywayConfig] = deriveConfig[FlywayConfig]
