package com.ulys

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array

class KGrafikNPC : KomponenGrafik() {

    private val walkingAnimationSpritePath = "sprites/characters/Engineer.png"
    private val immobileAnimation1 = "sprites/characters/Player0.png"
    private val immobileAnimation2 = "sprites/characters/Player1.png"

    private val animDown: Animation<TextureRegion>
    private val animLeft: Animation<TextureRegion>
    private val animRigh: Animation<TextureRegion>
    private val animUpgo: Animation<TextureRegion>

    private val animDirisitu: Animation<TextureRegion>

    init {
        Util.muatAsetTekstur(walkingAnimationSpritePath)
        Util.muatAsetTekstur(immobileAnimation1)
        Util.muatAsetTekstur(immobileAnimation2)

        val texture = Util.getAsetTekstur(walkingAnimationSpritePath)
        val texture1 = Util.getAsetTekstur(immobileAnimation1)
        val texture2 = Util.getAsetTekstur(immobileAnimation2)

        // Pair(baris, kolum)
        val down = Array<Pair<Int, Int>>(4)
        val left = Array<Pair<Int, Int>>(4)
        val righ = Array<Pair<Int, Int>>(4)
        val upgo = Array<Pair<Int, Int>>(4)
        down.add(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3))
        left.add(Pair(1, 0), Pair(1, 1), Pair(1, 2), Pair(1, 3))
        righ.add(Pair(2, 0), Pair(2, 1), Pair(2, 2), Pair(2, 3))
        upgo.add(Pair(3, 0), Pair(3, 1), Pair(3, 2), Pair(3, 3))

        animDown = buatAnimasi(texture, down)
        animLeft = buatAnimasi(texture, left)
        animRigh = buatAnimasi(texture, righ)
        animUpgo = buatAnimasi(texture, upgo)

        animDirisitu = loadAnimation(texture1, texture2, Pair(0, 0))
    }

    override fun kemaskini(delta: Float, entiti: Entiti, batch: Batch, pengurusPeta: PengurusPeta) {
        masafrem = (masafrem + delta) % 5
        setCurrentFrame()

        batch.begin()
        batch.draw(texRegion, pos.x, pos.y, 1f, 1f)
        batch.end()
    }

    private fun setCurrentFrame() {
//        gerak = Entiti.Gerak.IMMOBILE
        when (gerak) {
            Entiti.Gerak.JALAN -> {
                texRegion = when (arah) {
                    Entiti.Arah.KIRI -> animLeft.getKeyFrame(masafrem)
                    Entiti.Arah.KANAN -> animRigh.getKeyFrame(masafrem)
                    Entiti.Arah.ATAS -> animUpgo.getKeyFrame(masafrem)
                    Entiti.Arah.BAWAH -> animDown.getKeyFrame(masafrem)
                }
            }
            Entiti.Gerak.DIAM -> {
                texRegion = when (arah) {
                    Entiti.Arah.KIRI -> animLeft.getKeyFrame(0f)
                    Entiti.Arah.KANAN -> animRigh.getKeyFrame(0f)
                    Entiti.Arah.ATAS -> animUpgo.getKeyFrame(0f)
                    Entiti.Arah.BAWAH -> animDown.getKeyFrame(0f)
                }
            }
            Entiti.Gerak.IMMOBILE -> {
                texRegion = animDirisitu.getKeyFrame(masafrem)
            }
        }
    }

    private fun buatAnimasi(texture: Texture, points: Array<Pair<Int, Int>>): Animation<TextureRegion> {
        val textureFrames = TextureRegion.split(texture, Entiti.LEBAR_FREM, Entiti.TINGGI_FREM)
        val animationKeyFrames = Array<TextureRegion>(points.size)
        for (point in points) {
            animationKeyFrames.add(textureFrames[point.first][point.second])
        }
        return Animation(0.25f, animationKeyFrames, Animation.PlayMode.LOOP)
    }

    private fun loadAnimation(
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