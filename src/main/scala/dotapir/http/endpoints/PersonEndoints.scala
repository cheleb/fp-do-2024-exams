package dotapir.http.endpoints

import sttp.tapir.*
import zio.*
import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.*

import dotapir.model.*

object PersonEndpoints extends BaseEndpoint:

  val createEndpoint: Endpoint[Unit, Person, Throwable, User, Any] =
    baseEndpoint
      .tag("persons")
      .name("create-person")
      .post
      .in("persons")
      .in(
        jsonBody[Person]
          .description("Person to create")
          .example(Person("John", 30, "jonh@dev.com"))
      )
      .out(jsonBody[User])
      .description("Create a person")

  val deleteEndpoint: Endpoint[Unit, Long, Throwable, User, Any] =
    baseEndpoint
      .tag("persons")
      .name("delete-person")
      .delete
      .in("persons")
      .in(
        path[Long]("id")
          .description("ID of the person to delete")
          .example(1)
      )
      .out(jsonBody[User])
      .description("Delete a person")

  val listEndpoint: Endpoint[Unit, Unit, Throwable, List[User], Any] =
    baseEndpoint
      .tag("persons")
      .name("list-persons")
      .get
      .in("persons")
      .out(jsonBody[List[User]])
      .description("List all persons")
