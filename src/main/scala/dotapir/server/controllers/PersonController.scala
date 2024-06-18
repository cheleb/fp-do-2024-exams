package dotapir.server.controllers

import zio.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.ztapir.*

import dotapir.http.endpoints.PersonEndpoints
import dotapir.model.User
import java.time.ZonedDateTime
import dotapir.repository.UserRepository

class PersonController(userRepository: UserRepository) extends BaseController {

  // Endpoint pour créer une nouvelle personne
  // zServerLogic est une méthode qui permet de définir la logique du serveur pour un endpoint
  // le case (person) est le paramètre de l'endpoint
  val create: ServerEndpoint[Any, Task] = PersonEndpoints.createEndpoint
    .zServerLogic { case (person) =>
      userRepository.create(
        User(-1, person.name, person.age, person.email, ZonedDateTime.now())
      )
    }

  // Endpoint pour récupérer toutes les personnes
  val getAll: ServerEndpoint[Any, Task] = PersonEndpoints.getAllEndpoint
    .zServerLogic { case _ =>
      userRepository.getAll()
    }

  // Liste des endpoints disponibles dans ce contrôleur
  val routes: List[ServerEndpoint[Any, Task]] =
    List(create, getAll)
}

object PersonController {
  // Méthode pour créer une instance de PersonController en utilisant ZIO
  def makeZIO =
    ZIO
      .service[UserRepository]
      .map(new PersonController(_))
}
