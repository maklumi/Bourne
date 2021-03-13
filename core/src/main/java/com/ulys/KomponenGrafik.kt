package com.ulys

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import java.util.*

abstract class KomponenGrafik : Penerima {

    protected var masafrem = 0f
    protected var texRegion: TextureRegion? = null
    protected var pos = Vector2()
    private var arah = Entiti.Arah.BAWAH
    private var gerak = Entiti.Gerak.DIAM
    protected val shapeRenderer = ShapeRenderer()
    private var animations = Hashtable<Entiti.AnimationType, Animation<TextureRegion>>()

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
                Penerima.Mesej.MUAT_ANIMASI -> {
                    val config = j.fromJson(Konfigurasi::class.java, lis[1])
                    config.animationConfig.forEach {
                        if (it.texturePaths.size == 1) {
                            val anim = buatAnimasi(it.texturePaths[0], it.gridPoints)
                            animations[it.animationType] = anim
                        } else if (it.texturePaths.size == 2) {
                            val anim = loadAnimation(it.texturePaths[0], it.texturePaths[1], it.gridPoints)
                            animations[it.animationType] = anim
                        }
                    }
                }
                else -> {
                }
            }
        }
    }

    protected fun setCurrentFrame() {
        when (gerak) {
            Entiti.Gerak.JALAN -> {
                texRegion = when (arah) {
                    Entiti.Arah.KIRI -> {
                        val anim = animations[Entiti.AnimationType.WALK_LEFT]
                        anim?.getKeyFrame(masafrem)
                    }
                    Entiti.Arah.KANAN -> {
                        val anim = animations[Entiti.AnimationType.WALK_RIGHT]
                        anim?.getKeyFrame(masafrem)
                    }
                    Entiti.Arah.ATAS -> {
                        val anim = animations[Entiti.AnimationType.WALK_UP]
                        anim?.getKeyFrame(masafrem)
                    }
                    Entiti.Arah.BAWAH -> {
                        val anim = animations[Entiti.AnimationType.WALK_DOWN]
                        anim?.getKeyFrame(masafrem)
                    }
                }
            }
            Entiti.Gerak.DIAM -> {
                texRegion = when (arah) {
                    Entiti.Arah.KIRI -> {
                        val anim = animations[Entiti.AnimationType.WALK_LEFT]
                        anim?.getKeyFrame(0f)
                    }
                    Entiti.Arah.KANAN -> {
                        val anim = animations[Entiti.AnimationType.WALK_RIGHT]
                        anim?.getKeyFrame(0f)
                    }
                    Entiti.Arah.ATAS -> {
                        val anim = animations[Entiti.AnimationType.WALK_UP]
                        anim?.getKeyFrame(0f)
                    }
                    Entiti.Arah.BAWAH -> {
                        val anim = animations[Entiti.AnimationType.WALK_DOWN]
                        anim?.getKeyFrame(0f)
                    }
                }
            }
            Entiti.Gerak.IMMOBILE -> {
                val anim = animations[Entiti.AnimationType.IMMOBILE]
                texRegion = anim?.getKeyFrame(masafrem)
            }
        }
    }

    private fun buatAnimasi(path: String, points: Array<GridPoint2>): Animation<TextureRegion> {
        val texture = Util.getAsetTekstur(path)
        val textureFrames = TextureRegion.split(texture, Entiti.LEBAR_FREM, Entiti.TINGGI_FREM)
        val animationKeyFrames = Array<TextureRegion>(points.size)
        for (point in points) {
            animationKeyFrames.add(textureFrames[point.x][point.y])
        }
        return Animation(0.25f, animationKeyFrames, Animation.PlayMode.LOOP)
    }

    private fun loadAnimation(path1: String, path2: String, points: Array<GridPoint2>): Animation<TextureRegion> {
        val texture1 = Util.getAsetTekstur(path1)
        val texture2 = Util.getAsetTekstur(path2)
        val frames1 = TextureRegion.split(texture1, Entiti.LEBAR_FREM, Entiti.TINGGI_FREM)
        val frames2 = TextureRegion.split(texture2, Entiti.LEBAR_FREM, Entiti.TINGGI_FREM)

        val animationKeyFrames = Array<TextureRegion>(2)

        animationKeyFrames.add(frames1[points[0].x][points[0].y])
        animationKeyFrames.add(frames2[points[1].x][points[1].y])

        return Animation(0.25f, animationKeyFrames, Animation.PlayMode.LOOP)
    }
}