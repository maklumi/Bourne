package com.ulys

sealed class Pengeluar {

    abstract fun get(): Entiti

    object Pemain : Pengeluar() {
        override fun get(): Entiti {
            return Entiti(KInputPemain(), KFizikPemain(), KGrafikPemain())
        }
    }

}