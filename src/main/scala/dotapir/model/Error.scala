package dotapir.model

import sttp.model.StatusCode

// Modèle représentant une erreur HTTP
final case class HttpError(
    statusCode: StatusCode, // Code de statut HTTP
    message: String,        // Message d'erreur
    cause: Throwable        // Cause de l'erreur (Throwable)
) extends RuntimeException(message, cause) // Hérite de RuntimeException avec le message et la cause

object HttpError {
  // Méthode pour décoder un tuple (StatusCode, String) en HttpError
  def decode(tuple: (StatusCode, String)): HttpError =
    HttpError(tuple._1, tuple._2, new RuntimeException(tuple._2))

  // Méthode pour encoder une Throwable en tuple (StatusCode, String)
  def encode(error: Throwable): (StatusCode, String) =
    error match {
      case UnauthorizedException(msg) => (StatusCode.Unauthorized, msg) // Si l'erreur est UnauthorizedException, renvoyer le code 401
      case _ => (StatusCode.InternalServerError, error.getMessage()) // Sinon, renvoyer le code 500 avec le message de l'erreur
    }
}
