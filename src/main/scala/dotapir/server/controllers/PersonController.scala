package dotapir.server.controllers

import zio.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.ztapir.*

import dotapir.http.endpoints.PersonEndpoints
import dotapir.model.User
import java.time.LocalDateTime
import java.time.ZonedDateTime
import dotapir.repository.UserRepository

/** User repository est injecté dans le controller grâce à ZIO, on doit
  * évidemment le fournir dans les dépandances de l'application. (HttpApi.scala)
  */
class PersonController(userRepository: UserRepository) extends BaseController {

  val create: ServerEndpoint[Any, Task] = PersonEndpoints.createEndpoint
    .zServerLogic { case (person) =>
      userRepository.create(
        User(-1, person.name, person.age, person.email, ZonedDateTime.now())
      )
    }

  val list: ServerEndpoint[Any, Task] = PersonEndpoints.listEndpoint
    .zServerLogic { case () =>
      userRepository.getAll
    }

  val routes: List[ServerEndpoint[Any, Task]] =
    List(list, create)
}

object PersonController {
  def makeZIO =
    ZIO
      .service[UserRepository]
      .map(new PersonController(_))
}
