package dotapir.server.controllers

import sttp.tapir.server.ServerEndpoint
import zio.Task

// Define a base controller that all controllers must extend
trait BaseController {

  // A controller only has a list of routes
  val routes: List[ServerEndpoint[Any, Task]]

}
