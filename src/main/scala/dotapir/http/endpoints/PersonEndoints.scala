package dotapir.http.endpoints

import sttp.tapir.*
import zio.*
import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.*

import dotapir.model.*

object PersonEndpoints extends BaseEndpoint:

  // create endpoint to create a user from a person
  val createEndpoint: Endpoint[Unit, Person, Throwable, User, Any] =
    baseEndpoint
      // tag - to regroup with swagger
      .tag("person")
      .name("person")
      .post
      // endpoint path
      .in("person")
      // expected body type
      .in(
        jsonBody[Person]
          .description("Person to create")
          // example for body / placeholder / test
          .example(Person("John", 30, "jonh@dev.com"))
      )
      // return type
      .out(jsonBody[User])
      .description("Create person")
      
  val listEndpoint: Endpoint[Unit, Unit, Throwable, List[User], Any] =
    baseEndpoint
      .tag("person")
      .name("persons")
      .get
      .in("persons")
      .out(jsonBody[List[User]])
      .description("Get all persons")