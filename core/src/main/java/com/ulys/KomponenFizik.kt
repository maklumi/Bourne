package com.ulys

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.ulys.Entiti.Arah
import com.ulys.Entiti.Gerak
import com.ulys.Penerima.Mesej
import com.ulys.PengurusPeta.Companion.kpp

abstract class KomponenFizik : Penerima {

    protected var pos = Vector2(4f, 4f)
    private var arah = Arah.BAWAH
    private var gerak = Gerak.DIAM
    private val laju = Vector2(5f, 5f)
    private var nextPos = Vector2(pos)
    var nextRect = Rectangle()

    open fun kemaskini(delta: Float, entiti: Entiti, pengurusPeta: PengurusPeta) {
        setNextBoundSize()
        if (!akanBerlagaDenganLayer(nextRect, pengurusPeta) && gerak == Gerak.JALAN) {
            setCalculatedPosAsCurrent()
            entiti.posMesej(Mesej.POS_KINI, toJson(pos))
        }
        if (gerak == Gerak.JALAN) kiraPosisi(delta, arah)
        cekMasukPortalLayer(nextRect, pengurusPeta)
    }

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

    private fun kiraPosisi(delta: Float, arah: Arah) {
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

    private fun setCalculatedPosAsCurrent() {
        pos.set(nextPos)
    }

    private fun setNextBoundSize() {
        val lebar = Entiti.LEBAR_FREM * 1f
        val tinggi = Entiti.TINGGI_FREM * 0.5f
        nextRect.set(nextPos.x, nextPos.y, lebar, tinggi)
    }

    private fun updatePositionsAndBound(newPos: Vector2) {
        pos.set(newPos)
        nextPos.set(pos)
        setNextBoundSize()
    }

    private fun akanBerlagaDenganLayer(rect: Rectangle, pengurusPeta: PengurusPeta): Boolean {
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

    private fun cekMasukPortalLayer(rect: Rectangle, pengurusPeta: PengurusPeta): Boolean {
        val layer = pengurusPeta.portalLayer
        for (i in 0 until layer.objects.count) {
            val obj = layer.objects[i]
            if (obj is RectangleMapObject && rect.overlaps(obj.rectangle)) {
                val namaPeta = obj.name
                pengurusPeta.cacheTempatSpawnHampir(pos)
                pengurusPeta.setupPeta(namaPeta)
                pengurusPeta.berpindah = true

                if (namaPeta == PengurusPeta.TOP_WORLD) {
                    pengurusPeta.kamera = OrthographicCamera(75f, 75f)
                    pengurusPeta.kamera.setToOrtho(false, 75f, 75f)
                } else if (namaPeta == PengurusPeta.TOWN) {
                    pengurusPeta.kamera = OrthographicCamera(40f, 30f)
                    pengurusPeta.kamera.setToOrtho(false, 40f, 30f)
                }
                pengurusPeta.kamera.update()

                updatePositionsAndBound(pengurusPeta.posisiMula)
                Gdx.app.debug(TAG, "Masuk portal $namaPeta $pos")
                return true
            }
        }
        return false
    }

    companion object {
        private const val TAG = "KomponenFizik"
    }
}