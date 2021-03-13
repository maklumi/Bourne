package com.ulys

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.utils.Array
import com.ulys.Entiti.AnimationType

class Konfigurasi {

    var animationConfig: Array<AnimationConfig> = Array()

    class AnimationConfig {
        var animationType: AnimationType = AnimationType.IDLE
        var texturePaths: Array<String> = Array()
        var gridPoints: Array<GridPoint2> = Array()
    }
}