package com.ulys.dialog

import com.badlogic.gdx.utils.Array

open class SubjekTindakanGraf {

    private val observers = Array<TindakanGraf>()

    fun addPemerhatiTindakan(observer: TindakanGraf) {
        observers.add(observer)
    }

    fun removeSemuaPemerhatiTindakan() {
        observers.forEach { observers.removeValue(it, true) }
    }

    fun tindakanGraf(t: TindakanGraf.Tujuan, g: Graf) {
        observers.forEach { it.onTindakanGraf(t, g) }
    }

}