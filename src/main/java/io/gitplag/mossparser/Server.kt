package io.gitplag.mossparser

import com.beust.klaxon.Klaxon
import org.apache.log4j.BasicConfigurator
import spark.Request
import spark.Response
import spark.Spark

fun main() {
    BasicConfigurator.configure()
    Spark.port(8082)

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

fun execute(request: Request, response: Response, func: (mossResultLink: String, mode: Mode) -> String): String {
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