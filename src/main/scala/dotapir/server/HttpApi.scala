package dotapir.server

import zio.*
import sttp.tapir.server.ServerEndpoint

import controllers.*

object HttpApi {
  // This gets all the routes that are defined in the controllers and combines them
  private def gatherRoutes(
      controllers: List[BaseController]
  ): List[ServerEndpoint[Any, Task]] =
    controllers.flatMap(_.routes)

  // This defines all the controller in this application (the health and person controllers)
  private def makeControllers = for {
    healthController <- HealthController.makeZIO
    personController <- PersonController.makeZIO
  } yield List(healthController, personController)

  // This is the list of all the endpoints that are defined in this application
  val endpointsZIO = makeControllers.map(gatherRoutes)
}
