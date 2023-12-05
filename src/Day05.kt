import java.util.stream.LongStream

fun main() {
    fun part1(input: List<String>): Long {
        val seeds = input[0].substringAfter("seeds:")
            .trim()
            .split(" ")
            .map { it.toLong() }
        val map = buildMap(input.drop(2))

        return seeds.minOf { seed ->
            find(map, "seed", seed)
        }
    }

    fun part2(input: List<String>): Long {
        val map = buildMap(input.drop(2))

        return input[0].substringAfter("seeds:")
            .trim()
            .split(" ")
            .map { it.toLong() }
            .chunked(2) { (start, length) ->
                LongStream.range(start, start + length)
                    .parallel()
                    .map { find(map, "seed", it) }
                    .min()
                    .asLong
            }.minOf { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

data class Range(val name: String, val destination: Long, val source: Long, val length: Long) {

    operator fun contains(value: Long) = source <= value && value < source + length

    operator fun get(value: Long) = destination + (value - source)

}

private val mapPattern = """(\w+)-to-(\w+) map:""".toRegex()

private tailrec fun find(map: Map<String, List<Range>>, sourceName: String, value: Long): Long {
    if (map[sourceName] == null) {
        return value
    } else {
        val (name, result) = map[sourceName]!!
            .find { value in it }
            ?.let { it.name to it[value] }
            ?: (map[sourceName]!![0].name to value)
        return find(map, name, result)
    }
}

private fun buildMap(input: List<String>): Map<String, List<Range>> {
    val map = mutableMapOf<String, List<Range>>()
    var sourceName = ""
    var destName = ""
    var ranges = mutableListOf<Range>()

    for (line in input) {
        when {
            mapPattern.matches(line) -> {
                val (sn, dn) = mapPattern.matchEntire(line)!!.destructured
                sourceName = sn
                destName = dn
            }
            line.isBlank() -> {
                map[sourceName] = ranges
                ranges = mutableListOf()
            }
            else -> {
                val (dest, source, length) = line.split(" ")
                    .map { it.toLong() }
                ranges += Range(destName, dest, source, length)
            }

        }
    }

    if (ranges.isNotEmpty()) {
        map[sourceName] = ranges.sortedBy { it.source }
    }

    return map
}
