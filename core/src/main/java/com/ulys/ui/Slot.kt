package com.ulys.ui

import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.Align

class Slot : Stack() {

    //All slots have this default image
    private val image = Image(NinePatch(HUD.statusuiTexAtlas.createPatch("dialog")))
    private var kiraan = 0
    private val label = Label(kiraan.toString(), HUD.statusuiSkin, "inventory-item-count")

    init {
        add(image)

        label.setAlignment(Align.bottomRight)
        paparKiraan()
        add(label)
    }

    fun tolakKiraan() {
        kiraan--
        label.setText(kiraan.toString())
        paparKiraan()
    }

    fun tambahKiraan() {
        kiraan++
        label.setText(kiraan.toString())
        paparKiraan()
    }

    override fun add(actor: Actor) {
        super.add(actor)
        if (actor != image && actor != label) {
            tambahKiraan()
        }
    }

    private fun paparKiraan() {
        label.isVisible = kiraan >= 1
    }

    fun getTopItem(): Actor? {
        return if (hasChildren()) children.peek() else null
    }
}
