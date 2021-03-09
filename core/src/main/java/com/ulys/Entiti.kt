package com.ulys

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion

open class Entiti(util: Util) {

    private val lebarFrem = 16
    private val tinggiFrem = 16

    private val spritePath = "maps/Characters/Warrior.png"
    private val tekstur = util.getAsetTekstur(spritePath)
    private val texRegions = TextureRegion.split(tekstur, lebarFrem, tinggiFrem)
    val frameSprite = Sprite(texRegions[0][0], 0, 0, lebarFrem, tinggiFrem)


}