package dotapir.model

import zio.json.JsonCodec
import sttp.tapir.Schema

// Person model
case class Person(
    name: String,
    age: Int,
    email: String
) derives JsonCodec,
      Schema
