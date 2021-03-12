package com.ulys

import com.badlogic.gdx.Gdx
import com.ulys.Entiti.*
import com.ulys.Penerima.Mesej

class KInputNPC : KomponenInput() {

    private var arah = Arah.ATAS
    private var gerak = Gerak.DIAM
    private var masa = 0f

    init {
        Gdx.input.inputProcessor = this
    }

    override fun kemaskini(delta: Float, entiti: Entiti) {
        if (delta < 1 / 60f) return
        if (kekunci[Kekunci.QUIT] == true) Gdx.app.exit()

        masa += delta
        if (masa > 3) {
            masa = 0f
            gerak = Gerak.values().random()
            arah = Arah.values().random()
        }

        if (gerak == Gerak.IMMOBILE) {
            entiti.posMesej(Mesej.GERAK_KINI, toJson(gerak))
            return
        } else if (gerak == Gerak.DIAM) {
            entiti.posMesej(Mesej.GERAK_KINI, toJson(gerak))
            return
        }

        when (arah) {
            Arah.KIRI -> {
                entiti.posMesej(Mesej.ARAH_KINI, toJson(Arah.KIRI))
                entiti.posMesej(Mesej.GERAK_KINI, toJson(Gerak.JALAN))
            }
            Arah.KANAN -> {
                entiti.posMesej(Mesej.ARAH_KINI, toJson(Arah.KANAN))
                entiti.posMesej(Mesej.GERAK_KINI, toJson(Gerak.JALAN))
            }
            Arah.ATAS -> {
                entiti.posMesej(Mesej.ARAH_KINI, toJson(Arah.ATAS))
                entiti.posMesej(Mesej.GERAK_KINI, toJson(Gerak.JALAN))
            }
            Arah.BAWAH -> {
                entiti.posMesej(Mesej.ARAH_KINI, toJson(Arah.BAWAH))
                entiti.posMesej(Mesej.GERAK_KINI, toJson(Gerak.JALAN))
            }
        }
    }

    override fun terima(s: String) {
        val lis = s.split(Penerima.PEMISAH)
        // dihantar dari komponen fizik bila berlaga dengan collision layer
        if (lis.size == 1) {
            if (Mesej.BERLAGA_PETA == Mesej.valueOf(lis.first())) {
                arah = getArahSelain(arah)
            }
        }
    }

    private fun getArahSelain(ini: Arah): Arah {
        var tempArah: Arah
        do {
            tempArah = Arah.values().random()
        } while (tempArah == ini)
        return tempArah
    }
}