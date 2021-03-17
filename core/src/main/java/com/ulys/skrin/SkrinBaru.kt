package com.ulys.skrin

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.ulys.Bourne
import com.ulys.sejarah.Penyelia
import com.ulys.ui.HUD

class SkrinBaru(val game: Bourne) : Screen {

    private val stage = Stage()

    init {
        val profileName = Label("Taip nama fail baru: ", HUD.statusuiSkin)
        val profileText = TextField("", HUD.statusuiSkin, "inventory")
        profileText.maxLength = 30

        val overwriteDialog = Dialog("Overwrite", HUD.statusuiSkin, "solidbackground")
        val overwriteLabel = Label("Pasti untuk overwrite fail ni?", HUD.statusuiSkin)
        val cancelButton = TextButton("Kensel", HUD.statusuiSkin, "inventory")
        val overwriteButton = TextButton("Overwrite", HUD.statusuiSkin, "inventory")
        overwriteDialog.setKeepWithinStage(true)
        overwriteDialog.isModal = true
        overwriteDialog.isMovable = false
        overwriteDialog.text(overwriteLabel)

        val startButton = TextButton("Mula", HUD.statusuiSkin)
        val backButton = TextButton("Back", HUD.statusuiSkin)

        //Layout
        overwriteDialog.row()
        overwriteDialog.button(cancelButton).bottom().left()
        overwriteDialog.button(overwriteButton).bottom().right()

        val topTable = Table()
        topTable.setFillParent(true)
        topTable.add(profileName).center()
        topTable.add(profileText).center()

        val bottomTable = Table()
        bottomTable.height = startButton.height * 4
        bottomTable.width = Gdx.graphics.width.toFloat()
        bottomTable.top()
        bottomTable.add(backButton).padRight(50f)
        bottomTable.add(startButton)

        stage.addActor(topTable)
        stage.addActor(bottomTable)

        //Listeners
        cancelButton.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                overwriteDialog.hide()
                return true
            }
        }
        )

        overwriteButton.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                val messageText = profileText.text
                Penyelia.writeProfileToStorage(messageText, "", true)
                Penyelia.setNamaProfail(messageText)
                Penyelia.saveProfile()
                Penyelia.loadProfile()
                game.screen = game.getSkrin(Bourne.JenisSkrin.SKRIN_UTAMA)
                return true
            }
        }
        )

        startButton.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                val messageText = profileText.text
                //check to see if the current profile matches one that already exists
                val exists = Penyelia.adaNamafail(messageText)
                if (exists) {
                    //Pop up dialog for Overwrite
                    overwriteDialog.show(stage)
                } else {
                    Penyelia.writeProfileToStorage(messageText, "", false)
                    Penyelia.setNamaProfail(messageText)
                    Penyelia.saveProfile()
                    Penyelia.loadProfile()
                    game.screen = game.getSkrin(Bourne.JenisSkrin.SKRIN_UTAMA)
                }
                return true
            }
        }
        )

        backButton.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                game.screen = game.getSkrin(Bourne.JenisSkrin.SKRIN_MENU)
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
