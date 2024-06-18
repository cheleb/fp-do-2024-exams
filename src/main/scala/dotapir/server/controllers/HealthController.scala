package dotapir.server.controllers

import zio.*
import sttp.tapir.*
import sttp.tapir.server.ServerEndpoint
import dotapir.http.endpoints.HealthEndpoint

class HealthController private extends BaseController with HealthEndpoint {

  val health = healthEndpoint
    .serverLogicSuccess[Task](_ => ZIO.succeed("OK")) // Return OK when the health endpoint is called
  override val routes: List[ServerEndpoint[Any, Task]] = List(health)
}

object HealthController {
  // Initialize the controller
  val makeZIO = ZIO.succeed(new HealthController)
}
