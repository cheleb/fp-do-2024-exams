package dotapir.config

import zio.*
import zio.config.*
import zio.config.typesafe.TypesafeConfigProvider

import com.typesafe.config.ConfigFactory
object Configs:
  // Create a ZLayer from a configuration located at `path`
  def makeConfigLayer[C](path: String)(using conf: Config[C], r: Tag[C]) =
    ZLayer(
      TypesafeConfigProvider
        .fromTypesafeConfig(
          ConfigFactory.load().getConfig(path)
        )
        .load[C](conf)
    )
