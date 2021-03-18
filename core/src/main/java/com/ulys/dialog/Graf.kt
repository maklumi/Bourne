package com.ulys.dialog

import java.util.*

class Graf(
    private val conversations: Hashtable<Int, Perbualan>,
    private var currentConversationId: Int
) {

    private val associatedChoices = Hashtable<Int, ArrayList<Pilihan>>(conversations.size).also {
        for (bual in conversations.values) {
            it[bual.id] = ArrayList()
        }
    }

    fun addChoice(pilihan: Pilihan) {
        val arr = associatedChoices[pilihan.sourceId] ?: return
        arr.add(pilihan)
    }

    fun displayCurrentConversation(): String {
        return conversations[currentConversationId]?.dialog ?: ":)"
    }

    fun getCurrentChoices(): ArrayList<Pilihan> {
        return associatedChoices[currentConversationId] ?: ArrayList()
    }

    fun getConversationByID(id: Int): Perbualan? {
        return conversations[id]
    }

    fun setCurrentConversation(id: Int) {
        //Can we reach the new conversation from the current one?
        if (isReachable(currentConversationId, id)) {
            currentConversationId = id
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
        string.append("[id perbualan]: ids pilihan")
        string.append(System.getProperty("line.separator"))

        var bilanganPilihan = 0
        val keys = associatedChoices.keys
        for (conversationID in keys) {
            string.append(String.format("[%d]: ", conversationID))

            for (choice in associatedChoices[conversationID]!!) {
                string.append(String.format("%d ", choice.destinationId))
                bilanganPilihan++
            }

            string.append(System.getProperty("line.separator"))
        }
        string.append("Jumlah ayat:  ${conversations.size}, pilihan: $bilanganPilihan")
        string.append(System.getProperty("line.separator"))

        return string.toString()
    }
}