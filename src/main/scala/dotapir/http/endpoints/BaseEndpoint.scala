package dotapir.http.endpoints

import sttp.tapir.*

import dotapir.model.HttpError

trait BaseEndpoint {
  // Define the baseEndpoint with no INPUT (Unit) and no OUTPUT (Unit) 
  val baseEndpoint: Endpoint[Unit, Unit, Throwable, Unit, Any] = endpoint
    .errorOut(statusCode and plainBody[String]) // Define error output with status code and plain body
    .mapErrorOut[Throwable](HttpError.decode)(HttpError.encode)
    .prependIn("api")

  // Define the baseSecuredEndpoint by extending the baseEndpoint and adding security
  val baseSecuredEndpoint: Endpoint[String, Unit, Throwable, Unit, Any] =
    baseEndpoint
      .securityIn(auth.bearer[String]())

}
