package com.ulys.dialog

import java.util.*

class Graf(
    private val conversations: Hashtable<Int, Perbualan>,
    root: Int
) {

    private var numChoices = 0
    private val associatedChoices = Hashtable<Int, ArrayList<Int>>(conversations.size).also {
        for (bual in conversations.values) {
            it[bual.id] = ArrayList()
        }
    }
    private var currentConversation = getConversationByID(root)

    fun addChoice(sourceID: Int, targetID: Int) {
        val arr = associatedChoices[sourceID] ?: return
        arr.add(targetID)
        numChoices++
    }

    fun displayCurrentConversation(): String {
        return currentConversation?.dialog ?: ":)"
    }

    fun getCurrentChoices(): ArrayList<Int> {
        return associatedChoices[currentConversation?.id] ?: ArrayList()
    }

    fun getConversationByID(id: Int): Perbualan? {
        return conversations[id]
    }

    fun setCurrentConversation(id: Int) {
        val conversation = getConversationByID(id)
        //Can we reach the new conversation from the current one?
        if (currentConversation != null && isReachable(currentConversation!!.id, id)) {
            currentConversation = conversation
        } else {
            println("New conversation node is not reachable from current node!")
        }
    }

    private fun isReachable(sourceID: Int, sinkID: Int): Boolean {
        if (!isValid(sourceID) || !isValid(sinkID)) return false

        //First get edges/choices from the source
        val list = associatedChoices[sourceID] as ArrayList
        return list.contains(sinkID)
    }

    private fun isValid(conversationID: Int): Boolean {
        return conversations[conversationID] != null
    }

    override fun toString(): String {
        val string = StringBuilder()
        string.append("Jumlah ayat: " + conversations.size + ", pilihan:" + numChoices)
        string.append(System.getProperty("line.separator"))
        string.append("[id perbualan]: ids pilihan")
        string.append(System.getProperty("line.separator"))

        val keys = associatedChoices.keys
        for (conversationID in keys) {
            string.append(String.format("[%d]: ", conversationID))

            for (choiceID in associatedChoices[conversationID]!!) {
                string.append(String.format("%d ", choiceID))
            }

            string.append(System.getProperty("line.separator"))
        }

        return string.toString()
    }
}