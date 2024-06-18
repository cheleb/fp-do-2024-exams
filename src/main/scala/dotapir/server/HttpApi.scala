package dotapir.server

import zio.*
import sttp.tapir.server.ServerEndpoint

import controllers.*

object HttpApi {
  // Récupère toutes les routes à partir des Controllers
  private def gatherRoutes(
      controllers: List[BaseController]
  ): List[ServerEndpoint[Any, Task]] =
    controllers.flatMap(_.routes)

  // Récupères toutes les endpoints
  private def makeControllers = for {
    healthController <- HealthController.makeZIO
    personController <- PersonController.makeZIO
  } yield List(healthController, personController)

  // Récupère toutes les routes à partir de tous les controllers
  val endpointsZIO = makeControllers.map(gatherRoutes)
}
