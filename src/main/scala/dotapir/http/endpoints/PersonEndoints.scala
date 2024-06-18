package dotapir.http.endpoints

import sttp.tapir.*
import zio.*
import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.*

import dotapir.model.*

object PersonEndpoints extends BaseEndpoint:

  /**
   * Endpoint de création d'une {@link Person}
   */
  val createEndpoint: Endpoint[Unit, Person, Throwable, User, Any] =
    baseEndpoint
      .tag("person")
      .name("person_create")
      .post
      .in("person")
      .in(
        jsonBody[Person]
          .description("Person to create")
          .example(Person("John", 30, "jonh@dev.com"))
      )
      .out(jsonBody[User])
      .description("Create person")

  /**
   * Endpoint de listing de toutes les {@link Person} en base
   */
  val listEndpoint: Endpoint[Unit, Unit, Throwable, List[User], Any] =
    baseEndpoint
      .tag("person")
      .name("person_list")
      .get
      .in("person")
      .out(jsonBody[List[User]])
      .description("List persons")
