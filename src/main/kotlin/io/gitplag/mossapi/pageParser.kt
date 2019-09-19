package io.gitplag.mossapi

import io.gitplag.mossapi.model.Lines
import io.gitplag.mossapi.model.Match
import io.gitplag.mossapi.model.Result
import io.gitplag.mossapi.model.graph.GraphData
import io.gitplag.mossapi.model.graph.Link
import io.gitplag.mossapi.model.graph.Node
import org.jsoup.Jsoup

fun parseToGraph(mossResultLink: String): GraphData {
    val result = parseMossPage(mossResultLink, Mode.PAIRS)
    val names = mutableSetOf<String>()
    val links = mutableListOf<Link>()
    result.matches.forEach { match ->
        names.add(match.name1)
        names.add(match.name2)
        val maxPercentage = if (match.percentage1 > match.percentage2) match.percentage1 else match.percentage2
        links.add(Link(match.name1, match.name2, maxPercentage))
    }
    val nodes = names.map { Node(it) }
    return GraphData(nodes, links)
}

fun parseMossPage(mossResultLink: String, mode: Mode): Result =
    Result(Jsoup.connect(mossResultLink).get()
        .body()
        .getElementsByTag("table")
        .select("tr")
        .drop(1)
        .map { tr -> tr.select("td") }
        .mapNotNull { tds ->
            val firstATag = tds[0].selectFirst("a")
            val secondATag = tds[1].selectFirst("a")

            val students = firstATag.text().split(" ").first() to secondATag.text().split(" ").first()

            val lines = tds[2].text().toInt()

            val percentage1 = firstATag.text().split(" ")
                .last()
                .removeSurrounding("(", "%)")
                .toInt()
            val percentage2 = secondATag.text().split(" ")
                .last()
                .removeSurrounding("(", "%)")
                .toInt()

            val matchedLines = if (mode == Mode.FULL) {
                val rows = Jsoup.connect(firstATag.attr("href").replace(".html", "-top.html"))
                    .get().getElementsByTag("tr")
                val matchedLines = mutableListOf<Lines>()
                for (row in rows.subList(1, rows.size)) {
                    val cells = row.getElementsByTag("td")
                    val firstMatch = cells[0].selectFirst("a").text().split("-")
                    val secondMatch = cells[2].selectFirst("a").text().split("-")
                    matchedLines += Lines(
                        from1 = firstMatch[0].toInt(),
                        to1 = firstMatch[1].toInt(),
                        from2 = secondMatch[0].toInt(),
                        to2 = secondMatch[1].toInt()
                    )
                }
                matchedLines
            } else emptyList<Lines>()

            Match(
                name1 = students.first,
                name2 = students.second,
                percentage1 = percentage1,
                percentage2 = percentage2,
                linesCount = lines,
                lineNumbers = matchedLines
            )
        }
    )
