package com.ulys

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.utils.Array
import com.ulys.Entiti.AnimationType

class Konfigurasi {

    var entityID: String = "tiada id"
    var state: Entiti.Gerak = Entiti.Gerak.IDLE
    var direction: Entiti.Arah = Entiti.Arah.DOWN
    var animationConfig: Array<AnimationConfig> = Array()

    class AnimationConfig {
        var frameDuration: Float = 1f
        var animationType: AnimationType = AnimationType.IDLE
        var texturePaths: Array<String> = Array()
        var gridPoints: Array<GridPoint2> = Array()
    }
}