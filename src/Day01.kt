fun main() {
    fun part1(input: List<String>) = input.sumOf {
        digitsToInt(it.findDigit(), it.findDigit(true))
    }

    fun part2(input: List<String>) = input.sumOf {
        digitsToInt(it.findDigitOrWord(), it.findDigitOrWord(true))
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

private fun digitsToInt(first: Int, second: Int) = 10 * first + second

private fun String.findDigit(last: Boolean = false): Int {
    val digit = if (last) {
        last { it.isDigit() }
    } else {
        first { it.isDigit() }
    }

    return digit.digitToInt()
}

private val digitsOrWords = listOf(
    "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
) + List(10) { "$it" }

private fun String.findDigitOrWord(last: Boolean = false): Int {
    val (_, value) = if (last) {
        findLastAnyOf(digitsOrWords)
    } else {
        findAnyOf(digitsOrWords)
    } ?: error("No digit found")

    return digitsOrWords.indexOf(value).mod(10)
}
