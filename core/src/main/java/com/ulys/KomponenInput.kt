package com.ulys

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Vector3

class KomponenInput(private val pemain: Entiti) : InputProcessor {

    private enum class Kekunci { KIRI, KANAN, ATAS, BAWAH, QUIT }

    private val kekunci = HashMap<Kekunci, Boolean>().also {
        it[Kekunci.KIRI] = false
        it[Kekunci.KANAN] = false
        it[Kekunci.ATAS] = false
        it[Kekunci.BAWAH] = false
        it[Kekunci.QUIT] = false
    }

    enum class Tikus { PILIH, LAKSANA }

    private val tetikus = hashMapOf(Tikus.PILIH to false, Tikus.LAKSANA to false)
    private val koordinatTikus = Vector3()

    init {
        Gdx.input.inputProcessor = this
    }

    fun kemaskini(delta: Float) {
        prosesInput(delta)
    }

    private fun prosesInput(delta: Float) {
        if (delta < 0.008f) return
        when {
            kekunci[Kekunci.KIRI] == true -> {
                pemain.arah = Entiti.Arah.KIRI
                pemain.gerak = Entiti.Gerak.JALAN
            }
            kekunci[Kekunci.KANAN] == true -> {
                pemain.arah = Entiti.Arah.KANAN
                pemain.gerak = Entiti.Gerak.JALAN
            }
            kekunci[Kekunci.ATAS] == true -> {
                pemain.arah = Entiti.Arah.ATAS
                pemain.gerak = Entiti.Gerak.JALAN
            }
            kekunci[Kekunci.BAWAH] == true -> {
                pemain.arah = Entiti.Arah.BAWAH
                pemain.gerak = Entiti.Gerak.JALAN
            }
            kekunci[Kekunci.QUIT] == true -> {
                Gdx.app.exit()
            }
            else -> pemain.gerak = Entiti.Gerak.DIAM
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.LEFT, Input.Keys.A -> kekunci[Kekunci.KIRI] = true
            Input.Keys.RIGHT, Input.Keys.D -> kekunci[Kekunci.KANAN] = true
            Input.Keys.UP, Input.Keys.W -> kekunci[Kekunci.ATAS] = true
            Input.Keys.DOWN, Input.Keys.S -> kekunci[Kekunci.BAWAH] = true
            Input.Keys.Q -> kekunci[Kekunci.QUIT] = true
        }
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.LEFT, Input.Keys.A -> kekunci[Kekunci.KIRI] = false
            Input.Keys.RIGHT, Input.Keys.D -> kekunci[Kekunci.KANAN] = false
            Input.Keys.UP, Input.Keys.W -> kekunci[Kekunci.ATAS] = false
            Input.Keys.DOWN, Input.Keys.S -> kekunci[Kekunci.BAWAH] = false
            Input.Keys.Q -> kekunci[Kekunci.QUIT] = false
        }
        return true
    }

    override fun keyTyped(character: Char): Boolean = false

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        when (button) {
            Input.Buttons.LEFT -> {
                koordinatTikus.set(screenX.toFloat(), screenY.toFloat(), 0f)
                tetikus[Tikus.PILIH] = true
            }
            Input.Buttons.RIGHT -> {
                koordinatTikus.set(screenX.toFloat(), screenY.toFloat(), 0f)
                tetikus[Tikus.LAKSANA] = true
            }
        }
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        when (button) {
            Input.Buttons.LEFT -> {
                tetikus[Tikus.PILIH] = false
            }
            Input.Buttons.RIGHT -> {
                tetikus[Tikus.LAKSANA] = false
            }
        }
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false

    override fun scrolled(amountX: Float, amountY: Float): Boolean = false
}