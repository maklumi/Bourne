package com.ulys.skrin

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.ulys.Entiti
import com.ulys.KawalPemain
import com.ulys.Util

class SkrinUtama : Screen {

    var lebarWindow = 0f
    var tinggiWindow = 0f
    var lebarViewcam = 10f
    var tinggiViewcam = 10f
    val kpp = 1 / 16f // 1 kaki = 16 piksel
    var peta: TiledMap? = null
    lateinit var kamera: OrthographicCamera
    lateinit var renderer: OrthogonalTiledMapRenderer
    private val util = Util()
    private val path = "sprites/tmx/Town.tmx"
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

        kawalPemain.kemaskini(delta)
        player.kemaskini(delta)

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
}