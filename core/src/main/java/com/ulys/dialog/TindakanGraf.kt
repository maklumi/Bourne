package com.ulys.dialog

interface TindakanGraf {

    enum class Tujuan {
        LOAD_STORE_INVENTORY,
        EXIT_CONVERSATION,
        ACCEPT_QUEST,
        NONE,
    }

    fun onTindakanGraf(t: Tujuan, g: Graf)

}