package dotapir.config

import zio.*
import zio.config.*
import zio.config.typesafe.TypesafeConfigProvider

import com.typesafe.config.ConfigFactory
// Objet contenant des utilitaires pour la gestion de la configuration de l'application.
object Configs:
  // Fonction pour créer une couche ZLayer chargée de la configuration à partir d'un fichier Typesafe Config.
  def makeConfigLayer[C](path: String)(using conf: Config[C], r: Tag[C]) =
    ZLayer(
      TypesafeConfigProvider
        .fromTypesafeConfig(
          ConfigFactory.load().getConfig(path)
        )
        .load[C](conf)
    )
