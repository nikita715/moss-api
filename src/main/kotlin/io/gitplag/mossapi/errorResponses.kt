package io.gitplag.mossapi

import com.beust.klaxon.Klaxon
import org.eclipse.jetty.http.HttpStatus
import spark.Response

private val klaxon = Klaxon()

internal fun internalServerError(response: Response, message: String = "Internal server error"): String {
    response.status(HttpStatus.INTERNAL_SERVER_ERROR_500)
    return klaxon.toJsonString(
        ErrorDto(
            status = HttpStatus.INTERNAL_SERVER_ERROR_500,
            message = message
        )
    )
}

internal fun badRequest(response: Response): String {
    response.status(HttpStatus.BAD_REQUEST_400)
    return klaxon.toJsonString(
        ErrorDto(
            status = HttpStatus.BAD_REQUEST_400,
            message = "Bad request"
        )
    )
}

internal fun mossReportNotFound(response: Response) = notFound(response, "The requested Moss report was not found")

internal fun notFound(response: Response, message: String = "Page not found"): String {
    response.status(HttpStatus.NOT_FOUND_404)
    return klaxon.toJsonString(
        ErrorDto(
            status = HttpStatus.NOT_FOUND_404,
            message = message
        )
    )
}

internal fun mossIsUnavailable(response: Response): String {
    response.status(HttpStatus.SERVICE_UNAVAILABLE_503)
    return klaxon.toJsonString(
        ErrorDto(
            status = HttpStatus.SERVICE_UNAVAILABLE_503,
            message = "Moss server is unavailable"
        )
    )
}

class ErrorDto(
    val status: Int,
    val message: String
)