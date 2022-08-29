fun main() {
    val tasks = mutableListOf<String>()

    while (true) {
        println("Input an action (add, print, end):")
        val option = readln().trim().lowercase()
        when (option) {
            "add" -> addTask(tasks = tasks)
            "print" -> printTasks(tasks = tasks)
            "end" -> break
            else -> println("The input action is invalid")
        }
    }
    println("Tasklist exiting!")

}

fun addTask(tasks : MutableList<String>) {
    println("Input a new task (enter a blank line to end):")
    var task = ""
    while (true) {
        val line = readln().trim()
        if (line.isEmpty()) break
        task +=  "  " + line + "\n" + " "
    }
    if (task.isEmpty()) println("The task is blank") else tasks.add(task)

}

fun printTasks(tasks : MutableList<String>) {
    if (tasks.isEmpty()) println("No tasks have been input")
    else for (indice in tasks.indices) {

        if (indice < 9) println("${indice + 1}${tasks[indice]}\n")
        else println("${indice + 1}${tasks[indice]}\n".replace("  "," "))
    }
}