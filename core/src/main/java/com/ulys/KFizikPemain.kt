package com.ulys

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class KFizikPemain : KomponenFizik() {

    override fun kemaskini(delta: Float, entiti: Entiti, pengurusPeta: PengurusPeta) {
        setNextBoundSize(0.3f, 0.5f)

        if (!akanBerlagaDenganLayer(entiti, nextRect, pengurusPeta.collisionLayer)
            && gerak == Entiti.Gerak.WALKING
        ) {
            setCalculatedPosAsCurrent(entiti)
        }
        if (gerak == Entiti.Gerak.WALKING) kiraPosisi(delta, arah)

        cekMasukPortalLayer(nextRect, pengurusPeta)

        val kamera = pengurusPeta.kamera
        kamera.position.set(pos.x, pos.y, 0f)
        kamera.update()
    }

    private fun cekMasukPortalLayer(rect: Rectangle, pengurusPeta: PengurusPeta): Boolean {
        val layer = pengurusPeta.portalLayer
        for (i in 0 until layer.objects.count) {
            val obj = layer.objects[i]
            if (obj is RectangleMapObject && rect.overlaps(obj.rectangle)) {
                val namaPeta = obj.name
                pengurusPeta.cacheTempatSpawnHampir(pos)
                pengurusPeta.setupPeta(PengeluarPeta.JenisPeta.valueOf(namaPeta))
                pengurusPeta.berpindah = true

                if (namaPeta == PengeluarPeta.JenisPeta.TOP_WORLD.name) {
                    pengurusPeta.kamera = OrthographicCamera(75f, 75f)
                    pengurusPeta.kamera.setToOrtho(false, 75f, 75f)
                } else if (namaPeta == PengeluarPeta.JenisPeta.TOWN.name) {
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

    private fun updatePositionsAndBound(newPos: Vector2) {
        pos.set(newPos)
        nextPos.set(pos)
        setNextBoundSize(0.3f, 0.5f)
    }

    companion object {
        const val TAG = "KFizikPemain"
    }
}