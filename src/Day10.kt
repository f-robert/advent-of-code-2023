import Direction.*

fun main() {
    fun part1(input: List<String>) = field(input).walk(SOUTH)

    fun part2(input: List<String>): Int = TODO()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 4)

    val input = readInput("Day10")
    part1(input).println()
//    part2(input).println()
}

private fun field(input: List<String>): Field = buildList<Tile> {
    for ((y, line) in input.withIndex()) {
        for ((x, char) in line.withIndex()) {
            add(Tile(char, x, y))
        }
    }
}.let { Field(it, input[0].length) }

private class Field(val tiles: List<Tile>, val cols: Int) {

    private val startingPosition = tiles
        .first { it.char == 'S' }
        .position

    fun walk(initialDirection: Direction) =
        walk(startingPosition, initialDirection, mutableSetOf())

    private tailrec fun walk(
        position: Position,
        direction: Direction,
        seen: MutableSet<Position>
    ): Int {
        seen += position
        val newPosition = position.next(direction)
        if (newPosition == startingPosition) {
            return seen.size / 2
        } else {
            val tile = tile(newPosition)
            val newDirection = tile
                .connections
                .first { it != direction.opposite }
            return walk(newPosition, newDirection, seen)
        }
    }

    private fun tile(position: Position) = tiles[position.y * cols + position.x]

    override fun toString() = tiles.chunked(cols) { row ->
        row.map { it.char }.joinToString(" ")
    }.joinToString("\n")

}

private data class Position(val x: Int, val y: Int) {

    fun next(direction: Direction) = when (direction) {
        NORTH -> copy(y = y - 1)
        EAST -> copy(x = x + 1)
        SOUTH -> copy(y = y + 1)
        WEST -> copy(x = x - 1)
    }

}

private enum class Direction {
    NORTH, EAST, SOUTH, WEST;

    val opposite get() = entries[(ordinal + 2).rem(entries.size)]

}

private data class Tile(val char: Char, val position: Position, val connections: Set<Direction>) {

    companion object {
        operator fun invoke(char: Char, x: Int, y: Int): Tile {
            val connections = when (char) {
                '|' -> setOf(NORTH, SOUTH)
                '-' -> setOf(EAST, WEST)
                'L' -> setOf(NORTH, EAST)
                'J' -> setOf(NORTH, WEST)
                '7' -> setOf(SOUTH, WEST)
                'F' -> setOf(SOUTH, EAST)
                '.' -> emptySet()
                'S' -> emptySet()
                else -> error("No tile $char found")
            }

            return Tile(char, Position(x, y), connections)
        }
    }

}
