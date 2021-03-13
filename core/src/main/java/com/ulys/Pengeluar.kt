package com.ulys

import com.ulys.Penerima.Mesej

sealed class Pengeluar {

    abstract fun get(): Entiti

    object Pemain : Pengeluar() {
        override fun get(): Entiti {
            val entiti = Entiti(KInputPemain(), KFizikPemain(), KGrafikPemain())
            entiti.muatKonfigurasiGrafik("scripts/player.json")
            entiti.posMesej(Mesej.MUAT_ANIMASI, j.toJson(entiti.konfigurasi))
            return entiti
        }
    }

    object NPC : Pengeluar() {
        override fun get(): Entiti {
            return Entiti(KInputNPC(), KFizikNPC(), KGrafikNPC())
        }
    }
}