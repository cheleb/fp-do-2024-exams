package dotapir.model

import zio.json.JsonCodec
import sttp.tapir.Schema

// Define a person model
case class Person(
    name: String,
    age: Int,
    email: String
) derives JsonCodec,
      Schema // Derive JSON codec and schema for the model so it can be used in Tapir endpoints
