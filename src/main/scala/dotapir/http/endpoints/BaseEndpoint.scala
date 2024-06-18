package dotapir.http.endpoints

import sttp.tapir.*

import dotapir.model.HttpError

trait BaseEndpoint {
  // This defines the common configuration for all endpoints
  // Notably, each endpoints is prefixed with "api"
  val baseEndpoint: Endpoint[Unit, Unit, Throwable, Unit, Any] = endpoint
    .errorOut(statusCode and plainBody[String])
    .mapErrorOut[Throwable](HttpError.decode)(HttpError.encode)
    .prependIn("api")

  // This expends the base endpoint defined above with the need for a Bearer token
  val baseSecuredEndpoint: Endpoint[String, Unit, Throwable, Unit, Any] =
    baseEndpoint
      .securityIn(auth.bearer[String]())

}
