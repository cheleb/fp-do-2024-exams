package dotapir.http.endpoints

import sttp.tapir.*
import zio.*
import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.*

import dotapir.model.*

object PersonEndpoints extends BaseEndpoint:

  // A description of a POST endpoint that adds a user to the db
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
      // return User object serialized into json string
      .out(jsonBody[User])
      .description("Create person")

  // A description of a GET endpoint that returns a list of all users
  val listEndpoint: Endpoint[Unit, Unit, Throwable, List[User], Any] =
    baseEndpoint
      .tag("person")
      // The name of an endpoint needs to be unique, 
      // otherwise Swagger will confuse the same-named endpoints
      // and always execute the first one in the list
      .name("listPerson")
      .get
      .in("person")
      .out(jsonBody[List[User]])
      .description("Get a list of users")