package com.ulys

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.ulys.PengurusPeta.Companion.kpp

class KGrafikPemain : KomponenGrafik() {

    override fun kemaskini(delta: Float, entiti: Entiti, batch: Batch, pengurusPeta: PengurusPeta) {
        masafrem = (masafrem + delta) % 5
        setCurrentFrame()

        batch.begin()
        batch.draw(texRegion, pos.x, pos.y, 1f, 1f)
        batch.end()

        shapeRenderer.apply {
            projectionMatrix = pengurusPeta.kamera.combined
            begin(ShapeRenderer.ShapeType.Line)
            color = Color.GRAY
            val r = entiti.rect
            rect(r.x * kpp, r.y * kpp, r.width * kpp, r.height * kpp)
            end()
        }
    }

    override fun terima(s: String) {
        val lis = s.split(Penerima.PEMISAH)
        if (lis.size < 2) return
        // untuk satu payload
        if (lis.size == 2) {
            when (Penerima.Mesej.valueOf(lis[0])) {
                Penerima.Mesej.POS_MULA -> pos = fromJson(lis[1])
                Penerima.Mesej.POS_KINI -> pos = fromJson(lis[1])
                Penerima.Mesej.ARAH_KINI -> arah = fromJson(lis[1])
                Penerima.Mesej.GERAK_KINI -> gerak = fromJson(lis[1])
                else -> {
                }
            }
        }
    }

}