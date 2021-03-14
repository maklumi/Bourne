package com.ulys

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.Ray
import com.ulys.Penerima.Companion.PEMISAH
import com.ulys.Penerima.Mesej
import com.ulys.Peta.Companion.kpp

class KFizikPemain : KomponenFizik() {

    private var pilihSekali = false
    private var koordTikus = Vector3()
    private val ray = Ray(Vector3(), Vector3())
    private val lingkungan = 1 / kpp * 10

    override fun terima(s: String) {
        super.terima(s)
        val lis = s.split(PEMISAH)
        if (lis.size == 2) {
            if (Mesej.PEMILIHAN_MULA == Mesej.valueOf(lis[0])) {
                koordTikus = j.fromJson(Vector3::class.java, lis[1])
                pilihSekali = true
            }
        }
    }

    private fun pilihEntitiTerdekat(pengurus: PengurusPeta) {
        //Convert screen coordinates to world coordinates, then to unit scale coordinates
        pengurus.kamera.unproject(koordTikus)
        koordTikus.scl(1 / kpp)
//        Gdx.app.debug("KFizikPemain", "Koord peta (${koordTikus.x},${koordTikus.y})")

        pengurus.semuaEntiti.forEach { npc ->
            npc.posMesej(Mesej.ENTITI_TAK_DIPILIH) // reset entiti dulu
            val rekEntiti = npc.rect
//            Gdx.app.debug("KFizikPemain", "Calon ( ${rekEntiti.x},${rekEntiti.y})")
            if (npc.rect.contains(koordTikus.x, koordTikus.y)) {
//                Gdx.app.debug("KFizikPemain", "Pemain ( ${nextRect.x},${nextRect.y})")
                val lokasiPemain = Vector3(nextRect.x / kpp, nextRect.y / kpp, 0.0f)
                val lokasiCalon = Vector3(rekEntiti.x, rekEntiti.y, 0.0f)
                ray.set(lokasiPemain, lokasiCalon)
                val distance = ray.origin.dst(lokasiCalon)
                if (distance <= lingkungan) {
                    Gdx.app.debug("KFizikPemain", "Dipilih: ${npc.konfigurasi?.entityID}")
                    npc.posMesej(Mesej.ENTITI_DIPILIH)
                }
            }
        }
    }

    override fun kemaskini(delta: Float, entiti: Entiti, pengurusPeta: PengurusPeta) {
        setNextBoundSize(0.3f, 0.5f)

        if (pilihSekali) {
            pilihSekali = false
            pilihEntitiTerdekat(pengurusPeta)
        }

        if (!akanBerlagaDenganLayer(entiti, nextRect, pengurusPeta.collisionLayer)
            && !akanBerlagaEntiti(entiti, pengurusPeta.semuaEntiti)
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