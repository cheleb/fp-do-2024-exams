package dotapir.http.endpoints

import sttp.tapir.*

import dotapir.model.HttpError

// Define the base endpoint with error handling
trait BaseEndpoint {
  // handle the error with `HttpError` model
  val baseEndpoint: Endpoint[Unit, Unit, Throwable, Unit, Any] = endpoint
    .errorOut(statusCode and plainBody[String])
    .mapErrorOut[Throwable](HttpError.decode)(HttpError.encode)
    .prependIn("api")

  // handle the secured endpoint with bearer token
  val baseSecuredEndpoint: Endpoint[String, Unit, Throwable, Unit, Any] =
    baseEndpoint
      .securityIn(auth.bearer[String]())

}
