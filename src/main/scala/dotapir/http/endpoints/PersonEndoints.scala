package dotapir.http.endpoints

import sttp.tapir.*
import zio.*
import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.*

import dotapir.model.*

object PersonEndpoints extends BaseEndpoint:
  // Create person endpoint
  val createEndpoint: Endpoint[Unit, Person, Throwable, User, Any] =
    baseEndpoint
      .tag("person")
      .name("person")
      .post
      .in("person")
      .in(
        /*
         * Parses the JSON body of the HTTP request into a `Person`
         * @return The parsed `Person` object.
         */
        jsonBody[Person]
          .description("Person to create")
          .example(Person("John", 30, "jonh@dev.com"))
      )
      .out(jsonBody[User])
      .description("Create person")

  // List all users endpoint
  val listEndpoint: Endpoint[Unit, Unit, Throwable, List[User], Any] =
    baseEndpoint
      .tag("persons")
      .name("persons")
      .get
      .in("persons")
      .out(jsonBody[List[User]])  // Parse Json body ressponse to List[User]
      .description("List persons")
