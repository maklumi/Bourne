package com.ulys

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle

class Entiti(util: Util, private val pengurusPeta: PengurusPeta) {

    private val komponenInput = KomponenInput(this)
    private val komponenGrafik = KomponenGrafik(util)
    private val komponenFizik = KomponenFizik(pengurusPeta)

    private val lisKomponen = arrayOf(
        komponenInput,
        komponenGrafik,
        komponenFizik
    )

    enum class Arah { KIRI, KANAN, ATAS, BAWAH }
    enum class Gerak { DIAM, JALAN }

    val rect: Rectangle
        get() = komponenFizik.nextRect

    fun kemaskini(delta: Float, batch: Batch) {
        komponenInput.kemaskini(delta)
        komponenFizik.kemaskini(delta, this)
        komponenGrafik.kemaskini(delta, this, batch, pengurusPeta)
    }

    fun posMesej(kod: Penerima.Mesej, vararg args: String) {
        val mesej = args.fold(kod.name, { acc, s -> acc + Penerima.PEMISAH + s })
        for (i in lisKomponen.indices) {
            lisKomponen[i].terima(mesej)
        }
    }

    fun dispose() {
        for (i in lisKomponen.indices) {
            lisKomponen[i].dispose()
        }
    }

    companion object {
        const val LEBAR_FREM = 16
        const val TINGGI_FREM = 16
    }
}