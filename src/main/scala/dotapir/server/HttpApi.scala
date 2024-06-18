package dotapir.server

import zio.*
import sttp.tapir.server.ServerEndpoint

import controllers.*

object HttpApi {
  private def gatherRoutes(
      controllers: List[BaseController]
  ): List[ServerEndpoint[Any, Task]] =
    controllers.flatMap(_.routes)

  // Merge controllers as a list
  private def makeControllers = for {
    healthController <- HealthController.makeZIO
    personController <- PersonController.makeZIO
  } yield List(healthController, personController)

  /**
   * Creates the ZIO endpoints with the controller's composition
   *
   * @return a ZIO[UserRepository, Nothing, List[BaseController]] (ZIO[Effect, Error, Success])
   */

  val endpointsZIO = makeControllers.map(gatherRoutes)
}
