package dotapir.http.endpoints

import sttp.tapir.*
import zio.*
import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.*

import dotapir.model.*

object PersonEndpoints extends BaseEndpoint:

  // nos endpoints vont être définis ici et seront utilisés dans le contrôleur
  // pour définir les routes de notre serveur
  // Swagger va aussi utiliser ces endpoints pour générer la documentation
  val createEndpoint: Endpoint[Unit, Person, Throwable, User, Any] =
    baseEndpoint
      .tag("person")
      .name("person")
      .post
      .in("person")
      .in(
        jsonBody[Person]
          .description("Person to create")
          .example(Person("John", 30, "john@dev.com"))
      )
      .out(jsonBody[User])
      .description("Create person")


  val getAllEndpoint: Endpoint[Unit, Unit, Throwable, List[User], Any] =
    baseEndpoint
      .tag("person")
      .name("person")
      .get
      .in("person")
      .out(jsonBody[List[User]])
      .description("Get all persons")