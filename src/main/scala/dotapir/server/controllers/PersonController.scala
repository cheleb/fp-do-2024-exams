package dotapir.server.controllers

import zio.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.ztapir.*

import dotapir.http.endpoints.PersonEndpoints
import dotapir.model.User
import java.time.LocalDateTime
import java.time.ZonedDateTime
import dotapir.repository.UserRepository

// A controller for the '/api/person' endpoint
// Uses dependency injection to get a UserRepositoryLive object
class PersonController(userRepository: UserRepository) extends BaseController {

  // Add business logic to the POST endpoint description from 'http/endpoints'
  val create: ServerEndpoint[Any, Task] = PersonEndpoints.createEndpoint
    .zServerLogic { case (person) =>
      // Add a user to the db via the 
      userRepository.create(
        User(-1, person.name, person.age, person.email, ZonedDateTime.now())
      )
    }

  // Add business logic to the GET endpoint description from 'http/endpoints'
  val getAll: ServerEndpoint[Any, Task] = PersonEndpoints.listEndpoint
    .zServerLogic { case () =>
      // Get a list of users from the db
      userRepository.getAll()
    }

  // Add endpoints to the list of PersonController routes
  val routes: List[ServerEndpoint[Any, Task]] =
    List(create, getAll)
}

object PersonController {
  def makeZIO =
    ZIO
      // Require injection of a UserRepository object in the controller
      .service[UserRepository]
      .map(new PersonController(_))
}
