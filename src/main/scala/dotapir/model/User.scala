package dotapir.model

import zio.json.JsonCodec
import sttp.tapir.Schema
import java.time.ZonedDateTime

// Nice case class, that defines a User
// It uses JsonCodec to be (de)-serialized into json and Schema for the DB representation
case class User(
    id: Long,
    name: String,
    age: Int,
    email: String,
    created: ZonedDateTime
//  length: Option[Int] = None
) derives JsonCodec,
      Schema
