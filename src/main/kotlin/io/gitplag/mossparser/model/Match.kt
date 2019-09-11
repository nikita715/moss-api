package io.gitplag.mossparser.model

data class Match(
    val name1: String,
    val percentage1: Int,
    val name2: String,
    val percentage2: Int,
    val linesCount: Int,
    val lineNumbers: List<Lines> = emptyList()
)