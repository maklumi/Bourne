package com.ulys

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.ulys.Peta.Companion.kpp

class KGrafikNPC : KomponenGrafik() {

    override fun kemaskini(delta: Float, entiti: Entiti, batch: Batch, pengurusPeta: PengurusPeta) {
        masafrem = (masafrem + delta) % 5
        setCurrentFrame()

        batch.begin()
        batch.draw(texRegion, pos.x, pos.y, 1f, 1f)
        batch.end()

        shapeRenderer.apply {
            projectionMatrix = pengurusPeta.kamera.combined
            begin(ShapeRenderer.ShapeType.Line)
            color = Color.YELLOW
            val r = entiti.rect
            rect(
                r.x * kpp,
                r.y * kpp,
                r.width * kpp,
                r.height * kpp
            )
            end()
        }
    }

}