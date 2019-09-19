package io.gitplag.mossapi

import com.beust.klaxon.Klaxon
import org.jsoup.HttpStatusException
import spark.Request
import spark.Response
import spark.Spark

const val mossResultPrefix = "http://moss.stanford.edu/results/"

fun initServerConfig() {
    System.getenv("MOSSPARSER_PORT")?.toInt()?.let { port -> Spark.port(port) }

    Spark.internalServerError { _, response ->
        internalServerError(response)
    }

    Spark.notFound { _, response ->
        notFound(response)
    }
}

fun initServerApi() {

    Spark.get("/parse") { request, response ->
        headers(response)
        val mossResultLink = getUrlParam(request)
        val mode = getModeParam(request)
        if (mossResultLink != null && mode != null) {
            processRequest(response) {
                Klaxon().toJsonString(parseMossPage(mossResultLink, mode))
            }
        } else {
            badRequest(response)
        }
    }

    Spark.get("/parse/:id") { request, response ->
        headers(response)
        val mossResultLink = getIdParam(request)
        val mode = getModeParam(request)
        if (mossResultLink != null && mode != null) {
            processRequest(response) {
                Klaxon().toJsonString(parseMossPage(mossResultLink, mode))
            }
        } else {
            badRequest(response)
        }
    }

    Spark.get("/parse-graph") { request, response ->
        headers(response)
        val mossResultLink = getUrlParam(request)
        if (mossResultLink != null) {
            processRequest(response) {
                Klaxon().toJsonString(parseToGraph(mossResultLink))
            }
        } else {
            badRequest(response)
        }
    }

    Spark.get("/parse-graph/:id") { request, response ->
        headers(response)
        val mossResultLink = getIdParam(request)
        if (mossResultLink != null) {
            processRequest(response) {
                Klaxon().toJsonString(parseToGraph(mossResultLink))
            }
        } else {
            badRequest(response)
        }
    }
}

private fun processRequest(response: Response, action: () -> String) =
    try {
        action()
    } catch (e: HttpStatusException) {
        if (e.statusCode == 404) {
            mossReportNotFound(response)
        } else {
            mossIsUnavailable(response)
        }
    } catch (e: Exception) {
        internalServerError(response)
    }

private fun headers(response: Response) {
    response.header("Access-Control-Allow-Origin", "*")
}

private fun getModeParam(request: Request) = try {
    val mode = request.queryParams("mode")?.toUpperCase()
    mode?.let { Mode.valueOf(it) } ?: Mode.PAIRS
} catch (e: Exception) {
    null
}

private fun getUrlParam(request: Request) = request.queryParams("url")
private fun getIdParam(request: Request) = request.params("id")?.let { id -> "$mossResultPrefix$id" }