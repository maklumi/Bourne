package com.ulys

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.ulys.Entiti.Arah
import com.ulys.Entiti.Gerak
import com.ulys.Penerima.Mesej

class KInputNPC : KomponenInput() {

    private var arah = Arah.UP
    private var gerak = Gerak.IDLE
    private var masa = 0f

    override fun kemaskini(delta: Float, entiti: Entiti) {
        if (delta < 1 / 60f) return
        if (kekunci[Kekunci.QUIT] == true) Gdx.app.exit()

        // kalau entiti IMMOBILE tak perlu update
        if (gerak == Gerak.IMMOBILE) {
            entiti.posMesej(Mesej.GERAK_KINI, toJson(gerak))
            return
        }

        masa += delta
        if (masa > MathUtils.random(1, 5)) {
            masa = 0f
            gerak = Gerak.randomDiamAtauJalan()
            arah = Arah.values().random()
        }

        if (gerak == Gerak.IDLE) {
            entiti.posMesej(Mesej.GERAK_KINI, toJson(gerak))
            return
        }

        if (gerak == Gerak.WALKING) {
            entiti.posMesej(Mesej.GERAK_KINI, toJson(Gerak.WALKING))
        }

        when (arah) {
            Arah.LEFT -> entiti.posMesej(Mesej.ARAH_KINI, toJson(Arah.LEFT))
            Arah.RIGHT -> entiti.posMesej(Mesej.ARAH_KINI, toJson(Arah.RIGHT))
            Arah.UP -> entiti.posMesej(Mesej.ARAH_KINI, toJson(Arah.UP))
            Arah.DOWN -> entiti.posMesej(Mesej.ARAH_KINI, toJson(Arah.DOWN))
        }
    }

    override fun terima(s: String) {
        val lis = s.split(Penerima.PEMISAH)
        // dihantar dari komponen fizik bila berlaga dengan collision layer
        if (lis.size == 1) {
            if (Mesej.BERLAGA_PETA == Mesej.valueOf(lis.first())) {
                arah = getArahSelain(arah)
            } else if (Mesej.BERLAGA_ENTITI == Mesej.valueOf(lis.first())) {
                gerak = Gerak.IDLE
            }
        }

        if (lis.size == 2) {
            if (Mesej.GERAK_MULA == Mesej.valueOf(lis[0])) {
                gerak = j.fromJson(Gerak::class.java, lis[1])
            } else if (Mesej.ARAH_MULA == Mesej.valueOf(lis[0])) {
                arah = j.fromJson(Arah::class.java, lis[1])
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