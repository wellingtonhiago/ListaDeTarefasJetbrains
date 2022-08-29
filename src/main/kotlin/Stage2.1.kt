fun main() = mutableListOf<Task>().runTasklistApp()

fun MutableList<Task>.runTasklistApp() {
    when (println("Input an action (add, print, end):").run { readln() }) {
        "add" -> Task(size + 1).let { if (it.name.isNotEmpty()) add(it) }
        "print" -> println(if (isNotEmpty()) joinToString("\n") else "No tasks have been input")
        "end" -> println("Tasklist exiting!").run { return }
        else -> println("The input action is invalid")
    }
    runTasklistApp()
}

data class Task(val id: Int, var name: String = "", var toDoList: MutableList<String> = mutableListOf()) {
    init {
        println("Input a new task (enter a blank line to end):")
        readln().trimIndent().let { if (it.isEmpty()) println("The task is blank") else name = it.also { addToDo() } }
    }
    private fun addToDo() {
        readln().trimIndent().let { if (it.isNotEmpty()) toDoList.add(it).also { addToDo() } }
    }
    override fun toString(): String {
        with (if (id < 10) "$id  " else "$id ") {
            return "$this$name\n   ${toDoList.joinToString("\n   ")}\n"
        }
    }
}