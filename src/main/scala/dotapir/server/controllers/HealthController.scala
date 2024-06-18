package dotapir.server.controllers

import zio.*
import sttp.tapir.*
import sttp.tapir.server.ServerEndpoint
import dotapir.http.endpoints.HealthEndpoint

// Contrôleur de la santé de l'application
class HealthController private extends BaseController with HealthEndpoint {

  // Endpoint de santé
  val health = healthEndpoint
    .serverLogicSuccess[Task](_ => ZIO.succeed("OK")) // Logique du serveur : renvoie "OK" dans un effet ZIO
  override val routes: List[ServerEndpoint[Any, Task]] = List(health) // Liste des endpoints du contrôleur
}

object HealthController {
  // Méthode pour créer une instance de HealthController avec ZIO
  val makeZIO = ZIO.succeed(new HealthController)
}
