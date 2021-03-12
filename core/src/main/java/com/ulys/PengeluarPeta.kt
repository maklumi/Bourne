package com.ulys

import java.util.*

object PengeluarPeta {

    enum class JenisPeta {
        TOP_WORLD,
        TOWN,
        CASTLE_OF_DOOM
    }

    private val jadualPeta = Hashtable<JenisPeta, Peta>()

    fun getPeta(jenisPeta: JenisPeta): Peta {
        var peta = jadualPeta[jenisPeta]
        if (peta == null) {
            peta = when (jenisPeta) {
                JenisPeta.TOP_WORLD -> PetaTopWorld()
                JenisPeta.TOWN -> PetaTown()
                JenisPeta.CASTLE_OF_DOOM -> PetaCastleDoom()
            }
            jadualPeta[jenisPeta] = peta
        }
        return peta
    }
}