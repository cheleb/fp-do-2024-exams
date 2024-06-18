package dotapir.model

import zio.json.JsonCodec
import sttp.tapir.Schema

// model for person
case class Person(
    name: String,
    age: Int,
    email: String
    // JsonCodec - serialize / deserialize to Json
) derives JsonCodec,
      Schema
