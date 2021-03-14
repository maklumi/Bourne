package com.ulys

interface Penerima {

    enum class Mesej {
        POS_MULA, POS_KINI,
        ARAH_MULA, ARAH_KINI,
        GERAK_MULA, GERAK_KINI,
        BERLAGA_PETA, BERLAGA_ENTITI,
        MUAT_ANIMASI,
        PEMILIHAN_MULA,
        ENTITI_DIPILIH,
        ENTITI_TAK_DIPILIH,
    }

    fun terima(s: String)

    fun dispose() {}

    companion object {
        const val PEMISAH = ":::::"
    }
}