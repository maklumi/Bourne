package com.ulys.skrin

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.ulys.Entiti
import com.ulys.KawalPemain
import com.ulys.Util

class SkrinUtama : Screen {

    private var lebarWindow = 0f
    private var tinggiWindow = 0f
    private var lebarViewcam = 10f
    private var tinggiViewcam = 10f
    private var peta: TiledMap? = null
    private lateinit var kamera: OrthographicCamera
    private lateinit var renderer: OrthogonalTiledMapRenderer
    private val util = Util()
    private val path = "maps/town.tmx"
    private lateinit var player: Entiti
    private lateinit var kawalPemain: KawalPemain

    override fun show() {
        setupViewport()
        muatAsetPeta()

        kamera = OrthographicCamera(lebarWindow, tinggiWindow)
        kamera.setToOrtho(false, lebarViewcam, tinggiViewcam)
        kamera.update()

        renderer = OrthogonalTiledMapRenderer(peta, kpp)
        renderer.setView(kamera)

        player = Entiti(util)

        kawalPemain = KawalPemain(player)
        Gdx.input.inputProcessor = kawalPemain
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // lock and center camera to player's pos
        kamera.position.set(player.pos.x, player.pos.y, 0f)
        kamera.update()

        kawalPemain.kemaskini(delta)
        player.kemaskini(delta)
        if (!akanBerlagaDenganLayer(player.nextRect)) {
            player.setCalculatedPosAsCurrent()
        }

        renderer.setView(kamera)
        renderer.render()
        //player
        renderer.batch.begin()
        // guna world unit iaitu kaki
        renderer.batch.draw(player.texRegion, player.pos.x, player.pos.y, 1f, 1f)
        renderer.batch.end()
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {
        util.dispose(path)
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
        Gdx.app.debug("SkrinUtama", "Window: $lebarWindow,$tinggiWindow")
        Gdx.app.debug("SkrinUtama", "Viewport: $lebarViewcam,$tinggiViewcam")

    }

    private fun muatAsetPeta() {
        util.muatAsetPeta(path)
        if (util.asetDimuat(path)) {
            peta = util.getAsetPeta(path)
        } else {
            Gdx.app.debug("SkrinUtama", "Peta tak diperolehi: $path")
        }
    }

    private fun akanBerlagaDenganLayer(rect: Rectangle): Boolean {
        val layer = peta?.layers?.get(MAP_COLLISION_LAYER)
        return if (layer != null) akanBerlaga(rect, layer) else false
    }

    private fun akanBerlaga(rect: Rectangle, layer: MapLayer): Boolean {
        // convert bound dalam kaki unit ke pixel
        rect.setPosition(rect.x / kpp, rect.y / kpp)
        for (i in 0 until layer.objects.count) {
            val obj = layer.objects[i]
            if (obj is RectangleMapObject && rect.overlaps(obj.rectangle)) {
                return true
            }
        }
        return false
    }

    companion object {
        const val kpp = 1 / 16f // 1 kaki = 16 piksel
        private const val MAP_COLLISION_LAYER = "MAP_COLLISION_LAYER"
    }
}