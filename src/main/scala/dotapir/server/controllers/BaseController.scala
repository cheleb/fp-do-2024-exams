package dotapir.server.controllers

import sttp.tapir.server.ServerEndpoint
import zio.Task

// Base controller trait with `route` value for all the controllers
trait BaseController {

  val routes: List[ServerEndpoint[Any, Task]]

}
