package dotapir.model

import zio.json.JsonCodec
import sttp.tapir.Schema
import java.time.ZonedDateTime

// User model which can be derived from `JsonCodec` and `Schema` to be translated to JSON and OpenAPI schema
case class User(
    id: Long,
    name: String,
    age: Int,
    email: String,
    created: ZonedDateTime
//  length: Option[Int] = None
) derives JsonCodec,
      Schema
