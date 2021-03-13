package com.ulys.tests

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.utils.JsonWriter
import com.ulys.Entiti
import com.ulys.Konfigurasi
import com.ulys.j
import com.badlogic.gdx.utils.Array as GdxArray

object KonfigurasiTest {

    @JvmStatic
    fun main(args: Array<String>) {
        val animIdle = Konfigurasi.AnimationConfig()
        animIdle.animationType = Entiti.AnimationType.IDLE
        animIdle.texturePaths = GdxArray()
        animIdle.texturePaths.add("sprites/characters/Warrior.png")
        animIdle.gridPoints = GdxArray()
        animIdle.gridPoints.add(GridPoint2(0, 0))
        animIdle.gridPoints.add(GridPoint2(0, 1))
        animIdle.gridPoints.add(GridPoint2(0, 2))
        animIdle.gridPoints.add(GridPoint2(0, 3))

        val konfigurasi = Konfigurasi()
        konfigurasi.animationConfig = GdxArray()
        konfigurasi.animationConfig.add(animIdle)
        j.setUsePrototypes(false)
        j.setOutputType(JsonWriter.OutputType.minimal)
        print(j.prettyPrint(konfigurasi))
    }

}