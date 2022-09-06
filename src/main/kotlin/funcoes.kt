import kotlinx.datetime.*
import java.time.LocalTime

fun addTask(tasks : MutableList<String>,
            tasksPriority : MutableList<String>,
            tasksData : MutableList<String>,
            tasksTime : MutableList<String>) {


    tasksPriority.add(priority())
    tasksData.add(date().toString())
    tasksTime.add(time().toString())

    println("Input a new task (enter a blank line to end):")
    var task = ""
    while (true) {
        val line = readln().trim()
        if (line.isEmpty()) break
        task +=  "  " + line + "\n" + " "
    }
    if (task.isEmpty()) println("The task is blank") else tasks.add(task)

}

fun printTasks(tasks : MutableList<String>,
               tasksPriority : MutableList<String>,
               tasksData : MutableList<String>,
               tasksTime : MutableList<String>) {
    if (tasks.isEmpty()) println("No tasks have been input")
    else for (indice in tasks.indices) {

        if (indice < 9) println("${indice + 1}  ${tasksData[indice]} ${tasksTime[indice]} ${tasksPriority[indice]}\n ${tasks[indice]}\n")
        else println("${indice + 1} ${tasksData[indice]} ${tasksTime[indice]} ${tasksPriority[indice]}\n ${tasks[indice]}\n")
    }
}

fun priority(): String {
    val escolhasPrioridades = listOf("C", "H", "N", "L")
    while (true) {
        println("Input the task priority (C, H, N, L):")
        val priority = readln().trim().uppercase()
        if (priority in escolhasPrioridades)
            return priority
    }
}

fun date(): LocalDate? {
    var data: LocalDate? = null
    while (true) {
        println("Input the date (yyyy-mm-dd):")
        try {
            val inputDate = readLine()!!.split("-")
            return LocalDate(   inputDate[0].toInt(),
                inputDate[1].toInt(),
                inputDate[2].toInt())
        } catch (e: IllegalArgumentException) {
            println("The input date is invalid")
        }
    }

}

fun time(): LocalTime? {
    while(true) {
        println("Input the time (hh:mm):")
        try {
            val inputTime = readLine()!!.split(":")
            return LocalTime.of(    inputTime[0].toInt(),
                inputTime[1].toInt())
        }
        catch (e:Exception) { println("The input time is invalid") }
    }
}
