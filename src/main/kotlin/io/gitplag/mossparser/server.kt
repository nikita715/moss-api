package io.gitplag.mossparser

import com.beust.klaxon.Klaxon
import spark.Request
import spark.Response
import spark.Spark

fun initServer() {
    Spark.port(System.getenv("MOSSPARSER_PORT").toInt())

    Spark.get("/parse") { request, response ->
        execute(request, response) { mossResultLink, mode ->
            Klaxon().toJsonString(
                parseMossPage(
                    mossResultLink,
                    mode
                )
            )
        }
    }

    Spark.get("/parse-graph") { request, response ->
        execute(request, response) { mossResultLink, mode ->
            Klaxon().toJsonString(
                parseToGraph(
                    mossResultLink,
                    mode
                )
            )
        }
    }
}

private fun execute(
    request: Request,
    response: Response,
    func: (mossResultLink: String, mode: Mode) -> String
): String {
    response.header("Access-Control-Allow-Origin", "*")
    val mossResultLink = request.queryParams("url")
    val mode = try {
        request.queryParams("mode")?.toUpperCase()?.let { Mode.valueOf(it) }
    } catch (e: Exception) {
        null
    }
    return if (mossResultLink != null && mode != null) {
        func(mossResultLink, mode)
    } else {
        response.status(400)
        "Bad request"
    }
}