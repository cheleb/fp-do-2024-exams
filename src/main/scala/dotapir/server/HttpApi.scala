package dotapir.server

import zio.*
import sttp.tapir.server.ServerEndpoint

import controllers.*

/** Simple objet permettant de rassembler les endpoints des différents
  * controllers. Comme chaque controller renvoie une liste d'endpoints, on peut
  * utiliser les operations map et flatMap pour les rassembler.
  */
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
