package dotapir.config

import zio.*
import zio.config.*
import zio.config.typesafe.TypesafeConfigProvider

import com.typesafe.config.ConfigFactory
object Configs:
  // This seems to load a configuration file from a path that is typed, and puts it into a layer
  // that is going to be used in the main program
  def makeConfigLayer[C](path: String)(using conf: Config[C], r: Tag[C]) =
    ZLayer(
      TypesafeConfigProvider
        .fromTypesafeConfig(
          ConfigFactory.load().getConfig(path)
        )
        .load[C](conf)
    )
