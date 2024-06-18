package dotapir.http.endpoints

import sttp.tapir.*
import zio.*

// Definition of the Health Check endpoint.....
trait HealthEndpoint {
  val healthEndpoint = endpoint
    .tag("health")
    .name("health")
    .get
    .in("health")
    .out(stringBody)
    .description("Health check")

}
