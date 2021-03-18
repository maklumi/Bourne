package com.ulys.ui

import com.badlogic.gdx.utils.Array
import com.ulys.Konfigurasi

open class BualSubjek {

    private val lisPendengar = Array<Bualan>()

    fun addPendengar(pendengar: Bualan) {
        lisPendengar.add(pendengar)
    }

    fun removePendengar(pendengar: Bualan) {
        lisPendengar.removeValue(pendengar, true)
    }

    fun removeSemuaPendengar() {
        for (observer in lisPendengar) {
            lisPendengar.removeValue(observer, true)
        }
    }

    fun bual(k: Konfigurasi, e: Bualan.UIEvent) {
        for (observer in lisPendengar) {
            observer.onBual(k, e)
        }
    }

}