package dotapir.http.endpoints

import sttp.tapir.*

import dotapir.model.HttpError

// Define a base endpoint that all endpoints must extend
trait BaseEndpoint {
  val baseEndpoint: Endpoint[Unit, Unit, Throwable, Unit, Any] = endpoint
    .errorOut(statusCode and plainBody[String]) // Define the error output as a status code and a message
    .mapErrorOut[Throwable](HttpError.decode)(HttpError.encode) // Map the error to a HttpError
    .prependIn("api") // start all base endpoints with /api (for example GET /api/person)

  // Define a secured base endpoint (not used in this project)
  val baseSecuredEndpoint: Endpoint[String, Unit, Throwable, Unit, Any] =
    baseEndpoint
      .securityIn(auth.bearer[String]())

}
