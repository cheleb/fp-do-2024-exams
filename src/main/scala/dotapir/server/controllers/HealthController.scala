package dotapir.server.controllers

import zio.*
import sttp.tapir.*
import sttp.tapir.server.ServerEndpoint
import dotapir.http.endpoints.HealthEndpoint

// This controller is used to check if the server is running
// It uses the HealthEndpoint trait as a mixin to access the health endpoint directly
class HealthController private extends BaseController with HealthEndpoint {
  // This implements the health endpoint, returning the string "OK" as the response
  val health = healthEndpoint
    .serverLogicSuccess[Task](_ => ZIO.succeed("OK"))

  // This is a list of the routes defined by this controller (here, only the GET /health route)
  override val routes: List[ServerEndpoint[Any, Task]] = List(health)
}

object HealthController {
  // This creates the global health controller used in the application
  val makeZIO = ZIO.succeed(new HealthController)
}
