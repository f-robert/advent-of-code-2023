fun main() {
    fun part1(input: List<String>): Int {
        return walk1(input)
    }

    fun part2(input: List<String>): Long {
        return walk2(input)
    }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

data class Node(val left: String, val right: String)

private fun parse(input: List<String>): Pair<String, Map<String, Node>> {
    val pattern = """([A-Z\d]{3}) = \(([A-Z\d]{3}), ([A-Z\d]{3})\)""".toRegex()

    val instructions = input[0]
    val nodes = input.drop(2)
        .map { pattern.matchEntire(it) }
        .associate { it!!.groupValues[1] to Node(it.groupValues[2], it.groupValues[3]) }

    return instructions to nodes
}

private fun walk1(input: List<String>): Int {
    val (instructions, nodes) = parse(input)
    println(nodes)
    var current = "AAA"
    var steps = 0

    while (current != "ZZZ") {
        val direction = instructions[steps.mod(instructions.length)]
        val node = nodes[current]!!

        current = if (direction == 'L') {
            node.left
        } else {
            node.right
        }

        steps++
    }

    return steps
}

private fun walk2(input: List<String>): Long {
    val (instructions, nodes) = parse(input)

    val steps = nodes.keys.filter { it.endsWith('A') }
        .map {
            var current = it
            var steps = 0L

            while (!current.endsWith('Z')) {
                val direction = instructions[steps.mod(instructions.length)]
                val node = nodes[current]!!

                current = if (direction == 'L') {
                    node.left
                } else {
                    node.right
                }

                steps++
            }
            steps
        }

    tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

    fun lcm(a: Long, b: Long) = a * b / gcd(a, b)

    return steps.reduce { acc, l -> lcm(acc, l) }

}
