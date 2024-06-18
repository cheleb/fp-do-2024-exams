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

  /**
   * Implémentation ZIO de l'endpoint de création de personnes.
   * ({@link PersonEndpoints.createEndpoint})
   */
  val create: ServerEndpoint[Any, Task] = PersonEndpoints.createEndpoint
    .zServerLogic { case (person) =>
      userRepository.create(
        User(-1, person.name, person.age, person.email, ZonedDateTime.now())
      )
    }

  /**
   * Implémentation ZIO de l'endpoint de listing de personnes.
   * ({@link PersonEndpoints.listEndpoint})
   */
  val getAll: ServerEndpoint[Any, Task] = PersonEndpoints.listEndpoint
    .zServerLogic { case () =>
      userRepository.getAll()
    }

  /**
   * Liste des routes à rendre disponibles auprès du serveur ZIO
   */
  val routes: List[ServerEndpoint[Any, Task]] =
    List(create, getAll)
}

object PersonController {
  /**
   * Ajoute le user repository comme service disponible et renvoie le controller.
   * @return un {@link PersonController} layeré avec le service {@link UserRepository}
   */
  def makeZIO: ZIO[UserRepository, Nothing, PersonController] =
    ZIO
      .service[UserRepository]
      .map(new PersonController(_))
}
