package dotapir.server

import zio.*
import sttp.tapir.server.ServerEndpoint

import controllers.*

object HttpApi {
  private def gatherRoutes(
      controllers: List[BaseController]
  ): List[ServerEndpoint[Any, Task]] =
    controllers.flatMap(_.routes)

  private def makeControllers = for {
    healthController <- HealthController.makeZIO
    personController <- PersonController.makeZIO
  } yield List(healthController, personController)

  /*
   * Notes:
   *
   * When adding an endpoint you should update:
   * - `XYZController.routes` with the new endpoint
   * - `XYZEndpoints.routes` with the definition for the new endpoint
   */

  val endpointsZIO = makeControllers.map(gatherRoutes)
}
