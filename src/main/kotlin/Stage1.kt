fun main() {
    val tasks = mutableListOf<String>()

    println("Input the tasks (enter a blank line to end):")
    while (true) {
        val task = readln().trim()
        if (task.isEmpty()) break
        tasks.add(task)
    }

    if (tasks.isEmpty()) println("No tasks have been input")
    else for (indice in tasks.indices) {

        if (indice < 9) println("${indice + 1}  ${tasks[indice]}") else println("${indice + 1} ${tasks[indice]}")
    }
}