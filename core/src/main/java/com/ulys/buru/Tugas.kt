package com.ulys.buru

import com.badlogic.gdx.utils.ObjectMap

class Tugas {

    var id = "no id"
    var taskPhrase = "no task phrase"
    var taskProperties = ObjectMap<String, Any>()

    fun sudahSiap(): Boolean {
        return taskProperties[IS_TASK_COMPLETE] == true
    }

    override fun toString(): String {
        return taskPhrase
    }

    companion object {
        private const val IS_TASK_COMPLETE = "IS_TASK_COMPLETE"
    }
}