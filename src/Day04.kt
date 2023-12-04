fun main() {
    fun part1(input: List<String>) = input.parse().sumOf { it.value }

    fun part2(input: List<String>): Int {
        val results = input.parse()
            .map { ScratchResult(it) }

        results.onEachIndexed { index, (scratchCard, occurrences) ->
            for (i in 0..<scratchCard.matchingNumbers) {
                results[index + i + 1].occurrences += occurrences
            }
        }

        return results.sumOf { it.occurrences }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

data class ScratchResult(val scratchCard: ScratchCard, var occurrences: Int = 1)

data class ScratchCard(private val numbers: Set<Int>, private val winners: Set<Int>) {

    val matchingNumbers = numbers.intersect(winners).size

    val value = when (matchingNumbers) {
        0 -> 0
        1 -> 1
        else -> 1 shl (matchingNumbers - 1)
    }

}

private val pattern = """(\d+)""".toRegex()

private fun List<String>.parse() = buildSet {
    for (line in this@parse) {
        val (_, content) = line.split(":")
        val (winners, numbers) = content.split("|")

        add(ScratchCard(winners.parseNumbers(), numbers.parseNumbers()))
    }
}

private fun String.parseNumbers() = pattern
    .findAll(this@parseNumbers)
    .map { it.groupValues[1] }
    .map { it.toInt() }
    .toSet()
