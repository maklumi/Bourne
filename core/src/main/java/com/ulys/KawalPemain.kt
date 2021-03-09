package com.ulys

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor

class KawalPemain(val pemain: Entiti) : InputProcessor {

    private enum class Kekunci { KIRI, KANAN, ATAS, BAWAH, QUIT }

    private val kekunci = HashMap<Kekunci, Boolean>().also {
        it[Kekunci.KIRI] = false
        it[Kekunci.KANAN] = false
        it[Kekunci.ATAS] = false
        it[Kekunci.BAWAH] = false
        it[Kekunci.QUIT] = false
    }

    fun kemaskini(delta: Float) {
        prosesInput(delta)
    }

    private fun prosesInput(delta: Float) {
        when {
            kekunci[Kekunci.KIRI] == true -> {
                pemain.arah = Entiti.Arah.KIRI
                pemain.kiraPosisi(delta)
            }
            kekunci[Kekunci.KANAN] == true -> {
                pemain.arah = Entiti.Arah.KANAN
                pemain.kiraPosisi(delta)
            }
            kekunci[Kekunci.ATAS] == true -> {
                pemain.arah = Entiti.Arah.ATAS
                pemain.kiraPosisi(delta)
            }
            kekunci[Kekunci.BAWAH] == true -> {
                pemain.arah = Entiti.Arah.BAWAH
                pemain.kiraPosisi(delta)
            }
            kekunci[Kekunci.QUIT] == true -> {
                Gdx.app.exit()
            }
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
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false

    override fun scrolled(amountX: Float, amountY: Float): Boolean = false
}