import kotlin.math.abs

fun main() {
    fun part1(input: List<String>) = calc(input, 2)

    fun part2(input: List<String>) = calc(input, 1_000_000)

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.row(i: Int) = this[i]

private fun List<String>.col(i: Int) = map { it[i] }
    .joinToString()

private fun calc(input: List<String>, factor: Int): Long {
    val expandedRows = input.indices
        .map { if ('#' !in input.row(it)) factor else 1 }

    val expandedCols = (0..<input[0].length)
        .map { if ('#' !in input.col(it)) factor else 1 }

    val galaxies = buildList {
        for (y in input.indices) {
            val yOffset = expandedRows.take(y).sum()

            for (x in input[0].indices) {
                if (input[y][x] == '#') {
                    val xOffset = expandedCols.take(x).sum()
                    add(Galaxy(xOffset, yOffset))
                }
            }
        }
    }

    return (galaxies.flatMap { first ->
        galaxies.map { second ->
            abs(first.x - second.x).toLong() + abs(first.y - second.y).toLong()
        }
    }.sum() / 2L)
}

private data class Galaxy(val x: Int, val y: Int)
