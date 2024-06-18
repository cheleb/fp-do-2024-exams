package dotapir.http.endpoints

import sttp.tapir.*
import zio.*
import sttp.tapir.json.zio.*

import dotapir.model.*

object PersonEndpoints extends BaseEndpoint:
  // create a new person
  val createEndpoint: Endpoint[Unit, Person, Throwable, User, Any] =
    baseEndpoint
      // tag the endpoint with `person` for OpenApi documentation
      .tag("person")
      // name the endpoint `person` for OpenApi documentation
      .name("person")
      // specify the endpoint to be a POST request
      .post
      // specify the endpoint to be at the path `/person`, which result in `/api/person`
      .in("person")
      // specify the request body to be a `Person` object, with a description and example
      .in(
        jsonBody[Person]
          .description("Person to create")
          .example(Person("John", 30, "jonh@dev.com"))
      )
      // specify the response body to be a `User` object
      .out(jsonBody[User])
      // specify the description of the endpoint
      .description("Create person")
    // get all the users
  val getAllEndpoint: Endpoint[Unit, Unit, Throwable, List[User], Any] =
      baseEndpoint
        // tag the endpoint with `person` for OpenApi documentation      
        .tag("person")
        // name the endpoint `person` for OpenApi documentation
        .name("person")
        // specify the endpoint to be a GET request
        .get
        // specify the endpoint to be at the path `/person`, which result in `/api/person`
        .in("person")
        // specify the response body to be a list of `User` objects
        .out(jsonBody[List[User]])
        // specify the description of the endpoint
        .description("Get all persons")
