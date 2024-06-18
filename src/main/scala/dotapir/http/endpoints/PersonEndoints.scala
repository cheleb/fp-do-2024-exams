package dotapir.http.endpoints

import sttp.tapir.*
import zio.*
import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.*

import dotapir.model.*

// This contains all the HTTP endpoints related to the person controller
object PersonEndpoints extends BaseEndpoint:
  // This is the person create endpoint that defines a POST request to the /person path
  // It receives a Person object in the request body as a JSON object and returns a User object in
  // the response body as a JSON object
  // An example for the request body is also provided for the OpenAPI specification
  val createEndpoint: Endpoint[Unit, Person, Throwable, User, Any] =
    baseEndpoint
      .tag("person")
      .name("create-person")
      .post
      .in("person")
      .in(
        jsonBody[Person]
          .description("Person to create")
          .example(Person("John", 30, "jonh@dev.com"))
      )
      .out(jsonBody[User])
      .description("Create person")

  // This is the person get endpoint that defines a GET request to the /person path
  // It returns all the users in the response body as a JSON array
  val getAllEndpoint: Endpoint[Unit, Unit, Throwable, List[User], Any] =
    baseEndpoint
      .tag("person")
      .name("get-all-persons")
      .get
      .in("person")
      .out(jsonBody[List[User]])
      .description("Get all persons")
