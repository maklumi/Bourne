package com.ulys.dialog

interface TindakanGraf {

    enum class Tujuan {
        LOAD_STORE_INVENTORY,
        EXIT_CONVERSATION,
        NONE,
    }

    fun onTindakanGraf(t: Tujuan, g: Graf)

}