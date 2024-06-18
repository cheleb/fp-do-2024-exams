package dotapir.http.endpoints

import sttp.tapir.*

import dotapir.model.HttpError

// Trait contenant des endpoints de base
trait BaseEndpoint {
  // Endpoint de base sans sécurité
  val baseEndpoint: Endpoint[Unit, Unit, Throwable, Unit, Any] = endpoint
    .errorOut(statusCode and plainBody[String]) // Définition de la sortie en cas d'erreur
    .mapErrorOut[Throwable](HttpError.decode)(HttpError.encode) // Mappage des erreurs vers HttpError
    .prependIn("api") // Préfixe du chemin de l'API

  // Endpoint de base sécurisé avec JWT Bearer token
  val baseSecuredEndpoint: Endpoint[String, Unit, Throwable, Unit, Any] =
    baseEndpoint
      .securityIn(auth.bearer[String]()) // Sécurité : token Bearer JWT
}
