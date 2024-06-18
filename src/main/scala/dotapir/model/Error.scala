package dotapir.model

import sttp.model.StatusCode

// Error class which encapsulates the HTTP status code, message and the cause
final case class HttpError(
    statusCode: StatusCode,
    message: String,
    cause: Throwable
) extends RuntimeException(message, cause)

// Companion object for `HttpError` which provides methods to encode and decode the error
object HttpError {
  def decode(tuple: (StatusCode, String)) =
    HttpError(tuple._1, tuple._2, new RuntimeException(tuple._2))
  def encode(error: Throwable) =
    error match
      case UnauthorizedException(msg) => (StatusCode.Unauthorized, msg)
      case _ => (StatusCode.InternalServerError, error.getMessage())
}
