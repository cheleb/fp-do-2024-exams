package dotapir.server.controllers

import sttp.tapir.server.ServerEndpoint
import zio.Task

// This trait is implemented by all the server controllers
trait BaseController {

  // This is the list of the HTTP routes served by this controller
  val routes: List[ServerEndpoint[Any, Task]]

}
