package dotapir.server.controllers

import zio.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.ztapir.*

import dotapir.http.endpoints.PersonEndpoints
import dotapir.model.User
import java.time.LocalDateTime
import java.time.ZonedDateTime
import dotapir.repository.UserRepository


// La classe PersonController étend BaseController et prend un userRepository 
// comme paramètre pour accéder aux opérations CRUD sur les utilisateurs. 
// Elle définit deux points d'entrée serveur (endpoints) : create et getAll.

class PersonController(userRepository: UserRepository) extends BaseController {

  // PersonEndpoints.createEndpoint :
  // Référence à l'endpoint de création d'un utilisateur défini dans PersonEndpoints
  val create: ServerEndpoint[Any, Task] = PersonEndpoints.createEndpoint

  // zServerLogic correspond à la logique de l'endpoint
    .zServerLogic { case (person) =>

      // Fait appel à la méthode create de userRepository pour créer un nouvel utilisateur
      userRepository.create(
        User(-1, person.name, person.age, person.email, ZonedDateTime.now())
      )
    }

  val getAll: ServerEndpoint[Any, Task] = PersonEndpoints.getAllEndpoint
    .zServerLogic { case () =>
      userRepository.getAll
    } 

  // La méthode routes retourne une liste des points d'entrée serveur 
  val routes: List[ServerEndpoint[Any, Task]] =
    List(create, getAll)

}

object PersonController {
  def makeZIO =
    ZIO
      .service[UserRepository]
      .map(new PersonController(_))
}


// Les points qui pourraient être améliorés dans ce code sont les suivants :
//
// 1. Gestion des erreurs: Le code ne gère pas explicitement les erreurs. 
//
// 2. ID utilisateur: L'initialisation de l'ID utilisateur à -1 est un choix qui pourrait prêter à confusion. 
//    Il serait préférable de le gérer de manière plus explicite, peut-être en le laissant null ou en utilisant une option.
//
// 3. Validation des entrées (DTO): La validation des données d'entrée n'est pas prise en compte. 
//    Il serait utile de valider les informations de l'utilisateur avant de les enregistrer dans la base de données.