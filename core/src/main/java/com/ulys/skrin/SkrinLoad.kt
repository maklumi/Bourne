package com.ulys.skrin

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.List as GdxList
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.ulys.Bourne
import com.ulys.sejarah.Penyelia
import com.ulys.ui.HUD

class SkrinLoad(val game: Bourne) : Screen {

    private val stage = Stage()

    init {
        val loadButton = TextButton("Load", HUD.statusuiSkin)
        val backButton = TextButton("Back", HUD.statusuiSkin)

//        ProfileManager.instance.storeAllProfiles()
        val array = Penyelia.getSenaraiNamafail()
        val listItems = GdxList<String>(HUD.statusuiSkin, "inventory")
        listItems.setItems(array)

        val scrollPane = ScrollPane(listItems)
        scrollPane.setOverscroll(false, false)
        scrollPane.setScrollingDisabled(true, false)
        scrollPane.setScrollbarsOnTop(true)
        scrollPane.fadeScrollBars = false

        //Layout
        val table = Table()
        val bottomTable = Table()
        table.center()
        table.setFillParent(true)
        table.padBottom(loadButton.height)
        table.add(scrollPane).center()

        bottomTable.height = loadButton.height * 4
        bottomTable.width = Gdx.graphics.width.toFloat()
        bottomTable.top()
        bottomTable.add(backButton).padRight(50f)
        bottomTable.add(loadButton)

        stage.addActor(table)
        stage.addActor(bottomTable)

        //Listeners
        backButton.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                game.screen = game.getSkrin(Bourne.JenisSkrin.SKRIN_MENU)
                return true
            }
        }
        )

        loadButton.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                val fileName = listItems.selected.toString()
                val file = Penyelia.getFileHandleUntuk(fileName)
                if (file != null) {
                    Penyelia.setNamaProfail(fileName)
                    Penyelia.loadProfile(fileName)
                    game.screen = game.getSkrin(Bourne.JenisSkrin.SKRIN_UTAMA)
                }
                return true
            }
        }
        )
    }

    override fun render(delta: Float) {
        if (delta == 0f) return

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
        stage.clear()
        stage.dispose()
    }

}
