package com.ulys.tests

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonWriter
import com.ulys.buru.GrafBuruan
import com.ulys.buru.Kaitan
import com.ulys.buru.Tugas
import com.ulys.buru.Tugas.QuestTaskPropertyType.*
import java.util.*

object GrafBuruanTest {
    private var hashtable = Hashtable<String, Tugas>()
    private var grafBuruan = GrafBuruan()

    @JvmStatic
    fun main(arg: Array<String>) {

        val firstTask = Tugas()
        firstTask.id = "500"
        firstTask.taskPhrase = "Come back to me with the bones"

        val secondTask = Tugas()
        secondTask.id = "601"
        secondTask.taskPhrase = "Pickup 5 bones from the Isle of Death"

        hashtable[firstTask.id] = firstTask
        hashtable[secondTask.id] = secondTask


        grafBuruan.setTasks(hashtable)

        val firstDep = Kaitan()
        firstDep.sourceId = firstTask.id
        firstDep.destinationId = secondTask.id

        val cycleDep = Kaitan()
        cycleDep.sourceId = secondTask.id
        cycleDep.destinationId = firstTask.id

        grafBuruan.addDependency(firstDep)
        grafBuruan.addDependency(cycleDep)

//        println(grafBuruan.toString())

        hashtable.clear()
        grafBuruan.clear()

        val q1 = Tugas()
        q1.id = "1"
        q1.taskPhrase = "Come back to me with the items"

        val q2 = Tugas()
        q2.id = "2"
        q2.taskPhrase = "Collect 5 horns"

        val q3 = Tugas()
        q3.id = "3"
        q3.taskPhrase = "Collect 5 furs"

        val q4 = Tugas()
        q4.id = "4"
        q4.taskPhrase = "Find the area where the Tuskan beast feasts"

        hashtable[q1.id] = q1
        hashtable[q2.id] = q2
        hashtable[q3.id] = q3
        hashtable[q4.id] = q4

        grafBuruan.setTasks(hashtable)

        val qDep1 = Kaitan()
        qDep1.sourceId = q1.id
        qDep1.destinationId = q2.id

        val qDep2 = Kaitan()
        qDep2.sourceId = q1.id
        qDep2.destinationId = q3.id

        val qDep3 = Kaitan()
        qDep3.sourceId = q2.id
        qDep3.destinationId = q4.id

        val qDep4 = Kaitan()
        qDep4.sourceId = q3.id
        qDep4.destinationId = q4.id

        grafBuruan.addDependency(qDep1)
        grafBuruan.addDependency(qDep2)
        grafBuruan.addDependency(qDep3)
        grafBuruan.addDependency(qDep4)

        val json = Json()
        json.setUsePrototypes(false)
        json.setOutputType(JsonWriter.OutputType.minimal)
//        println(json.prettyPrint(grafBuruan))

        hashtable.clear()
        grafBuruan.clear()

        val q01 = Tugas()
        q01.id = "1"
        q01.taskPhrase = "Come back to me with the herbs"
        q01.taskProperties.put(IS_TASK_COMPLETE.name, false.toString())
        q01.taskProperties.put(TARGET_TYPE.name, "TOWN_FOLK1")
        q01.taskProperties.put(TARGET_LOCATION.name, "TOWN")
        q01.questType = Tugas.QuestType.RETURN

        val q02 = Tugas()
        q02.id = "2"
        q02.taskPhrase = "Please collect 5 herbs for my sick mother"
        q02.taskProperties.put(IS_TASK_COMPLETE.name, false.toString())
        q02.taskProperties.put(TARGET_TYPE.name, "scripts/quest_herbs.json")
        q02.taskProperties.put(TARGET_LOCATION.name, "TOWN")
        q02.taskProperties.put(TARGET_NUM.name, "5")
        q02.questType = Tugas.QuestType.FETCH

        hashtable[q01.id] = q01
        hashtable[q02.id] = q02

        grafBuruan.setTasks(hashtable)

        val qDep01 = Kaitan()
        qDep01.sourceId = q01.id
        qDep01.destinationId = q02.id

        grafBuruan.addDependency(qDep01)
        with(grafBuruan) {
            questID = "2"
            isQuestComplete = false
            questTitle = "Herbs for Friend"

        }



        println(json.prettyPrint(grafBuruan))
    }
}