package com.ulys

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.ulys.skrin.SkrinUtama

class Bourne : Game() {

    val skrinUtama: SkrinUtama = SkrinUtama()

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        setScreen(skrinUtama)
    }

    override fun dispose() {
        skrinUtama.dispose()
    }
}