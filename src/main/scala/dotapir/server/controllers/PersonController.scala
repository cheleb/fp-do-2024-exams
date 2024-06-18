package dotapir.server.controllers

import zio.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.ztapir.*

import dotapir.http.endpoints.PersonEndpoints
import dotapir.model.User
import java.time.LocalDateTime
import java.time.ZonedDateTime
import dotapir.repository.UserRepository

// controller to interact with person data
class PersonController(userRepository: UserRepository) extends BaseController {

  // create a new user from a person - use the endpoint createEndpoint from PersonEndpoints
  val create: ServerEndpoint[Any, Task] = PersonEndpoints.createEndpoint
    // case person => if a person was given
    .zServerLogic { case (person) =>
      // create the user
      userRepository.create(
        // -1 as id since it is necessary for the object User but no need to put a real value because the id is generated automatically in the db
        User(-1, person.name, person.age, person.email, ZonedDateTime.now())
      )
    }
  
    // get all users - use the endpoint listEndpoint from PersonEndpoints
    val get: ServerEndpoint[Any, Task] = PersonEndpoints.listEndpoint
    // case () since no arguments are needed
    .zServerLogic { case () =>
      // get all users
      userRepository.get()
    }

  // define all existing routes - here create user and get all users
  // Task[A] is a type alias for ZIO[Any, Throwable, A]
  val routes: List[ServerEndpoint[Any, Task]] =
    List(create, get)
}

object PersonController {
  def makeZIO =
    ZIO
      .service[UserRepository]
      .map(new PersonController(_))
}
