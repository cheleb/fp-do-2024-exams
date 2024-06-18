package dotapir.server.controllers

import sttp.tapir.server.ServerEndpoint
import zio.Task

// BaseController prototype
trait BaseController {

  val routes: List[ServerEndpoint[Any, Task]]

}
