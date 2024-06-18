package dotapir.model

import zio.json.JsonCodec
import sttp.tapir.Schema

// This defines a person with a name, age, and email, that can be encoded and decoded to/from JSON
// and as a Tapir schema to be used for documentation and validation purposes
case class Person(
    name: String,
    age: Int,
    email: String
) derives JsonCodec,
      Schema
