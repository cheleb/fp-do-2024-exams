package dotapir.model

import zio.json.JsonCodec
import sttp.tapir.Schema
import java.time.ZonedDateTime

// Define a user model
case class User(
    id: Long, // ID in the database (auto-generated)
    name: String,
    age: Int,
    email: String,
    created: ZonedDateTime
//  length: Option[Int] = None
) derives JsonCodec,
      Schema // Derive JSON codec and schema for the model so it can be used in Tapir endpoints
