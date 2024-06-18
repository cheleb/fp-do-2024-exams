package dotapir.model

import zio.json.JsonCodec
import sttp.tapir.Schema

// Nice case class, that defines a person
// It uses JsonCodec to be (de)-serialized into json and Schema for the DB representation
case class Person(
    name: String,
    age: Int,
    email: String
) derives JsonCodec,
      Schema
