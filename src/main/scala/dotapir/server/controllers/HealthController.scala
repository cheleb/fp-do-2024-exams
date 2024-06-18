package dotapir.server.controllers

import zio.*
import sttp.tapir.*
import sttp.tapir.server.ServerEndpoint
import dotapir.http.endpoints.HealthEndpoint

class HealthController private extends BaseController with HealthEndpoint {

  // return "OK" if the service is running
  val health = healthEndpoint
    .serverLogicSuccess[Task](_ => ZIO.succeed("OK"))
  // create a list of the only route
  override val routes: List[ServerEndpoint[Any, Task]] = List(health)
}

object HealthController {
  // create a new instance of the controller
  // uses ZIO.succeed to ensure the controller does not need any computation, as it is already successful
  val makeZIO = ZIO.succeed(new HealthController)
}
