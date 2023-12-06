fun main() {
    fun part1(input: List<String>) = input
        .toListOfRace {
            it.split("\\s+".toRegex()).map(String::toLong)
        }
        .won()

    fun part2(input: List<String>) = input
        .toListOfRace {
            listOf(it.replace("\\s+".toRegex(), "").toLong())
        }
        .won()

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.toListOfRace(transform: (String) -> List<Long>) =
    transform(this[0].substringAfter("Time:").trim())
        .zip(transform(this[1].substringAfter("Distance:").trim())) { time, distance ->
            Race(time, distance)
        }

data class Race(val time: Long, val distance: Long)

private fun List<Race>.won() = fold(1) { acc, race -> acc * race.won() }

private fun Race.won() =  (1..<(time - 1))
    .count { it * (time - it) > distance }
