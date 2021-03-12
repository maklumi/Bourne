package com.ulys

import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.ulys.Entiti.Arah
import com.ulys.Entiti.Gerak
import com.ulys.KomponenFizik.BoundingBoxLocation.*
import com.ulys.Penerima.Mesej
import com.ulys.PengurusPeta.Companion.kpp

abstract class KomponenFizik : Penerima {

    protected var pos = Vector2(4f, 4f)
    protected var arah = Arah.BAWAH
    protected var gerak = Gerak.DIAM
    private val laju = Vector2(5f, 5f)
    protected var nextPos = Vector2(pos)
    var nextRect = Rectangle()

    enum class BoundingBoxLocation { BOTTOM_LEFT, BOTTOM_CENTER, CENTER }

    protected open var boundingBoxLocation = BOTTOM_CENTER

    abstract fun kemaskini(delta: Float, entiti: Entiti, pengurusPeta: PengurusPeta)

    override fun terima(s: String) {
        val lis = s.split(Penerima.PEMISAH)
        if (lis.size == 2) {
            when (Mesej.valueOf(lis[0])) {
                Mesej.POS_MULA -> {
                    pos = fromJson(lis[1])
                    nextPos = fromJson(lis[1])
                }
                Mesej.ARAH_KINI -> arah = fromJson(lis[1])
                Mesej.GERAK_KINI -> gerak = fromJson(lis[1])
                else -> {
                }
            }
        }
    }

    fun kiraPosisi(delta: Float, arah: Arah) {
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

    fun setCalculatedPosAsCurrent(entiti: Entiti) {
        pos.set(nextPos)
        entiti.posMesej(Mesej.POS_KINI, toJson(pos))
    }

    fun akanBerlagaDenganLayer(entiti: Entiti, rect: Rectangle, layer: MapLayer): Boolean {
        return akanBerlaga(entiti, rect, layer)
    }

    private fun akanBerlaga(entiti: Entiti, rect: Rectangle, layer: MapLayer): Boolean {
        // convert bound dalam kaki unit ke pixel
        rect.setPosition(rect.x / kpp, rect.y / kpp)
        for (i in 0 until layer.objects.count) {
            val obj = layer.objects[i]
            if (obj is RectangleMapObject && rect.overlaps(obj.rectangle)) {
                entiti.posMesej(Mesej.BERLAGA_PETA)
                return true
            }
        }
        return false
    }

    protected fun setNextBoundSize(pcLebar: Float = 1f, pcTinggi: Float = 0.5f) {
        val lebar = Entiti.LEBAR_FREM * pcLebar
        val tinggi = Entiti.TINGGI_FREM * pcTinggi
        when (boundingBoxLocation) {
            BOTTOM_LEFT -> nextRect.set(nextPos.x, nextPos.y, lebar, tinggi)
            BOTTOM_CENTER -> {
                nextRect.set(nextPos.x + (1.0f - pcLebar) / 2f, nextPos.y, lebar, tinggi)
            }
            CENTER -> {
                nextRect.set(
                    nextPos.x + (1.0f - pcLebar) / 2f,
                    nextPos.y + (1.0f - pcTinggi) / 2f,
                    lebar, tinggi
                )
            }
        }
    }
}