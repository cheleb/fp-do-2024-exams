package dotapir.http.endpoints

import sttp.tapir.*
import zio.*
import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.*

import dotapir.model.*

// Describe a post create endpoint and a list get endpoint for persons
object PersonEndpoints extends BaseEndpoint:

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

  //  Define the endpoint to list all persons
  val listEndpoint: Endpoint[Unit, Unit, Throwable, List[User], Any] =
    baseEndpoint
      .tag("person") // Base tag for swagger docs
      .name("persons") // endpoint tag for swagger docs
      .get // Accepts get requests
      .in("persons") // endpoint path
      .out(jsonBody[List[User]]) // Define that the return type of the api is a list of users
      .description("List all persons") // Description for swagger docs