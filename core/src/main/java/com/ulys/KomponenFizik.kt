package com.ulys

import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.ulys.PengurusPeta.Companion.kpp

class KomponenFizik(private val pengurusPeta: PengurusPeta) {

    val pos = Vector2(4f, 4f)
    private val laju = Vector2(5f, 5f)
    private val nextPos = Vector2(pos)
    val nextRect = Rectangle()

    fun kemaskini(delta: Float, entiti: Entiti) {
        setNextBoundSize()
        if (!akanBerlagaDenganLayer(nextRect) && entiti.gerak == Entiti.Gerak.JALAN) {
            setCalculatedPosAsCurrent()
        }
        if (entiti.gerak == Entiti.Gerak.JALAN) kiraPosisi(delta, entiti.arah)
    }

    private fun kiraPosisi(delta: Float, arah: Entiti.Arah) {
        val tempPos = Vector2(pos)
        laju.scl(delta)
        when (arah) {
            Entiti.Arah.KIRI -> tempPos.x -= laju.x
            Entiti.Arah.KANAN -> tempPos.x += laju.x
            Entiti.Arah.ATAS -> tempPos.y += laju.y
            Entiti.Arah.BAWAH -> tempPos.y -= laju.y
        }
        nextPos.set(tempPos)
        laju.scl(1 / delta)
    }

    private fun setCalculatedPosAsCurrent() {
        pos.set(nextPos)
    }

    private fun setNextBoundSize() {
        val lebar = Entiti.LEBAR_FREM * 1f
        val tinggi = Entiti.TINGGI_FREM * 0.5f
        nextRect.set(nextPos.x, nextPos.y, lebar, tinggi)
    }

    fun updatePositionsAndBound(newPos: Vector2) {
        pos.set(newPos)
        nextPos.set(pos)
        setNextBoundSize()
    }

    private fun akanBerlagaDenganLayer(rect: Rectangle): Boolean {
        val layer = pengurusPeta.collisionLayer
        return akanBerlaga(rect, layer)
    }

    private fun akanBerlaga(rect: Rectangle, layer: MapLayer): Boolean {
        // convert bound dalam kaki unit ke pixel
        rect.setPosition(rect.x / kpp, rect.y / kpp)
        for (i in 0 until layer.objects.count) {
            val obj = layer.objects[i]
            if (obj is RectangleMapObject && rect.overlaps(obj.rectangle)) {
                return true
            }
        }
        return false
    }
}