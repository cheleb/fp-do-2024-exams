package dotapir.http.endpoints

import sttp.tapir.*
import zio.*

trait HealthEndpoint {
  // Define the /health endpoint
  val healthEndpoint = endpoint
    .tag("health")                // For documentation only
    .name("health")               // For documentation only
    .get                          // REST Method (Get, Post, Patch, Put, Delete)
    .in("health")                 // Endpoint path (/health)
    .out(stringBody)              // Endpoint output
    .description("Health check")  // Description (for documentation only)
}
