package dotapir.http.endpoints

import sttp.tapir.*
import zio.*

trait HealthEndpoint {
  // Define the health endpoint
  val healthEndpoint = endpoint
    .tag("health") // Tag the endpoint as health
    .name("health") // Name the endpoint health
    .get // Make it a GET request
    .in("health") // Add /health to the path
    .out(stringBody) // Outputs a string
    .description("Health check") // Description for Swagger

}
