package com.ulys

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.ulys.Penerima.Mesej
import com.ulys.Peta.Companion.kpp

class KGrafikNPC : KomponenGrafik() {

    private var entitiDipilih = false

    override fun kemaskini(delta: Float, entiti: Entiti, batch: Batch, pengurusPeta: PengurusPeta) {
        masafrem = (masafrem + delta) % 5
        setCurrentFrame()

//        if (texRegion == null) {
//            Gdx.app.debug("KGrafikNPC", "tiada texture: ${entiti.konfigurasi?.entityID}")
//        }

        if (entitiDipilih) tandaKotakTerpilih(entiti, pengurusPeta)

        texRegion?.let {
            batch.begin()
            batch.draw(texRegion, pos.x, pos.y, 1f, 1f)
            batch.end()
        }

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

    override fun terima(s: String) {
        super.terima(s)
        val lis = s.split(Penerima.PEMISAH)
        if (lis.size == 1) {
            if (Mesej.ENTITI_DIPILIH == Mesej.valueOf(lis.first())) {
                entitiDipilih = true
            } else if (Mesej.ENTITI_TAK_DIPILIH == Mesej.valueOf(lis.first())) {
                entitiDipilih = false
            }
        }
    }

    private fun tandaKotakTerpilih(entiti: Entiti, pengurusPeta: PengurusPeta) {
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        val camera = pengurusPeta.kamera
        val rect = entiti.rect
        shapeRenderer.projectionMatrix = camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.setColor(0.0f, 1.0f, 1.0f, 0.5f)

        val width = rect.getWidth() * kpp * 2f
        val height = rect.getHeight() * kpp / 2f
        val x = rect.x * kpp - width / 4
        val y = rect.y * kpp - height / 2

        shapeRenderer.ellipse(x, y, width, height)
        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)
    }
}