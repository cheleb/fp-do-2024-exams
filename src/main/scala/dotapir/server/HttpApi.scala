package dotapir.server

import zio.*
import sttp.tapir.server.ServerEndpoint

import controllers.*

// Object to gather all the controllers and their routes
object HttpApi {
  private def gatherRoutes(
      controllers: List[BaseController]
  ): List[ServerEndpoint[Any, Task]] =
    controllers.flatMap(_.routes)

  private def makeControllers = for {
    healthController <- HealthController.makeZIO
    personController <- PersonController.makeZIO
  } yield List(healthController, personController)

  val endpointsZIO = makeControllers.map(gatherRoutes)
}
