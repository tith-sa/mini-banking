package spring.minibanksystem.handleException


sealed class HandleException {

    class BadRequest(message: String) : RuntimeException(message)

    class ResourceNotFound(message: String) : RuntimeException(message)

    class Authorization(message: String) : RuntimeException(message)

    class Authentication(message: String) : RuntimeException(message)
}