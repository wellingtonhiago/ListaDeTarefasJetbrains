fun main() {
    val tasks = mutableListOf<String>()
    val tasksPriority = mutableListOf<String>()
    val tasksData = mutableListOf<String>()
    val tasksTime = mutableListOf<String>()

    while (true) {
        println("Input an action (add, print, end):")
        val option = readln().trim().lowercase()
        when (option) {
            "add" -> addTask(   tasks = tasks,
                tasksPriority = tasksPriority,
                tasksData = tasksData,
                tasksTime = tasksTime)
            "print" -> printTasks(  tasks = tasks,
                tasksPriority = tasksPriority,
                tasksData = tasksData,
                tasksTime = tasksTime)
            "end" -> break
            else -> println("The input action is invalid")
        }
    }
    println("Tasklist exiting!")

}
