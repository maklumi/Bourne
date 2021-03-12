package com.ulys

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array

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

    protected fun buatAnimasi(texture: Texture, points: Array<Pair<Int, Int>>): Animation<TextureRegion> {
        val textureFrames = TextureRegion.split(texture, Entiti.LEBAR_FREM, Entiti.TINGGI_FREM)
        val animationKeyFrames = Array<TextureRegion>(points.size)
        for (point in points) {
            animationKeyFrames.add(textureFrames[point.first][point.second])
        }
        return Animation(0.25f, animationKeyFrames, Animation.PlayMode.LOOP)
    }

    protected fun loadAnimation(
        texture1: Texture, texture2: Texture, frameIndex: Pair<Int, Int>
    ): Animation<TextureRegion> {
        val frames1 = TextureRegion.split(texture1, Entiti.LEBAR_FREM, Entiti.TINGGI_FREM)
        val frames2 = TextureRegion.split(texture2, Entiti.LEBAR_FREM, Entiti.TINGGI_FREM)

        val animationKeyFrames = Array<TextureRegion>(2)

        animationKeyFrames.add(frames1[frameIndex.first][frameIndex.second])
        animationKeyFrames.add(frames2[frameIndex.first][frameIndex.second])

        return Animation(0.25f, animationKeyFrames, Animation.PlayMode.LOOP)
    }
}