package dotapir.model

import zio.json.JsonCodec
import sttp.tapir.Schema

// Person model which can be derived from `JsonCodec` and `Schema` to be translated to JSON and OpenAPI schema
case class Person(
    name: String,
    age: Int,
    email: String
) derives JsonCodec,
      Schema
