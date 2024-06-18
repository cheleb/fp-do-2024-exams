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

  // Make the create endpoint use the create method from the repository
  val create: ServerEndpoint[Any, Task] = PersonEndpoints.createEndpoint
    .zServerLogic { case (person) =>
      // Take a person as input and save it to the repository as a user
      userRepository.create(
        // Create a new user with the person object with the current time as the creation time and auto-generated ID
        User(-1, person.name, person.age, person.email, ZonedDateTime.now())
      )
    }
  
  // Make the list endpoint use the list method from the repository
  val list: ServerEndpoint[Any, Task] = PersonEndpoints.listEndpoint
    .zServerLogic { _ =>
    // Retrieve all users from the repository
      userRepository.list
    }

  // List of all routes used in this controller
  val routes: List[ServerEndpoint[Any, Task]] =
    List(create, list)
}

object PersonController {
  // Initialize the controller with the repository
  def makeZIO =
    ZIO
      .service[UserRepository]
      .map(new PersonController(_))
}
