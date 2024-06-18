package dotapir.config

import zio.Config
import zio.config.magnolia.deriveConfig

final case class FlywayConfig(url: String, user: String, password: String)

object FlywayConfig:
  // Automatically derive db configs from resources folder
  given Config[FlywayConfig] = deriveConfig[FlywayConfig]
