package com.ulys

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

open class Entiti(util: Util) {

    private val lebarFrem = 16
    private val tinggiFrem = 16

    private val spritePath = "maps/Characters/Warrior.png"
    private val tekstur = util.getAsetTekstur(spritePath)
    private val texRegions = TextureRegion.split(tekstur, lebarFrem, tinggiFrem)
    val frameSprite = Sprite(texRegions[0][0], 0, 0, lebarFrem, tinggiFrem)

    var pos = Vector2(1f, 1f)
    val laju = Vector2(5f, 5f)

    enum class Arah { KIRI, KANAN, ATAS, BAWAH }

    var arah = Arah.BAWAH

    fun kiraPosisi(delta: Float) {
        laju.scl(delta)
        when (arah) {
            Arah.KIRI -> pos.x -= laju.x
            Arah.KANAN -> pos.x += laju.x
            Arah.ATAS -> pos.y += laju.y
            Arah.BAWAH -> pos.y -= laju.y
        }
        laju.scl(1 / delta)
    }
}