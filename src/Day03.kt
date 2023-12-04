fun main() {
    fun part1(input: List<String>) = input
        .findAdjacentNumberAndSymbol().sumOf { it.number.value }

    fun part2(input: List<String>) = input
        .findAdjacentNumberAndSymbol()
        .asSequence()
        .filter { it.symbol.symbol == '*' }
        .groupBy { it.symbol }
        .filter { it.value.size == 2 }
        .map { it.value
            .map { a -> a.number.value }
            .reduce(Int::times)
        }
        .sum()

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

data class Number(val value: Int, val col: Int, val row: Int)

data class Symbol(val symbol: Char, val col: Int, val row: Int)

data class Adjacent(val number: Number, val symbol: Symbol)

private fun List<String>.findAdjacentNumberAndSymbol() = buildSet {
    val numberRegex = """(\d+)""".toRegex()

    for ((y, line) in this@findAdjacentNumberAndSymbol.withIndex()) {
        val matches = numberRegex.findAll(line)

        for (match in matches) {
            val start = match.range.first
            val end = match.range.last
            val number = Number(match.groupValues[1].toInt(), start, y)

            addAll(adjacentSymbols(number, start, end, y))
        }
    }
}

private fun List<String>.adjacentSymbols(number: Number, start: Int, end: Int, row: Int) = buildSet {
    val rowRange = ((row - 1)..(row + 1)).intersect(this@adjacentSymbols.indices)

    for (y in rowRange) {
        val line = this@adjacentSymbols[y]
        val colRange = ((start - 1)..(start + 1))
            .union((end - 1)..(end + 1))
            .intersect(line.indices)
            .distinct()

        for (x in colRange) {
            if (line[x].isSymbol()) {
                val symbol = Symbol(line[x], x, y)
                add(Adjacent(number, symbol))
            }
        }
    }
}

private fun Char.isSymbol() = !isDigit() && this != '.'
