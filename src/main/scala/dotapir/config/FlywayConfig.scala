package dotapir.config

import zio.Config
import zio.config.magnolia.deriveConfig

final case class FlywayConfig(url: String, user: String, password: String)

object FlywayConfig:
  // Config instance for FlywayConfig
  given Config[FlywayConfig] = deriveConfig[FlywayConfig]
