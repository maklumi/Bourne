package com.ulys

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array

abstract class KomponenGrafik : Penerima {

    private val spritePath = "sprites/characters/Warrior.png"
    protected var texRegion: TextureRegion? = null
    protected var masafrem = 0f

    private val walkLeftFrames = Array<TextureRegion>(4)
    private val walkRightFrames = Array<TextureRegion>(4)
    private val walkUpFrames = Array<TextureRegion>(4)
    private val walkDownFrames = Array<TextureRegion>(4)

    init {
        muatSemuaAnimasi()
    }

    private val walkLeftAnim = Animation(0.25f, walkLeftFrames, Animation.PlayMode.LOOP)
    private val walkRightAnim = Animation(0.25f, walkRightFrames, Animation.PlayMode.LOOP)
    private val walkUpAnim = Animation(0.25f, walkUpFrames, Animation.PlayMode.LOOP)
    private val walkDownAnim = Animation(0.25f, walkDownFrames, Animation.PlayMode.LOOP)

    protected var pos = Vector2()
    protected var arah = Entiti.Arah.BAWAH
    protected var gerak = Entiti.Gerak.DIAM
    protected val shapeRenderer = ShapeRenderer()

    abstract fun kemaskini(delta: Float, entiti: Entiti, batch: Batch, pengurusPeta: PengurusPeta)

    private fun muatSemuaAnimasi() {
        val tekstur = Util.getAsetTekstur(spritePath)
        val texRegions = TextureRegion.split(tekstur, Entiti.LEBAR_FREM, Entiti.TINGGI_FREM)
        for (row in 0..3) {
            for (col in 0..3) {
                val textureRegion = texRegions[row][col]
                when (row) {
                    0 -> walkDownFrames.insert(col, textureRegion)
                    1 -> walkLeftFrames.insert(col, textureRegion)
                    2 -> walkRightFrames.insert(col, textureRegion)
                    3 -> walkUpFrames.insert(col, textureRegion)
                }
            }
        }
    }

    protected fun setCurrentFrame() {
        texRegion = if (gerak == Entiti.Gerak.JALAN) {
            when (arah) {
                Entiti.Arah.KIRI -> walkLeftAnim.getKeyFrame(masafrem)
                Entiti.Arah.KANAN -> walkRightAnim.getKeyFrame(masafrem)
                Entiti.Arah.ATAS -> walkUpAnim.getKeyFrame(masafrem)
                Entiti.Arah.BAWAH -> walkDownAnim.getKeyFrame(masafrem)
            }
        } else {
            when (arah) {
                Entiti.Arah.KIRI -> walkLeftAnim.getKeyFrame(0f)
                Entiti.Arah.KANAN -> walkRightAnim.getKeyFrame(0f)
                Entiti.Arah.ATAS -> walkUpAnim.getKeyFrame(0f)
                Entiti.Arah.BAWAH -> walkDownAnim.getKeyFrame(0f)
            }
        }
    }

    override fun dispose() {
        Util.dispose(spritePath)
    }
}