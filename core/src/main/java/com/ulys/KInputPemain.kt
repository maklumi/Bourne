package com.ulys

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3

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
                entiti.posMesej(Penerima.Mesej.ARAH_KINI, toJson(Entiti.Arah.LEFT))
                entiti.posMesej(Penerima.Mesej.GERAK_KINI, toJson(Entiti.Gerak.WALKING))
            }
            kekunci[Kekunci.KANAN] == true -> {
                entiti.posMesej(Penerima.Mesej.ARAH_KINI, toJson(Entiti.Arah.RIGHT))
                entiti.posMesej(Penerima.Mesej.GERAK_KINI, toJson(Entiti.Gerak.WALKING))
            }
            kekunci[Kekunci.ATAS] == true -> {
                entiti.posMesej(Penerima.Mesej.ARAH_KINI, toJson(Entiti.Arah.UP))
                entiti.posMesej(Penerima.Mesej.GERAK_KINI, toJson(Entiti.Gerak.WALKING))
            }
            kekunci[Kekunci.BAWAH] == true -> {
                entiti.posMesej(Penerima.Mesej.ARAH_KINI, toJson(Entiti.Arah.DOWN))
                entiti.posMesej(Penerima.Mesej.GERAK_KINI, toJson(Entiti.Gerak.WALKING))
            }
            kekunci[Kekunci.QUIT] == true -> {
                Gdx.app.exit()
            }
            else -> entiti.posMesej(Penerima.Mesej.GERAK_KINI, toJson(Entiti.Gerak.IDLE))
        }

        // input dari mouse
        if (tetikus[Tikus.PILIH] == true) {
            Gdx.app.debug("KInputPemain", "Koord skrin (${koordinatTikus.x},${koordinatTikus.y})")
            entiti.posMesej(Penerima.Mesej.PEMILIHAN_MULA, j.toJson(koordinatTikus))
            tetikus[Tikus.PILIH] = false
        }
    }

}