package com.ulys.buru

import java.util.*

class GrafBuruan {

    private var questTitle: String = ""
    private var questTasks = Hashtable<String, Tugas>()
    private var questTaskDependencies = Hashtable<String, ArrayList<Kaitan>>()

    fun setTasks(questTasks: Hashtable<String, Tugas>) {
        if (questTasks.size < 0) {
            throw IllegalArgumentException("Can't have a negative amount of conversations")
        }

        this.questTasks = questTasks
        this.questTaskDependencies = Hashtable<String, ArrayList<Kaitan>>(questTasks.size)

        for (questTask in questTasks.values) {
            questTaskDependencies[questTask.id] = ArrayList<Kaitan>()
        }
    }

    fun addDependency(dependency: Kaitan) {
        val list = questTaskDependencies[dependency.sourceId] ?: return

        //Will not add if creates cycles
        if (doesCycleExist(dependency)) {
            println("Cycle exists! Not adding")
            return
        }

        list.add(dependency)
    }

    private fun doesCycleExist(dep: Kaitan): Boolean {
        val keys = questTasks.keys
        for (id in keys) {
            if (doesQuestTaskHaveDependencies(id) && dep.destinationId == id) {
                println("ID: " + id + " destID: " + dep.destinationId)
                return true
            }
        }
        return false
    }

    private fun doesQuestTaskHaveDependencies(id: String): Boolean {
        if (getQuestTaskByID(id) == null) return false
        val list = questTaskDependencies[id] ?: return false
        return list.isNotEmpty()
    }

    private fun getQuestTaskByID(id: String): Tugas? {
        if (!isValid(id)) {
            println("Id $id is not valid!")
            return null
        }
        return questTasks[id]
    }

    private fun isValid(taskID: String): Boolean {
        return questTasks[taskID] != null
    }

    fun clear() {
        questTasks.clear()
        questTaskDependencies.clear()
    }

    fun getAllQuestTasks(): ArrayList<Tugas> {
        return Collections.list(questTasks.elements())
    }

    override fun toString(): String {
        val sb = StringBuilder()
        var numberTotalChoices = 0

        val keys = questTaskDependencies.keys
        for (id in keys) {
            sb.append(String.format("[%s]: ", id))
            sb.append(String.format("[%s]: ", getQuestTaskByID(id)!!.taskPhrase))

            for (dependency in questTaskDependencies[id]!!) {
                numberTotalChoices++
                sb.append(String.format("%s ", dependency.destinationId))
            }

            sb.append(System.getProperty("line.separator"))
        }

        sb.append(String.format("Number quest tasks: %d", questTasks.size))
        sb.append(String.format(", Number of dependencies: %d", numberTotalChoices))
        sb.append(System.getProperty("line.separator"))

        return sb.toString()
    }
}
