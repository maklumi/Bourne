package com.ulys

interface Penerima {

    enum class Mesej {
        POS_MULA, POS_KINI,
        ARAH_KINI,
        GERAK_KINI,
        BERLAGA_PETA,
    }

    fun terima(s: String)

    fun dispose() {}

    companion object {
        const val PEMISAH = ":::::"
    }
}