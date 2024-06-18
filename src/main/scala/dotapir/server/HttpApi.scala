package dotapir.server

import zio.*
import sttp.tapir.server.ServerEndpoint

import controllers.*

object HttpApi {

  // Fonction privée pour rassembler les routes des différents contrôleurs
  private def gatherRoutes(
      controllers: List[BaseController]
  ): List[ServerEndpoint[Any, Task]] =
    controllers.flatMap(_.routes)

  // Fonction privée pour créer les instances des contrôleurs
  private def makeControllers = for {
    healthController <- HealthController.makeZIO // Crée une instance de HealthController
    personController <- PersonController.makeZIO // Crée une instance de PersonController
  } yield List(healthController, personController) // Retourne une liste des contrôleurs créés

  // Variable contenant tous les endpoints ZIO du projet
  val endpointsZIO = makeControllers.map(gatherRoutes)
}
