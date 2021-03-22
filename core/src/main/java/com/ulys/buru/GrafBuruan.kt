package com.ulys.buru

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Array
import com.ulys.Entiti
import com.ulys.PengurusPeta
import com.ulys.Peta
import com.ulys.buru.Tugas.QuestTaskPropertyType.TARGET_LOCATION
import com.ulys.buru.Tugas.QuestTaskPropertyType.TARGET_TYPE
import java.util.*
import kotlin.collections.ArrayList

class GrafBuruan {

    var questID: String = ""
    var isQuestComplete: Boolean = false
    var questTitle: String = ""
    private var questTasks = Hashtable<String, Tugas>()
    private var questTaskDependencies = Hashtable<String, ArrayList<Kaitan>>()

    fun update(pengurusPeta: PengurusPeta) {
        val allQuestTasks = getAllQuestTasks()

        for (questTask in allQuestTasks) {
            //We first want to make sure the task is available and is relevant to current location
            if (!isQuestTaskAvailable(questTask.id)) continue

            for (el in questTask.taskProperties.entries()) {
                Gdx.app.debug(tag, "${el.key} = ${el.value}")
            }

            val taskLocation = questTask.getPropertyValue(TARGET_LOCATION.name)
            if (taskLocation != pengurusPeta.peta.jenisPeta.name) continue

            when (questTask.questType) {
                Tugas.QuestType.FETCH -> {
                    val entities = Array<Entiti>()
                    val positions = pengurusPeta.getQuestItemSpawnPositions(questID, questTask.id)
                    val taskConfig = questTask.getPropertyValue(TARGET_TYPE.name)
                    if (taskConfig.isNullOrEmpty()) continue
                    for (position in positions) {
                        entities.add(Peta.initEntity(Entiti.muatKonfigurasi(taskConfig), position))
                    }
                    pengurusPeta.addMapEntities(entities)
                }
                Tugas.QuestType.RETURN -> {
                }
                Tugas.QuestType.DISCOVER -> {
                }
                else -> {
                }
            }

        }
    }

    private fun isQuestTaskAvailable(id: String): Boolean {
        if (getQuestTaskByID(id) == null) return false
        val list = questTaskDependencies[id] ?: ArrayList()
        for (dep in list) {
            val depTask = getQuestTaskByID(dep.destinationId) ?: continue
            if (dep.sourceId == id && !depTask.sudahSiap()) {
                return false
            }
        }
        return true
    }

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
        return questTitle
    }

    companion object {
        private const val tag = "GrafBuruan"
    }
}
