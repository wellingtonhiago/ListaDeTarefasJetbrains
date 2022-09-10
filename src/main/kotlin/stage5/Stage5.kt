import kotlinx.datetime.*

const val ROW_SEPARATOR = "+----+------------+-------+---+---+--------------------------------------------+"
const val COLOR_BLACK = "\u001B[0m"
const val COLOR_RED = "\u001B[101m $COLOR_BLACK"
const val COLOR_YELLOW = "\u001B[103m $COLOR_BLACK"
const val COLOR_GREEN = "\u001B[102m $COLOR_BLACK"
const val COLOR_BLUE = "\u001B[104m $COLOR_BLACK"

fun main() {
    Menu().openMenu()
}

class Menu {
    private val tasks: MutableList<Task> = mutableListOf()

    fun openMenu() {
        when (println("Input an action (add, print, edit, delete, end):").run { readln().lowercase() }) {
            "add" -> Task(tasks.size + 1).let { if (it.task.isNotEmpty()) tasks.add(it) }
            "print" -> printTasks()
            "edit" -> doIfTasksIsNotEmpty(::editTask)
            "delete" -> doIfTasksIsNotEmpty(::deleteTask)
            "end" -> println("Tasklist exiting!").run { return }
            else -> println("The input action is invalid")
        }
        openMenu()
    }

    private fun printTasks() {
        val tableHeader = """
            $ROW_SEPARATOR
            | N  |    Date    | Time  | P | D |                   Task                     |
            $ROW_SEPARATOR
        """.trimIndent()

        println(
            if (tasks.isNotEmpty()) "$tableHeader\n${tasks.joinToString("\n")}"
            else "No tasks have been input"
        )
    }

    private fun doIfTasksIsNotEmpty(func: (Task) -> Unit) {
        printTasks()
        if (tasks.isNotEmpty()) func(getTask())
    }

    private fun getTask(): Task {
        println("Input the task number (1-${tasks.size}):")
        return try {
            tasks[readln().toInt() - 1]
        } catch (e: Exception) {
            println("Invalid task number").run { getTask() }
        }
    }

    private fun deleteTask(task: Task) {
        tasks.removeAt(task.id - 1).run { tasks.mapIndexed { idx, task -> task.id = idx + 1 } }
        println("The task is deleted")
    }

    private fun editTask(task: Task) {
        task.edit()
        println("The task is changed")
    }
}

data class Task(var id: Int) {
    private var priority = setPriority()
    private var date = setDate()
    private var time = setTime()
    var task = setTask()

    private fun setPriority(): String {
        println("Input the task priority (C, H, N, L):")
        return readln().uppercase().let { if ("^[CHNL]$".toRegex().matches(it)) it else setPriority() }
    }

    private fun setDate(): String {
        println("Input the date (yyyy-mm-dd):")
        return try {
            return readln().split("-").map { it.toInt() }.let { LocalDate(it[0], it[1], it[2]).toString() }
        } catch (e: Exception) {
            println("The input date is invalid").run { setDate() }
        }
    }

    private fun setTime(): String {
        println("Input the time (hh:mm):")
        return readln()
            .replace(Regex("^[0-9]:")) { "0${it.value}" }
            .replace(Regex(":[0-9]$")) { ":0${it.value[1]}" }
            .let {
                if (it.matches(Regex("^([0-1][0-9]|2[0-3]):([0-5][0-9])$"))) it
                else println("The input time is invalid").run { setTime() }
            }
    }

    private fun setTask() = mutableListOf<String>().also { it.processTask() }

    private fun MutableList<String>.processTask() {
        if (isEmpty()) println("Input a new task (enter a blank line to end):")
        readln().trim().let { if (it.isNotEmpty()) add(it).also { processTask() } }
        if (isEmpty()) println("The task is blank")
    }

    fun edit() {
        editOptions()
    }

    private fun editOptions() {
        println("Input a field to edit (priority, date, time, task):")
        when (readln().lowercase()) {
            "priority" -> priority = setPriority()
            "date" -> date = setDate()
            "time" -> time = setTime()
            "task" -> task = setTask()
            else -> println("Invalid field").run { editOptions() }
        }
    }

    private fun getDueTagColor(): String {
        val numberOfDays = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+0")).date
            .daysUntil(date.toLocalDate())
        return when {
            numberOfDays > 0 -> COLOR_GREEN
            numberOfDays < 0 -> COLOR_RED
            else -> COLOR_YELLOW
        }
    }

    private fun getPriorityColor() =
        when (priority) {
            "C" -> COLOR_RED
            "H" -> COLOR_YELLOW
            "N" -> COLOR_GREEN
            "L" -> COLOR_BLUE
            else -> COLOR_BLACK
        }

    private fun getTaskString(): String {
        val separator = "|\n|    |            |       |   |   |"
        return task.flatMap { it.chunked(44) }.joinToString(separator) { it.padEnd(44) }
    }

    override fun toString(): String {
        return "| $id ${if (id < 10) " " else ""}| $date | $time | ${getPriorityColor()} | ${getDueTagColor()} " +
                "|${getTaskString()}|\n$ROW_SEPARATOR"
    }
}