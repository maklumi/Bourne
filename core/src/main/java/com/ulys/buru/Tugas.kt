package com.ulys.buru

import com.badlogic.gdx.utils.ObjectMap
import com.ulys.buru.Tugas.QuestTaskPropertyType.*

class Tugas {

    var id = "no id"
    var taskPhrase = "no task phrase"
    var taskProperties = ObjectMap<String, Any>()
    var questType: QuestType? = null

    enum class QuestType {
        FETCH,
        RETURN,
        DISCOVER,
    }

    enum class QuestTaskPropertyType {
        IS_TASK_COMPLETE,
        TARGET_TYPE,
        TARGET_NUM,
        TARGET_LOCATION,
    }

    fun sudahSiap(): Boolean {
        return taskProperties[IS_TASK_COMPLETE.name] == true.toString()
    }

    fun getPropertyValue(key: String): String? {
        val propertyVal = taskProperties.get(key) ?: return null
        return propertyVal.toString()
    }

    override fun toString(): String {
        return taskPhrase
    }

}