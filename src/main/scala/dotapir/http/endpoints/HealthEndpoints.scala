package dotapir.http.endpoints

import sttp.tapir.*
import zio.*

// This contains all the HTTP endpoints related to the health controller
trait HealthEndpoint {
  // This is the health endpoint that defines a GET request to the /health path, which returns a string
  // There's also the tag, name and description properties that are used when generating the
  // OpenAPI specification
  val healthEndpoint = endpoint
    .tag("health")
    .name("health")
    .get
    .in("health")
    .out(stringBody)
    .description("Health check")
}
