package dotapir.http.endpoints

import sttp.tapir.*
import zio.*
import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.*

import dotapir.model.*

object PersonEndpoints extends BaseEndpoint:

  // The Endpoint Definitions with method types, tag, expected request and the kind of response
  val createEndpoint: Endpoint[Unit, Person, Throwable, User, Any] =
    baseEndpoint
      .tag("person")
      .name("person")
      .post
      .in("person")
      .in(
        jsonBody[Person]
          .description("Person to create")
          .example(Person("John", 30, "jonh@dev.com"))
      )
      .out(jsonBody[User])
      .description("Create person")

  val listPersonsEndpoint: Endpoint[Unit, Unit, Throwable, List[User], Any] =
    baseEndpoint
      .tag("person")
      .name("listPersons")
      .get
      .in("person")
      .out(jsonBody[List[User]])
      .description("Get list of persons")
