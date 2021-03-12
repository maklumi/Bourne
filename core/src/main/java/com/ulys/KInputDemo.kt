package com.ulys

class KInputDemo : KomponenInput() {

    private var arah = Entiti.Arah.ATAS

    override fun kemaskini(delta: Float, entiti: Entiti) {
        if (delta < 1 / 60f) return
        when (arah) {
            Entiti.Arah.KIRI -> {
                entiti.posMesej(Penerima.Mesej.ARAH_KINI, toJson(Entiti.Arah.KIRI))
                entiti.posMesej(Penerima.Mesej.GERAK_KINI, toJson(Entiti.Gerak.JALAN))
            }
            Entiti.Arah.KANAN -> {
                entiti.posMesej(Penerima.Mesej.ARAH_KINI, toJson(Entiti.Arah.KANAN))
                entiti.posMesej(Penerima.Mesej.GERAK_KINI, toJson(Entiti.Gerak.JALAN))
            }
            Entiti.Arah.ATAS -> {
                entiti.posMesej(Penerima.Mesej.ARAH_KINI, toJson(Entiti.Arah.ATAS))
                entiti.posMesej(Penerima.Mesej.GERAK_KINI, toJson(Entiti.Gerak.JALAN))
            }
            Entiti.Arah.BAWAH -> {
                entiti.posMesej(Penerima.Mesej.ARAH_KINI, toJson(Entiti.Arah.BAWAH))
                entiti.posMesej(Penerima.Mesej.GERAK_KINI, toJson(Entiti.Gerak.JALAN))
            }
        }
    }

    override fun terima(s: String) {
        val lis = s.split(Penerima.PEMISAH)
        // dihantar dari komponen fizik bila berlaga dengan collision layer
        if (lis.size == 1) {
            if (Penerima.Mesej.BERLAGA_PETA == Penerima.Mesej.valueOf(lis.first())) {
                arah = getArahSelain(arah)
            }
        }
    }

    private fun getArahSelain(ini: Entiti.Arah): Entiti.Arah {
        var tempArah: Entiti.Arah
        do {
            tempArah = Entiti.Arah.values().random()
        } while (tempArah == ini)
        return tempArah
    }
}