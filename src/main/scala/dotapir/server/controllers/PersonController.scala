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

  // create a new user with id `-1` and current time
  val create: ServerEndpoint[Any, Task] = PersonEndpoints.createEndpoint
    .zServerLogic { case (person) =>
      userRepository.create(
        User(-1, person.name, person.age, person.email, ZonedDateTime.now())
      )
    }
  
  // get all the users
  val get: ServerEndpoint[Any, Task] = PersonEndpoints.getAllEndpoint
    .zServerLogic { _ =>
      userRepository.getAll
    }

  // create a list of all the routes
  val routes: List[ServerEndpoint[Any, Task]] =
    List(create, get)
}

object PersonController {
  def makeZIO =
    ZIO
      .service[UserRepository]
      .map(new PersonController(_))
}
