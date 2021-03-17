package com.ulys.skrin

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.ulys.Bourne
import com.ulys.ui.HUD

class SkrinMenu(val game: Bourne) : Screen {

    private val stage = Stage()

    init {
        val table = Table()
        table.setFillParent(true)

        val tajuk = Image(HUD.statusuiTexAtlas.findRegion("bludbourne_title"))
        val baruBtn = TextButton("Game baru", HUD.statusuiSkin)
        val loadBtn = TextButton("Game load", HUD.statusuiSkin)
        val keluarBtn = TextButton("Keluar", HUD.statusuiSkin)

        //Layout
        table.add(tajuk).spaceBottom(75f).row()
        table.add(baruBtn).spaceBottom(10f).row()
        table.add(loadBtn).spaceBottom(60f).row()
        table.add(keluarBtn).spaceBottom(10f).row()

        stage.addActor(table)

        //Listeners
        baruBtn.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                game.screen = game.getSkrin(Bourne.JenisSkrin.SKRIN_BARU)
                return true
            }
        }
        )

        loadBtn.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                game.screen = game.getSkrin(Bourne.JenisSkrin.SKRIN_LOAD)
                return true
            }
        }
        )

        keluarBtn.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                Gdx.app.exit()
                return true
            }
        }
        )

    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.setScreenSize(width, height)
    }

    override fun show() {
        Gdx.input.inputProcessor = stage
    }

    override fun hide() {
        Gdx.input.inputProcessor = null
    }

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {
        stage.dispose()
    }

}
