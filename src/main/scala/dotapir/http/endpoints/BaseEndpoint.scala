package dotapir.http.endpoints

import sttp.tapir.*

import dotapir.model.HttpError

trait BaseEndpoint {
  // The Endpoint Base used to create new more specific Endpoints 
  val baseEndpoint: Endpoint[Unit, Unit, Throwable, Unit, Any] = endpoint
    .errorOut(statusCode and plainBody[String])
    .mapErrorOut[Throwable](HttpError.decode)(HttpError.encode)
    .prependIn("api")

  // The secured version of baseEndpoint with Bearer Token
  val baseSecuredEndpoint: Endpoint[String, Unit, Throwable, Unit, Any] =
    baseEndpoint
      .securityIn(auth.bearer[String]())

}
