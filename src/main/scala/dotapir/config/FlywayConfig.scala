package dotapir.config

import zio.Config
import zio.config.magnolia.deriveConfig

// This defines the configuration for Flyway (with the database URL and credentials)
final case class FlywayConfig(url: String, user: String, password: String)

object FlywayConfig:
  // This defines the global configuration for Flyway, but I don't really know how it gets
  // injected here (this should be from the application.conf file read by the config layer)
  given Config[FlywayConfig] = deriveConfig[FlywayConfig]
