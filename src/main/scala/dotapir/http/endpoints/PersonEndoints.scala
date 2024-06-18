package dotapir.http.endpoints

import sttp.tapir.*
import zio.*
import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.*

import dotapir.model.*

object PersonEndpoints extends BaseEndpoint:

  // Endpoint used to create a person
  val createEndpoint: Endpoint[Unit, Person, Throwable, User, Any] =
    baseEndpoint
      .tag("person") // Tag for the endpoint
      .name("person") // Name for the endpoint
      .post // Make it a POST request
      .in("person") // Add /person to the path
      .in( // Request body is a person
        jsonBody[Person]
          .description("Person to create") // Description for Swagger
          .example(Person("John", 30, "jonh@dev.com")) // Example for Swagger
      )
      .out(jsonBody[User]) // Outputs a user
      .description("Create person") // Description for Swagger

  // Endpoint used to list all persons
  val listEndpoint: Endpoint[Unit, Unit, Throwable, List[User], Any] =
    baseEndpoint
      .tag("person") // Tag for the endpoint
      .name("person") // Name for the endpoint
      .get // Make it a GET request
      .in("person") // Add /person to the path
      .out(jsonBody[List[User]]) // Outputs a list of users
      .description("List all persons") // Description for Swagger