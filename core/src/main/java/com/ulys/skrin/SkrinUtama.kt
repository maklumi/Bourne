package com.ulys.skrin

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.ulys.Entiti
import com.ulys.PengurusPeta
import com.ulys.Util

class SkrinUtama : Screen {

    private var lebarWindow = 0f
    private var tinggiWindow = 0f
    private var lebarViewcam = 40f
    private var tinggiViewcam = 30f
    private var peta: TiledMap? = null
    private lateinit var kamera: OrthographicCamera
    private lateinit var renderer: OrthogonalTiledMapRenderer
    private val util = Util()

    private lateinit var player: Entiti
    private lateinit var pengurusPeta: PengurusPeta
    private val kpp = PengurusPeta.kpp
    private lateinit var shapeRenderer: ShapeRenderer

    override fun show() {
        setupViewport()

        kamera = OrthographicCamera(lebarWindow, tinggiWindow)
        kamera.setToOrtho(false, lebarViewcam, tinggiViewcam)
        kamera.update()

        pengurusPeta = PengurusPeta(util)
        pengurusPeta.setupPeta(PengurusPeta.TOWN)
        peta = pengurusPeta.peta

        renderer = OrthogonalTiledMapRenderer(peta, kpp)
        renderer.setView(kamera)

        player = Entiti(util, pengurusPeta)
        player.komponenFizik.updatePositionsAndBound(pengurusPeta.posisiMula)

        shapeRenderer = ShapeRenderer()
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // lock and center camera to player's pos
        kamera.position.set(player.komponenFizik.pos.x, player.komponenFizik.pos.y, 0f)
        kamera.update()

        // peta
        renderer.setView(kamera)
        renderer.render(intArrayOf(1))

        // player
        player.kemaskini(delta, renderer.batch)

        cekMasukPortalLayer(player.komponenFizik.nextRect)

        lukisDebug()
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {
        player.komponenGrafik.dispose()
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

    private fun cekMasukPortalLayer(rect: Rectangle): Boolean {
        val layer = pengurusPeta.portalLayer
        for (i in 0 until layer.objects.count) {
            val obj = layer.objects[i]
            if (obj is RectangleMapObject && rect.overlaps(obj.rectangle)) {
                val namaPeta = obj.name
                pengurusPeta.cacheTempatSpawnHampir(player.komponenFizik.pos)
                pengurusPeta.setupPeta(namaPeta)
                peta = pengurusPeta.peta
                renderer.map = peta

                if (namaPeta == PengurusPeta.TOP_WORLD) {
                    kamera = OrthographicCamera(75f, 75f)
                    kamera.setToOrtho(false, 75f, 75f)
                } else if (namaPeta == PengurusPeta.TOWN) {
                    kamera = OrthographicCamera(40f, 30f)
                    kamera.setToOrtho(false, 40f, 30f)
                }
                kamera.update()

                player.komponenFizik.updatePositionsAndBound(pengurusPeta.posisiMula)
                Gdx.app.debug("SkrinUtama123", "Masuk portal $namaPeta ${player.komponenFizik.pos}")
                return true
            }
        }
        return false
    }

    private fun lukisDebug() {
        shapeRenderer.apply {
            projectionMatrix = kamera.combined
            // player
            begin(ShapeRenderer.ShapeType.Filled)
            color = Color.GOLD
            val r = player.komponenFizik.nextRect
            rect(r.x * kpp, r.y * kpp, r.width * kpp, r.height * kpp)
            end()
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

}