import kotlin.math.max

fun main() {
    fun part1(input: List<String>) = input.sumOf {
        val result = it.toGameResult()

        if (result.possible) result.id else 0
    }

    fun part2(input: List<String>) = input.sumOf {
        it.toGameResult().power
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

data class GameResult(val id: Int, val red: Int, val green: Int, val blue: Int)

private val idPattern = """^Game (\d+)""".toRegex()
private val colorPattern = """(\d+)\s+(\w+)""".toRegex()

private fun String.toGameResult(): GameResult {
    val (idStr, resultStr) = split(": ")
    val id = idPattern.find(idStr)
        ?.groupValues?.get(1)
        ?.toInt()
        ?: error("No id found")

    var red = 0
    var green = 0
    var blue = 0

    for (match in colorPattern.findAll(resultStr)) {
        val quantity = match.groupValues[1].toInt()

        when (val color = match.groupValues[2]) {
            "red" -> red = max(red, quantity)
            "green" -> green = max(green, quantity)
            "blue" -> blue = max(blue, quantity)
            else -> error("Unknown color $color")
        }
    }

    return GameResult(id, red, green, blue)
}

private val GameResult.possible get() = red <= 12 && green <= 13 && blue <= 14

private val GameResult.power get() = red * green * blue
