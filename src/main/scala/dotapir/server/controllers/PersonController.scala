package dotapir.server.controllers

import zio.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.ztapir.*

import dotapir.http.endpoints.PersonEndpoints
import dotapir.model.User
import java.time.LocalDateTime
import java.time.ZonedDateTime
import dotapir.repository.UserRepository

class PersonController(userRepository: UserRepository) extends BaseController {

  // create a new user from a person
  val create: ServerEndpoint[Any, Task] = PersonEndpoints.createEndpoint
    // case person => if a person was given
    .zServerLogic { case (person) =>
      userRepository.create(
        // -1 as id since it is necessary for the object User but no need to put a real value because the id is generated automatically in the db
        User(-1, person.name, person.age, person.email, ZonedDateTime.now())
      )
    }
  
    val get: ServerEndpoint[Any, Task] = PersonEndpoints.listEndpoint
    .zServerLogic { case () =>
      userRepository.get()
    }

  val routes: List[ServerEndpoint[Any, Task]] =
    List(create, get)
}

object PersonController {
  def makeZIO =
    ZIO
      .service[UserRepository]
      .map(new PersonController(_))
}
