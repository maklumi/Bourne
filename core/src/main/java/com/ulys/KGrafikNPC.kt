package com.ulys

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
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

        shapeRenderer.apply {
            projectionMatrix = pengurusPeta.kamera.combined
            begin(ShapeRenderer.ShapeType.Line)
            color = Color.YELLOW
            val r = entiti.rect
            rect(
                r.x * PengurusPeta.kpp,
                r.y * PengurusPeta.kpp,
                r.width * PengurusPeta.kpp,
                r.height * PengurusPeta.kpp
            )
            end()
        }
    }

    private fun setCurrentFrame() {
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

    override fun dispose() {
        Util.dispose(walkingAnimationSpritePath)
        Util.dispose(immobileAnimation1)
        Util.dispose(immobileAnimation2)
    }
}