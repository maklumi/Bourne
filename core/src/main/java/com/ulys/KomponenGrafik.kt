package com.ulys

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.ulys.Konfigurasi.AnimationConfig
import java.util.*

abstract class KomponenGrafik : Penerima {

    protected var masafrem = 0f
    protected var texRegion: TextureRegion? = null
    protected var pos = Vector2()
    private var arah = Entiti.Arah.DOWN
    private var gerak = Entiti.Gerak.IDLE
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
                            animations[it.animationType] = buatAnimasi(it)
                        } else if (it.texturePaths.size == 2) { // IMMOBILE
                            animations[it.animationType] = loadAnimation(it)
                        }
                    }
                }
                else -> {
                }
            }
        }
    }

    protected fun setCurrentFrame() {
        when (arah) {
            Entiti.Arah.LEFT -> {
                texRegion = when (gerak) {
                    Entiti.Gerak.IDLE -> {
                        val anim = animations[Entiti.AnimationType.WALK_LEFT] ?: return
                        anim.getKeyFrame(0f)
                    }
                    Entiti.Gerak.WALKING -> {
                        val anim = animations[Entiti.AnimationType.WALK_LEFT] ?: return
                        anim.getKeyFrame(masafrem)
                    }
                    Entiti.Gerak.IMMOBILE -> {
                        val anim = animations[Entiti.AnimationType.IMMOBILE] ?: return
                        anim.getKeyFrame(masafrem)
                    }
                }
            }
            Entiti.Arah.RIGHT -> {
                texRegion = when (gerak) {
                    Entiti.Gerak.IDLE -> {
                        val anim = animations[Entiti.AnimationType.WALK_RIGHT] ?: return
                        anim.getKeyFrame(0f)
                    }
                    Entiti.Gerak.WALKING -> {
                        val anim = animations[Entiti.AnimationType.WALK_RIGHT] ?: return
                        anim.getKeyFrame(masafrem)
                    }
                    Entiti.Gerak.IMMOBILE -> {
                        val anim = animations[Entiti.AnimationType.IMMOBILE] ?: return
                        anim.getKeyFrame(masafrem)
                    }
                }
            }
            Entiti.Arah.UP -> {
                texRegion = when (gerak) {
                    Entiti.Gerak.IDLE -> {
                        val anim = animations[Entiti.AnimationType.WALK_UP] ?: return
                        anim.getKeyFrame(0f)
                    }
                    Entiti.Gerak.WALKING -> {
                        val anim = animations[Entiti.AnimationType.WALK_UP] ?: return
                        anim.getKeyFrame(masafrem)
                    }
                    Entiti.Gerak.IMMOBILE -> {
                        val anim = animations[Entiti.AnimationType.IMMOBILE] ?: return
                        anim.getKeyFrame(masafrem)
                    }
                }
            }
            Entiti.Arah.DOWN -> {
                texRegion = when (gerak) {
                    Entiti.Gerak.IDLE -> {
                        val anim = animations[Entiti.AnimationType.WALK_DOWN] ?: return
                        anim.getKeyFrame(0f)
                    }
                    Entiti.Gerak.WALKING -> {
                        val anim = animations[Entiti.AnimationType.WALK_DOWN] ?: return
                        anim.getKeyFrame(masafrem)
                    }
                    Entiti.Gerak.IMMOBILE -> {
                        val anim = animations[Entiti.AnimationType.IMMOBILE] ?: return
                        anim.getKeyFrame(masafrem)
                    }
                }
            }
        }
    }

    /*
        protected fun setCurrentFrame() {
            when (gerak) {
                Entiti.Gerak.WALKING -> {
                    texRegion = when (arah) {
                        Entiti.Arah.LEFT -> {
                            val anim = animations[Entiti.AnimationType.WALK_LEFT]
                            anim?.getKeyFrame(masafrem)
                        }
                        Entiti.Arah.RIGHT -> {
                            val anim = animations[Entiti.AnimationType.WALK_RIGHT]
                            anim?.getKeyFrame(masafrem)
                        }
                        Entiti.Arah.UP -> {
                            val anim = animations[Entiti.AnimationType.WALK_UP]
                            anim?.getKeyFrame(masafrem)
                        }
                        Entiti.Arah.DOWN -> {
                            val anim = animations[Entiti.AnimationType.WALK_DOWN]
                            anim?.getKeyFrame(masafrem)
                        }
                    }
                }
                Entiti.Gerak.IDLE -> {
                    texRegion = when (arah) {
                        Entiti.Arah.LEFT -> {
                            val anim = animations[Entiti.AnimationType.WALK_LEFT]
                            anim?.getKeyFrame(0f)
                        }
                        Entiti.Arah.RIGHT -> {
                            val anim = animations[Entiti.AnimationType.WALK_RIGHT]
                            anim?.getKeyFrame(0f)
                        }
                        Entiti.Arah.UP -> {
                            val anim = animations[Entiti.AnimationType.WALK_UP]
                            anim?.getKeyFrame(0f)
                        }
                        Entiti.Arah.DOWN -> {
                            val anim = animations[Entiti.AnimationType.WALK_DOWN]
                            anim?.getKeyFrame(0f)
                        }
                    }
                }
                Entiti.Gerak.IMMOBILE -> {
                    val anim = animations[Entiti.AnimationType.IMMOBILE]
                    texRegion = anim?.getKeyFrame(masafrem) ?: TextureRegion()
                }
            }
        }
    */
    private fun buatAnimasi(config: AnimationConfig): Animation<TextureRegion> {
        val path = config.texturePaths[0]
        val points = config.gridPoints
        val tempohFrem = config.frameDuration
        val texture = Util.getAsetTekstur(path)
        val textureFrames = TextureRegion.split(texture, Entiti.LEBAR_FREM, Entiti.TINGGI_FREM)
        val animationKeyFrames = Array<TextureRegion>(points.size)
        for (point in points) {
            animationKeyFrames.add(textureFrames[point.x][point.y])
        }
        return Animation(tempohFrem, animationKeyFrames, Animation.PlayMode.LOOP)
    }

    private fun loadAnimation(config: AnimationConfig): Animation<TextureRegion> {
        val path1 = config.texturePaths[0]
        val path2 = config.texturePaths[1]
        val points = config.gridPoints
        val tempohFrem = config.frameDuration
        val texture1 = Util.getAsetTekstur(path1)
        val texture2 = Util.getAsetTekstur(path2)
        val frames1 = TextureRegion.split(texture1, Entiti.LEBAR_FREM, Entiti.TINGGI_FREM)
        val frames2 = TextureRegion.split(texture2, Entiti.LEBAR_FREM, Entiti.TINGGI_FREM)

        val animationKeyFrames = Array<TextureRegion>(2)

        animationKeyFrames.add(frames1[points[0].x][points[0].y])
        animationKeyFrames.add(frames2[points[1].x][points[1].y])
        return Animation(tempohFrem, animationKeyFrames, Animation.PlayMode.LOOP)
    }
}