package dotapir.model

import zio.json.JsonCodec
import sttp.tapir.Schema

// Derives permet de générer automatiquement un codec JSON pour la classe Person
case class Person(
    name: String,
    age: Int,
    email: String
) derives JsonCodec,
      Schema
