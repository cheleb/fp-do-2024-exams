package dotapir.server

import zio.*
import sttp.tapir.server.ServerEndpoint
import controllers.*
import dotapir.repository.UserRepository

object HttpApi {
  // gather all the routes from the controllers
  private def gatherRoutes(
      controllers: List[BaseController]
  ): List[ServerEndpoint[Any, Task]] =
    controllers.flatMap(_.routes)

  // create a list of all the controllers
  private def makeControllers = for {
    healthController <- HealthController.makeZIO
    personController <- PersonController.makeZIO
  } yield List(healthController, personController)

  //create a list of all the endpoints
  val endpointsZIO: ZIO[UserRepository, Nothing, List[ServerEndpoint[Any, Task]]] = makeControllers.map(gatherRoutes)
}
