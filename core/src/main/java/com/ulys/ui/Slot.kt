package com.ulys.ui

import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array

class Slot : Stack() {

    //All slots have this default image
    private val image = Image(NinePatch(HUD.statusuiTexAtlas.createPatch("dialog")))
    private val kiraan: Int get() = children.size - 2
    private val label = Label("0", HUD.statusuiSkin, "inventory-item-count")

    init {
        add(image)
        add(label)
        label.setAlignment(Align.bottomRight)
        paparKiraan()
    }

    override fun add(actor: Actor) {
        super.add(actor)
        if (actor != image && actor != label) {
            paparKiraan()
        }
    }

    fun paparKiraan() {
        label.setText(kiraan.toString())
        label.isVisible = kiraan >= 1
    }

    fun getTopItem(): Barang? {
        return if (hasChildren() && children.size > 2) {
            children.peek() as Barang
        } else null
    }

    fun adaBarang(): Boolean {
        return children.size > 2
    }

    fun tambahBarangan(array: Array<Actor>) {
        for (aktor in array) {
            add(aktor)
        }
    }

    fun popBarangan(): Array<Actor> {
        val semua = Array<Actor>()
        for (i in 2 until children.size) {
            semua.add(children.pop())
            paparKiraan()
        }
        return semua
    }

    companion object {
        fun tukarTempat(sumber: Slot, target: Slot, dragActor: Actor) {
            val senaraiSumber = sumber.popBarangan()
            senaraiSumber.add(dragActor)
            val senaraiTarget = target.popBarangan()
            sumber.tambahBarangan(senaraiTarget)
            target.tambahBarangan(senaraiSumber)
        }
    }

}
