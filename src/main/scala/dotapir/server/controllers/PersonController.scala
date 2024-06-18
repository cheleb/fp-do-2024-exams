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

  val create: ServerEndpoint[Any, Task] = PersonEndpoints.createEndpoint
    .zServerLogic { case (person) =>
      userRepository.create(
        User(-1, person.name, person.age, person.email, ZonedDateTime.now())
      )
    }

  // Declare 'list' that uses listEndpoint
  val list: ServerEndpoint[Any, Task] = PersonEndpoints.listEndpoint
    .zServerLogic { _ =>
      // Call the 'getAll' method of the userRepository to retrieve all users
      userRepository.getAll()
    }

  val routes: List[ServerEndpoint[Any, Task]] =
    List(create, list)  // List of my routes
}

// Create an object to instantiate the PersonController with ZIO
object PersonController {
  def makeZIO =
    ZIO
      .service[UserRepository]
      .map(new PersonController(_))
}
