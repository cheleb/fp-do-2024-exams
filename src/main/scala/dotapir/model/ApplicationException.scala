package dotapir.model

// Custom exception class for application exceptions
sealed abstract class ApplicationException(message: String)
    extends RuntimeException(message)

// Custom exception class for unauthorized exceptions
case class UnauthorizedException(message: String)
    extends ApplicationException(message)
