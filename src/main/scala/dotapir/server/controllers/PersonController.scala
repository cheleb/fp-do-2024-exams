package dotapir.server.controllers

import zio.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.ztapir.*

import dotapir.http.endpoints.PersonEndpoints
import dotapir.model.User
import java.time.LocalDateTime
import java.time.ZonedDateTime
import dotapir.repository.UserRepository

// This controller is used to interact with the Person entity
class PersonController(userRepository: UserRepository) extends BaseController {
  // This implements the endpoint to create a new person by destructuring the Person received from
  // the request body, and delegating the creation to the repository
  val create: ServerEndpoint[Any, Task] = PersonEndpoints.createEndpoint
    .zServerLogic { case (person) =>
      userRepository.create(
        User(-1, person.name, person.age, person.email, ZonedDateTime.now())
      )
    }

  // This implements the endpoint to get all persons by delegating the retrieval to the repository
  val getAll: ServerEndpoint[Any, Task] = PersonEndpoints.getAllEndpoint
    .zServerLogic { case () =>
      userRepository.getAll
    }

  // This is a list of the routes defined by this controller (here, the POST /person and GET /person routes)
  val routes: List[ServerEndpoint[Any, Task]] =
    List(create, getAll)
}

object PersonController {
  // This creates the global person controller used in the application - because we are using the
  // ZIO runtime, we also need to wrap the controller in a ZIO effect
  def makeZIO =
    ZIO
      .service[UserRepository]
      .map(new PersonController(_))
}
