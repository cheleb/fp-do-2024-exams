package dotapir.model

import sttp.model.StatusCode

// Define HTTP error model
final case class HttpError(
    statusCode: StatusCode,
    message: String,
    cause: Throwable
) extends RuntimeException(message, cause)

// Define functions to encode and decode HttpError
object HttpError {
  def decode(tuple: (StatusCode, String)) =
    HttpError(tuple._1, tuple._2, new RuntimeException(tuple._2))
  def encode(error: Throwable) =
    error match
      case UnauthorizedException(msg) => (StatusCode.Unauthorized, msg)
      case _ => (StatusCode.InternalServerError, error.getMessage())
}
