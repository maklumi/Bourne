package com.ulys

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2

abstract class KomponenGrafik : Penerima {

    protected var masafrem = 0f
    protected var texRegion: TextureRegion? = null
    protected var pos = Vector2()
    protected var arah = Entiti.Arah.BAWAH
    protected var gerak = Entiti.Gerak.DIAM
    protected val shapeRenderer = ShapeRenderer()

    abstract fun kemaskini(delta: Float, entiti: Entiti, batch: Batch, pengurusPeta: PengurusPeta)

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