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

  val getById: ServerEndpoint[Any, Task] = PersonEndpoints.getByIdEndpoint
    .zServerLogic { case (id: Long) =>
      userRepository
        .getById(id)
        // if an option is returned, throw an exception
        .someOrFail(RuntimeException(s"User $id not found"))
    }

  val delete: ServerEndpoint[Any, Task] = PersonEndpoints.deleteEndpoint
    .zServerLogic { case (id: Long) =>
      userRepository.delete(id)
    }

  val list: ServerEndpoint[Any, Task] = PersonEndpoints.listEndpoint
    .zServerLogic { _ =>
      userRepository.list()
    }

  val routes: List[ServerEndpoint[Any, Task]] =
    List(create, getById, delete, list)
}

object PersonController {
  def makeZIO =
    ZIO
      .service[UserRepository]
      .map(new PersonController(_))
}
