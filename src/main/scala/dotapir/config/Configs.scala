package dotapir.config

import zio.*
import zio.config.*
import zio.config.typesafe.TypesafeConfigProvider

import com.typesafe.config.ConfigFactory

/** Je suppose que cette classe permet de charger la configuration de
  * l'application depuis un fichier de configuration.
  * (src/main/resources/application.conf)
  *
  * Je ne comprends par contre pas trop comment "using" fonctionne ici et ce
  * qu'il apporte.
  */
object Configs:
  def makeConfigLayer[C](path: String)(using conf: Config[C], r: Tag[C]) =
    ZLayer(
      TypesafeConfigProvider
        .fromTypesafeConfig(
          ConfigFactory.load().getConfig(path)
        )
        .load[C](conf)
    )
