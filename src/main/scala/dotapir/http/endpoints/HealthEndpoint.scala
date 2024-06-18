package dotapir.http.endpoints

import sttp.tapir.*

trait HealthEndpoint {
  val healthEndpoint: Endpoint[Unit, Unit, Unit, String, Any] = endpoint
    // tag the endpoint with `health` for OpenApi documentation
    .tag("health")
    // name the endpoint `health` for OpenApi documentation
    .name("health")
    // specify the endpoint to be a GET request
    .get
    // specify the endpoint to be at the path `/health`
    .in("health")
    // specify the response body to be a string
    .out(stringBody)
    // specify the description of the endpoint
    .description("Health check")
}
