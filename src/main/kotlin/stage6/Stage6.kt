package stage6

import com.squareup.moshi.*
import kotlinx.datetime.*
import java.io.*

fun main() {
    Tasklist().runApp()
}

class Tasklist {

    fun runApp() {
        with(mutableListOf<Task>()) {
            val jsonFile = File("tasklist.json").also { it.createNewFile() }
            val type = Types.newParameterizedType(List::class.java, Task::class.java)
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val taskListAdapter = moshi.adapter<List<Task?>>(type)
            if (jsonFile.readText().isNotEmpty()) taskListAdapter.fromJson(jsonFile.readText())?.map { add(it!!) }
            actions()
            jsonFile.writeText(taskListAdapter.toJson(this))
        }
    }

    fun MutableList<Task>.actions() {
        when ("Input an action (add, print, edit, delete, end):".reply().lowercase()) {
            "add" -> addTask()
            "print" -> printTasks()
            "edit" -> printTasks().also { editTask() }
            "delete" -> printTasks().also { deleteTask() }
            "end" -> println("Tasklist exiting!").run { return }
            else -> println("The input action is invalid")
        }
        actions()
    }

    fun MutableList<Task>.addTask() {
        with(Task(size + 1)) {
            setPriority()
            setDate()
            setTime()
            setToDoList()
            if (toDoList.isNotEmpty()) add(this) }
    }

    fun MutableList<Task>.printTasks() = println(if (isNotEmpty()) joinToString("") else "No tasks have been input")

    fun MutableList<Task>.editTask() {
        if (isNotEmpty()) {
            getTask().editField().run { println("The task is changed") }
        }
    }

    fun MutableList<Task>.deleteTask() {
        if (isNotEmpty()) {
            remove(getTask()).also { mapIndexed { index, task -> task.id = index + 1 } }
            println("The task is deleted")
        }
    }

    fun MutableList<Task>.getTask(): Task {
        return try {
            this["Input the task number (1-${size}):".reply().toInt() - 1]
        } catch (e: Exception) {
            println("Invalid task number").run { getTask() }
        }
    }
}


data class Task(var id: Int) {
    var date: String = ""
    var priority: Int = 0
    var time: String = ""
    var toDoList: MutableList<String> = mutableListOf()

    fun setPriority() {
        with(mapOf("C" to 1, "H" to 3, "N" to 2, "L" to 4)) {
            println("Input the task priority (${keys.joinToString(", ")}):")
            readln().uppercase().let { if (it in keys) priority = getValue(it) else setPriority() }
        }
    }

    fun setDate() {
        try {
            with("Input the date (yyyy-mm-dd):".reply()) {
                split("-").map { it.toInt() }.let { date = LocalDate(it[0], it[1], it[2]).toString() }
            }
        } catch (e: Exception) {
            println("The input date is invalid").run { setDate() }
        }
    }

    fun setTime() {
        try {
            with("Input the time (hh:mm):".reply().split(":").map { it.toInt() }) {
                date.toLocalDate().atTime(first(), last()).let { time = it.toString().substringAfter("T") }
            }
        } catch (e: Exception) {
            println("The input time is invalid").run { setTime() }
        }
    }

    fun setToDoList() = toDoList.addToDo(true)

    private fun MutableList<String>.addToDo(isNewList: Boolean) {
        if (isNewList) println("Input a new task (enter a blank line to end):").also { clear() }
        readln().trimIndent().let { if (it.isNotEmpty()) add(it).run { addToDo(false) } }
        ifEmpty { println("The task is blank") }
    }

    fun editField() {
        when("Input a field to edit (priority, date, time, task):".reply()) {
            "priority" -> setPriority()
            "date" -> setDate()
            "time" -> setTime()
            "task" -> setToDoList()
            else -> println("Invalid field").run { editField() }
        }
    }
    private fun dueTag(): Int {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+0")).date
        return with (currentDate.daysUntil(date.toLocalDate())) {
            if (this > 0) 2 else if (this < 0) 1 else 3
        }
    }

    override fun toString(): String {
        val color = { code: Int -> "\u001B[10${code}m \u001B[0m" }
        val row = "| %-3s| %-11s| %-6s| %s | %s |%-44s|\n"
        val dividingLine = row.format(" ", " ", " ", " ", " ", " ").replace(" ", "-").replace("|", "+")
        val header = row.format("N", "   Date", "Time", "P", "D", " ".repeat(19) + "Task")

        return (if (id == 1) "$dividingLine$header$dividingLine" else "") +
                with(toDoList.flatMap { it.chunked(44) }) {
                    row.format("$id", date, time, color(priority), color(dueTag()), first()) +
                            drop(1).joinToString("") { row.format(" ", " ", " ", " ", " ", it) }
                } + dividingLine
    }
}

fun String.reply() = println(this).run { readln() }