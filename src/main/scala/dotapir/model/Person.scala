package dotapir.model

import zio.json.JsonCodec
import sttp.tapir.Schema

// Person class used to create a new User
case class Person(
    name: String,
    age: Int,
    email: String
// Use jsonCodec operator to derive JSON from ZIO schemas
) derives JsonCodec,
      Schema
