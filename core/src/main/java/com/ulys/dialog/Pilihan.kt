package com.ulys.dialog

class Pilihan {
    var sourceId = -1
    var destinationId = -1
    var choicePhrase = "choice phrase"
    var conversationCommandEvent = TindakanGraf.Tujuan.NONE

    override fun toString(): String {
        return choicePhrase
    }
}