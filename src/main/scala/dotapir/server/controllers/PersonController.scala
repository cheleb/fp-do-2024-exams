package dotapir.server.controllers

import zio.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.ztapir.*

import dotapir.http.endpoints.PersonEndpoints
import dotapir.model.User
import java.time.LocalDateTime
import java.time.ZonedDateTime
import dotapir.repository.UserRepository

import dotapir.model.*

class PersonController(userRepository: UserRepository) extends BaseController {

  // La définition fonctionnelle de l'endpoint create qui va traiter les requêtes avec l'objet Person
  val create: ServerEndpoint[Any, Task] = PersonEndpoints.createEndpoint
    .zServerLogic { case (person) =>  // S'attend à recevoir un objet Person pour renvoyer une réponse en fonction de celui ci
      userRepository.create(
        User(-1, person.name, person.age, person.email, ZonedDateTime.now())
      )
    }
  
  // Définition fonctionnelle de l'endpoint qui renvoie toute la liste des Utilisateurs
  val list: ServerEndpoint[Any, Task] = PersonEndpoints.listPersonsEndpoint
    .zServerLogic { case () =>
      userRepository.getAll.mapError {
        case e: Throwable => e
      }
  }

  // Déclaration des routes à partir des endpoints définis, utilisé plus tard par le Swagger
  val routes: List[ServerEndpoint[Any, Task]] =
    List(create, list)
}

// La définition du singleton controller qui va utiliser le service UserRepository et la classe PersonController
object PersonController {
  def makeZIO =
    ZIO
      .service[UserRepository]
      .map(new PersonController(_))
}
