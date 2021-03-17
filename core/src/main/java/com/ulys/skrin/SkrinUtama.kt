package com.ulys.skrin

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.ulys.*
import com.ulys.ui.HUD

class SkrinUtama : Screen {

    private var lebarWindow = 0f
    private var tinggiWindow = 0f
    private var lebarViewcam = 40f
    private var tinggiViewcam = 30f
    private var peta: TiledMap? = null
    private lateinit var hud: HUD
    private lateinit var hudKamera: OrthographicCamera
    private lateinit var kamera: OrthographicCamera
    private lateinit var renderer: OrthogonalTiledMapRenderer

    private lateinit var player: Entiti
    private lateinit var pengurusPeta: PengurusPeta
    private val kpp = Peta.kpp
    private lateinit var shapeRenderer: ShapeRenderer

    enum class GameState { RUNNING, PAUSED, }

    override fun show() {
        setupViewport()

        kamera = OrthographicCamera(lebarWindow, tinggiWindow)
        kamera.setToOrtho(false, lebarViewcam, tinggiViewcam)
        kamera.update()

        pengurusPeta = PengurusPeta()
        pengurusPeta.setupPeta(PengeluarPeta.JenisPeta.TOWN)
        pengurusPeta.kamera = this.kamera
        peta = pengurusPeta.tiledMap

        renderer = OrthogonalTiledMapRenderer(peta, kpp)
        renderer.setView(pengurusPeta.kamera)

        player = Pengeluar.Pemain.get()
        pengurusPeta.entitiPemain.add(player)
        player.posMesej(Penerima.Mesej.POS_MULA, toJson(pengurusPeta.posisiMula))

        shapeRenderer = ShapeRenderer()

        hudKamera = OrthographicCamera()
        hudKamera.setToOrtho(false, lebarWindow, tinggiWindow)
        hud = HUD(hudKamera)
        hud.isiInventori(player.konfigurasi.inventory)

        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(hud.stage)
        multiplexer.addProcessor(player.komponenInput)
        Gdx.input.inputProcessor = multiplexer
    }

    override fun render(delta: Float) {
        if (gameState == GameState.PAUSED) {
            player.komponenInput.kemaskini(delta, player)
            return
        }
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if (pengurusPeta.berpindah) {
            pengurusPeta.berpindah = false
            renderer.map = pengurusPeta.tiledMap
            player.posMesej(Penerima.Mesej.POS_MULA, toJson(pengurusPeta.posisiMula))
        }
        renderer.setView(pengurusPeta.kamera)
        renderer.render(intArrayOf(1))

//        pengurusPeta.updateMapEntities(delta, renderer.batch, pengurusPeta)

        player.kemaskini(delta, renderer.batch, pengurusPeta)

        lukisDebug()

        hud.render(delta)
    }

    override fun resize(width: Int, height: Int) {
        hud.resize(width, height)
    }

    override fun pause() {
        gameState = GameState.PAUSED
    }

    override fun resume() {
        gameState = GameState.RUNNING
    }

    override fun hide() {}

    override fun dispose() {
        player.dispose()
        Gdx.input.inputProcessor = null
    }

    private fun setupViewport() {
        lebarWindow = Gdx.graphics.width.toFloat()
        tinggiWindow = Gdx.graphics.height.toFloat()
        val aspekWindow = lebarWindow / tinggiWindow
        val aspekViewcam = lebarViewcam / tinggiViewcam
        if (aspekWindow >= aspekViewcam) {
            lebarViewcam = tinggiViewcam * aspekWindow
        } else {
            tinggiViewcam = lebarViewcam * aspekWindow
        }
//        Gdx.app.debug("SkrinUtama", "Window: $lebarWindow,$tinggiWindow")
//        Gdx.app.debug("SkrinUtama", "Viewport: $lebarViewcam,$tinggiViewcam")
    }


    private fun lukisDebug() {
        shapeRenderer.apply {
            projectionMatrix = pengurusPeta.kamera.combined
            // layers
            begin(ShapeRenderer.ShapeType.Line)
            val layer = pengurusPeta.spawnsLayer
            for (i in 0 until layer.objects.count) {
                val obj = layer.objects[i]
                color = Color.CORAL
                val s = (obj as RectangleMapObject).rectangle
                rect(s.x * kpp, s.y * kpp, s.width * kpp, s.height * kpp)
            }

            val layer2 = pengurusPeta.portalLayer
            for (i in 0 until layer2.objects.count) {
                val obj = layer2.objects[i]
                color = Color.GREEN
                val s2 = (obj as RectangleMapObject).rectangle
                rect(s2.x * kpp, s2.y * kpp, s2.width * kpp, s2.height * kpp)
            }

            val layer3 = pengurusPeta.collisionLayer
            for (i in 0 until layer3.objects.count) {
                val obj = layer3.objects[i]
                color = Color.FOREST
                val s2 = (obj as RectangleMapObject).rectangle
                rect(s2.x * kpp, s2.y * kpp, s2.width * kpp, s2.height * kpp)
            }
            end()
        }
    }

    companion object {
        var gameState: GameState = GameState.RUNNING
            set(gameState) {
                field = when (gameState) {
                    GameState.RUNNING -> gameState
                    GameState.PAUSED -> {
                        if (field == GameState.PAUSED)
                            GameState.RUNNING
                        else
                            GameState.PAUSED
                    }
                }
            }
    }

}