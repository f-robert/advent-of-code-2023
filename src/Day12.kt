fun main() {
    fun part1(input: List<String>) = parse(input)
        .sumOf { it.bruteforce() }

    val input = readInput("Day12")
    part1(input).println()
}

private fun parse(input: List<String>) = buildList {
    for (line in input) {
        val (condition, rest) = line.split(" ")
        add(Record(condition, rest.split(",").map { it.toInt() }))
    }
}

private data class Record(val condition: String, val groups: List<Int>) {

    private val groupPattern = "(#+)".toRegex()

    fun bruteforce() = generateCombinations(condition.count { it == '?' })
        .count { combination ->
            groupPattern
                .findAll(combination)
                .map { it.groupValues[0].length }
                .toList() == groups
        }

    private fun generateCombinations(n: Int): List<String> {
        val combinations = mutableListOf<String>()

        fun generate(combination: String = "") {
            if (combination.length == n) {
                combinations += combination.fold(condition) { acc, c ->
                     acc.replaceFirst('?', c)
                }
            } else {
                generate("$combination#")
                generate("$combination.")
            }
        }

        generate()

        return combinations
    }

}
