package com.ulys

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class Entiti(util: Util) {

    private val komponenInput = KomponenInput(this)
    val komponenGrafik = KomponenGrafik(util, this)

    val pos = Vector2(4f, 4f)
    private val laju = Vector2(5f, 5f)

    enum class Arah { KIRI, KANAN, ATAS, BAWAH }
    enum class Gerak { DIAM, JALAN }

    var arah = Arah.BAWAH
    var gerak = Gerak.DIAM
    val nextRect = Rectangle()
    private val nextPos = Vector2(pos)

    fun kemaskini(delta: Float) {
        komponenInput.kemaskini(delta)
        komponenGrafik.kemaskini(delta)

        setNextBoundSize()
    }

    fun kiraPosisi(delta: Float) {
        val tempPos = Vector2(pos)
        laju.scl(delta)
        when (arah) {
            Arah.KIRI -> tempPos.x -= laju.x
            Arah.KANAN -> tempPos.x += laju.x
            Arah.ATAS -> tempPos.y += laju.y
            Arah.BAWAH -> tempPos.y -= laju.y
        }
        nextPos.set(tempPos)
        laju.scl(1 / delta)
    }

    fun setCalculatedPosAsCurrent() {
        pos.set(nextPos)
    }

    private fun setNextBoundSize() {
        val lebar = LEBAR_FREM * 1f
        val tinggi = TINGGI_FREM * 0.5f
        nextRect.set(nextPos.x, nextPos.y, lebar, tinggi)
    }

    fun updatePositionsAndBound(newPos: Vector2) {
        pos.set(newPos)
        nextPos.set(pos)
        setNextBoundSize()
    }

    companion object {
        const val LEBAR_FREM = 16
        const val TINGGI_FREM = 16
    }
}