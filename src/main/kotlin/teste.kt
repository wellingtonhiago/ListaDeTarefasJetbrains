import kotlinx.datetime.*

fun main() {
    val teste = String.format("%8s %8s", "The percentage of", "this amount is 30%.")
    println(teste)
    val s1 = String.format("%8s %8s", "Hello", "World")
    println(s1)
    val int1 = 1234.000
    println(String.format("% -(12f", int1))

    val a = 12
    println(a % 2 != 1)
    println(a.mod(2) != 1)

    println(a.mod(2) == 0)
    println(a % 2 == 0)

    val t ="2022-12-31T10:10:00".toLocalDate()

    val b = Instant.parse("2022-01-01T19:00:00Z").toLocalDateTime(TimeZone.currentSystemDefault())

    val ok = "2022-12-31T10:10:00".toLocalDateTime()









}

fun solution(setSource: Set<String>, listSource: MutableList<String>): MutableSet<String> {
    val mutavel = setSource.toMutableSet()
    mutavel.addAll(listSource.toMutableSet())
    return mutavel
}

class Event(val id: Int, val x: Int, val y: Int, val isLongClick: Boolean) {
    operator fun component1() : Int = y
    operator fun component2() = x
    operator fun component3() : Boolean = isLongClick
    operator fun component4() : Int = id
}


