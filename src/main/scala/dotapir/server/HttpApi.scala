package dotapir.server

import zio.*
import sttp.tapir.server.ServerEndpoint

import controllers.*

object HttpApi {
  // get all routes from a list of basecontroller
  private def gatherRoutes(
      controllers: List[BaseController]
  ): List[ServerEndpoint[Any, Task]] =
    controllers.flatMap(_.routes)

  // get all controllers and put them in a list
  private def makeControllers = for {
    // get healthController
    healthController <- HealthController.makeZIO
    // get personController
    personController <- PersonController.makeZIO
    // if any other Controller => add here
  } yield List(healthController, personController)

  // create the endpoints for all controllers
  val endpointsZIO = makeControllers.map(gatherRoutes)
}
