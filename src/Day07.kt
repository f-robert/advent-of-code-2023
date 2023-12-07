fun main() {
    fun part1(input: List<String>) = listOfHands(
            input, "23456789TJQKA")
        .sorted()
        .mapIndexed { i, hand ->  (i + 1) * hand.bid }
        .sum()

    fun part2(input: List<String>) = listOfHands(
        input, "J23456789TQKA", 'J')
        .sorted()
        .mapIndexed { i, hand ->  (i + 1) * hand.bid }
        .sum()

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

enum class Type {
    HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, FULL_HOUSE, FOUR_OF_A_KIND, FIVE_OF_A_KIND
}

class Hand(
    val bid: Int,
    private val type: Type,
    private val cardValues: List<Int>
) : Comparable<Hand> {

    override fun compareTo(other: Hand) =
        when (val typeComparison = type.compareTo(other.type)) {
            0 -> cardValues.zip(other.cardValues)
                .dropWhile { it.first == it.second }
                .firstOrNull()
                ?.let { it.first.compareTo(it.second) }
                ?: 0
            else -> typeComparison
        }

}

private fun listOfHands(
    input: List<String>,
    cardOrder: String,
    wildcard: Char? = null
): List<Hand> {
    return input.map { line ->
        val (text, bid) = line.split(" ")
        val cardValues = text.map { cardOrder.indexOf(it) }

        fun findType(text: String): Type {
            val cardOccurrences = text
                .map { it }
                .groupingBy { it }
                .eachCount()
                .map { (_, count) -> count }
                .sortedByDescending { it }


            return when {
                cardOccurrences[0] == 5 -> Type.FIVE_OF_A_KIND
                cardOccurrences[0] == 4 -> Type.FOUR_OF_A_KIND
                cardOccurrences[0] == 3 && cardOccurrences[1] == 2 -> Type.FULL_HOUSE
                cardOccurrences[0] == 3 -> Type.THREE_OF_A_KIND
                cardOccurrences[0] == 2 && cardOccurrences[1] == 2 -> Type.TWO_PAIR
                cardOccurrences[0] == 2 -> Type.ONE_PAIR
                else -> Type.HIGH_CARD
            }
        }

        val type = wildcard?.let { w ->
            cardOrder
                .map { it }
                .maxOf { c -> findType(text.replace(w, c)) }
        } ?: findType(text)

        Hand(bid.toInt(), type, cardValues)
    }
}
