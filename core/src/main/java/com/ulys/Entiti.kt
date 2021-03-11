package com.ulys

import com.badlogic.gdx.graphics.g2d.Batch

class Entiti(util: Util, pengurusPeta: PengurusPeta) {

    private val komponenInput = KomponenInput(this)
    val komponenGrafik = KomponenGrafik(util, this)
    val komponenFizik = KomponenFizik(pengurusPeta)

    enum class Arah { KIRI, KANAN, ATAS, BAWAH }
    enum class Gerak { DIAM, JALAN }

    var arah = Arah.BAWAH
    var gerak = Gerak.DIAM

    fun kemaskini(delta: Float, batch: Batch) {
        komponenInput.kemaskini(delta)
        komponenGrafik.kemaskini(delta, this, batch)
        komponenFizik.kemaskini(delta, this)
    }

    companion object {
        const val LEBAR_FREM = 16
        const val TINGGI_FREM = 16
    }
}