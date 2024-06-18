package dotapir.model

import zio.json.JsonCodec
import sttp.tapir.Schema
import java.time.ZonedDateTime

// This defines a user
case class User(
    id: Long,
    name: String,
    age: Int,
    email: String,
    created: ZonedDateTime
//  length: Option[Int] = None
) derives JsonCodec,
      Schema
