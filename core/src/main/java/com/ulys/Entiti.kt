package com.ulys

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array

open class Entiti(private val util: Util) {

    private val spritePath = "sprites/Characters/Warrior.png"
    var texRegion: TextureRegion? = null
    private var masafrem = 0f

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

    val pos = Vector2(1f, 1f)
    private val laju = Vector2(5f, 5f)

    enum class Arah { KIRI, KANAN, ATAS, BAWAH }

    var arah = Arah.BAWAH
    val nextRect = Rectangle()
    private val nextPos = Vector2(pos)

    fun kemaskini(delta: Float) {
        masafrem = (masafrem + delta) % 5
        setCurrentFrame()
        setNextBoundSize()
    }

    fun kiraPosisi(delta: Float) {
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

    fun setCalculatedPosAsCurrent() {
        pos.set(nextPos)
    }

    private fun muatSemuaAnimasi() {
        val tekstur = util.getAsetTekstur(spritePath)
        val texRegions = TextureRegion.split(tekstur, LEBAR_FREM, TINGGI_FREM)
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

    private fun setCurrentFrame() {
        texRegion = when (arah) {
            Arah.KIRI -> walkLeftAnim.getKeyFrame(masafrem)
            Arah.KANAN -> walkRightAnim.getKeyFrame(masafrem)
            Arah.ATAS -> walkUpAnim.getKeyFrame(masafrem)
            Arah.BAWAH -> walkDownAnim.getKeyFrame(masafrem)
        }
    }

    private fun setNextBoundSize() {
        val lebar = LEBAR_FREM * 1f
        val tinggi = TINGGI_FREM * 0.5f
        nextRect.set(nextPos.x, nextPos.y, lebar, tinggi)
    }

    companion object {
        private const val LEBAR_FREM = 16
        private const val TINGGI_FREM = 16
    }
}