package com.ulys.dialog

import java.util.*

class Graf(
    private val conversations: Hashtable<Int, Perbualan>,
    root: Int
) {

    private var numChoices = 0
    private val associatedChoices = Hashtable<Int, ArrayList<Pilihan>>(conversations.size).also {
        for (bual in conversations.values) {
            it[bual.id] = ArrayList()
        }
    }
    private var currentConversation = getConversationByID(root)

    fun addChoice(pilihan: Pilihan) {
        val arr = associatedChoices[pilihan.sourceId] ?: return
        arr.add(pilihan)
        numChoices++
    }

    fun displayCurrentConversation(): String {
        return currentConversation?.dialog ?: ":)"
    }

    fun getCurrentChoices(): ArrayList<Pilihan> {
        return associatedChoices[currentConversation?.id] ?: ArrayList()
    }

    fun getConversationByID(id: Int): Perbualan? {
        return conversations[id]
    }

    fun getDestinationChoicePhraseById(id: Int): String {
        if (currentConversation == null) return "current conversation is null"
        if (isReachable(currentConversation!!.id, id)) {
            val list = associatedChoices[currentConversation!!.id] ?: ArrayList()
            for (choice in list) {
                if (choice.destinationId == id) return choice.choicePhrase
            }
        }
        return "not found"
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
        for (conversation in associatedChoices[sourceID] as ArrayList) {
            if (conversation.sourceId == sourceID
                && conversation.destinationId == sinkID
            ) {
                return true
            }
        }
        return false
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

            for (choice in associatedChoices[conversationID]!!) {
                string.append(String.format("%d ", choice.destinationId))
            }

            string.append(System.getProperty("line.separator"))
        }

        return string.toString()
    }
}