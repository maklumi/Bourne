package com.ulys.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport

class HUD(camera: Camera) : Screen {

    companion object {
        val statusuiTexAtlas = TextureAtlas("skins/statusui.atlas")
        val statusuiSkin = Skin(Gdx.files.internal("skins/statusui.json"), statusuiTexAtlas)
        val itemsTexAtlas = TextureAtlas("skins/items.atlas")
    }

    private val statusUI = StatusUI(statusuiSkin, statusuiTexAtlas)
    private val inventoryUI = InventoriUI(statusuiSkin, statusuiTexAtlas)
    private val viewport = ScreenViewport(camera)
    val stage = Stage(viewport).also {
        it.addActor(statusUI)
        it.addActor(inventoryUI)
        inventoryUI.isMovable = true
    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()
    }

    override fun show() {}

    override fun resize(width: Int, height: Int) {
//        stage.viewport.update(width, height, true)
        inventoryUI.setPosition((width - inventoryUI.width) / 2, (height - inventoryUI.height) / 2)
    }

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {
        stage.dispose()
    }
}