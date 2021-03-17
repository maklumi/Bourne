package com.ulys.dialog

import java.util.*
import kotlin.collections.ArrayList

class Graf(
    private val conversations: Hashtable<Int, Perbualan>,
    private var currentConversation: Perbualan
) {

    private var numChoices = 0
    private val associatedChoices = Hashtable<Perbualan, ArrayList<Perbualan>>(conversations.size).also {
        for (bual in conversations.values) {
            it[bual] = ArrayList()
        }
    }

    fun addChoice(source: Perbualan, target: Perbualan) {
        val arr = associatedChoices[source] ?: return
        arr.add(target)
        numChoices++
    }

    fun displayCurrentConversation(): String {
        return currentConversation.dialog
    }

    fun getCurrentChoices(): ArrayList<Perbualan> {
        return associatedChoices[currentConversation] ?: ArrayList()
    }

    fun getConversationByID(id: Int): Perbualan? {
        return conversations[id]
    }

    fun setCurrentConversation(conversation: Perbualan) {
        //Can we reach the new conversation from the current one?
        if (isReachable(currentConversation, conversation)) {
            currentConversation = conversation
        } else {
            println("New conversation node is not reachable from current node!")
        }
    }

    private fun isReachable(source: Perbualan, sink: Perbualan): Boolean {
        if (!isValid(source) || !isValid(sink)) return false

        //First get edges/choices from the source
        val list = associatedChoices[source] as ArrayList
        for (conversation in list) {
            if (conversation.id == sink.id) {
                return true
            }
        }
        return false
    }

    private fun isValid(conversation: Perbualan): Boolean {
        return conversations[conversation.id] != null
    }

    override fun toString(): String {
        val string = StringBuilder()
        string.append("Jumlah ayat: " + conversations.size + ", pilihan:" + numChoices)
        string.append(System.getProperty("line.separator"))
        string.append("[id perbualan]: ids pilihan")
        string.append(System.getProperty("line.separator"))

        val keys = associatedChoices.keys
        for (conversation in keys) {
            string.append(String.format("[%d]: ", conversation.id))

            for (choices in associatedChoices[conversation]!!) {
                string.append(String.format("%d ", choices.id))
            }

            string.append(System.getProperty("line.separator"))
        }

        return string.toString()
    }
}