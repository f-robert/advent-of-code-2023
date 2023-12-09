fun main() {
    fun part1(input: List<String>): Int {
        val data = input.map { line ->
            line.split(" ").map { it.toInt() }
        }

        return data.sumOf {
            val extrapolated = history(it).reversed()
            var result = 0

            for (i in extrapolated.indices) {
                result += extrapolated[i].last()
            }

            result
        }
    }

    fun part2(input: List<String>): Int {
        val data = input.map { line ->
            line.split(" ").map { it.toInt() }
        }

        return data.sumOf {
            val extrapolated = history(it).reversed()
            var result = 0

            for (i in 1..<extrapolated.size) {
                result = extrapolated[i][0] - result
            }

            result
        }
    }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

private fun history(data: List<Int>): List<List<Int>> {
    val history = mutableListOf(data)
    var current = data

    do {
        current = buildList {
            for (i in 0..<(current.size - 1)) {
                add(current[i + 1] - current[i])
            }
        }
        history += current
    } while (!current.all { it == 0 })

    return history
}
