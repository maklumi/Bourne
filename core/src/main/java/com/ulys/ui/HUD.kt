package com.ulys.ui

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport

class HUD(camera: Camera) : Screen {

    private val viewport = ScreenViewport(camera)
    private val statusUI = StatusUI()
    private val stage = Stage(viewport).also { it.addActor(statusUI) }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()
    }

    override fun show() {}

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}
}