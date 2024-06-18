package dotapir.server

import zio.*
import sttp.tapir.server.ServerEndpoint
import controllers.*
import dotapir.repository.UserRepository

object HttpApi {
  /**
   * Récolte et mappe les routes des controllers donnés en paramètre.
   * @param controllers la liste des controllers à utiliser pour le serveur HTTP ZIO
   * @return la liste de toutes les routes collectées
   */
  private def gatherRoutes(
      controllers: List[BaseController]
  ): List[ServerEndpoint[Any, Task]] =
    controllers.flatMap(_.routes)

  /**
   * @return La liste des controllers à charger
   */
  private def makeControllers = for {
    healthController <- HealthController.makeZIO
    personController <- PersonController.makeZIO
  } yield List(healthController, personController)

  /**
   * La liste des endpoints, en chargeant d'abord les controllers avec {@link makeControllers}
   * puis en mappant leurs routes avec {@link gatherRoutes}
   */
  val endpointsZIO: ZIO[UserRepository, Nothing, List[ServerEndpoint[Any, Task]]] = 
    makeControllers.map(gatherRoutes)
}
