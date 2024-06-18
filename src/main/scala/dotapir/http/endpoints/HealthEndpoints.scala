package dotapir.http.endpoints

import sttp.tapir.*
import zio.*

// Describes a single health endpoint
// This one doesn't an api prefix contrary to the other endpoints
// Wait the endpoint is defined in the trait instead of waiting for an implementation to define it
trait HealthEndpoint {
  val healthEndpoint = endpoint
    .tag("health")
    .name("health")
    .get
    .in("health")
    .out(stringBody)
    .description("Health check")

}
