package com.ulys

import com.badlogic.gdx.Gdx

class KInputPemain : KomponenInput() {

    init {
        Gdx.input.inputProcessor = this
    }

    override fun kemaskini(delta: Float, entiti: Entiti) {
        prosesInput(delta, entiti)
    }

    private fun prosesInput(delta: Float, entiti: Entiti) {
        if (delta < 0.008f) return
        when {
            kekunci[Kekunci.KIRI] == true -> {
                entiti.posMesej(Penerima.Mesej.ARAH_KINI, toJson(Entiti.Arah.KIRI))
                entiti.posMesej(Penerima.Mesej.GERAK_KINI, toJson(Entiti.Gerak.JALAN))
            }
            kekunci[Kekunci.KANAN] == true -> {
                entiti.posMesej(Penerima.Mesej.ARAH_KINI, toJson(Entiti.Arah.KANAN))
                entiti.posMesej(Penerima.Mesej.GERAK_KINI, toJson(Entiti.Gerak.JALAN))
            }
            kekunci[Kekunci.ATAS] == true -> {
                entiti.posMesej(Penerima.Mesej.ARAH_KINI, toJson(Entiti.Arah.ATAS))
                entiti.posMesej(Penerima.Mesej.GERAK_KINI, toJson(Entiti.Gerak.JALAN))
            }
            kekunci[Kekunci.BAWAH] == true -> {
                entiti.posMesej(Penerima.Mesej.ARAH_KINI, toJson(Entiti.Arah.BAWAH))
                entiti.posMesej(Penerima.Mesej.GERAK_KINI, toJson(Entiti.Gerak.JALAN))
            }
            kekunci[Kekunci.QUIT] == true -> {
                Gdx.app.exit()
            }
            else -> entiti.posMesej(Penerima.Mesej.GERAK_KINI, toJson(Entiti.Gerak.DIAM))
        }
    }

}