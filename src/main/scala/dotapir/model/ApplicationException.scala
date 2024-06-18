package dotapir.model

// Custom exception, used for all other user written exceptions
sealed abstract class ApplicationException(message: String)
    extends RuntimeException(message)

// Custom exception, describe an unauthorized request
case class UnauthorizedException(message: String)
    extends ApplicationException(message)
