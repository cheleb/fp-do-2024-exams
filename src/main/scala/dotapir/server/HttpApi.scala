package dotapir.server

import zio.*
import sttp.tapir.server.ServerEndpoint

import controllers.*

object HttpApi {
  // Get all routes from all controllers in parameter
  private def gatherRoutes(
      controllers: List[BaseController]
  ): List[ServerEndpoint[Any, Task]] =
    controllers.flatMap(_.routes)

  // Initialize all controllers
  private def makeControllers = for {
    healthController <- HealthController.makeZIO
    personController <- PersonController.makeZIO
  } yield List(healthController, personController)

  // Initialize all controllers and gather their routes
  val endpointsZIO = makeControllers.map(gatherRoutes)
}
