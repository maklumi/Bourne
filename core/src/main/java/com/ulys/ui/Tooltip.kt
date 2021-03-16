package com.ulys.ui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Window

class Tooltip(skin: Skin) : Window("", skin) {

    private val keterangan = Label("", skin, "inventory-item-count")

    init {
        add(keterangan)
    }

    fun bolehTunjuk(slot: Slot, visible: Boolean) {
        super.setVisible(visible)

        if (!slot.adaBarang()) {
            super.setVisible(false)
        }
    }

    fun infoTerkiniBarang(inventorySlot: Slot) {
        if (inventorySlot.adaBarang()) {
            keterangan.setText(inventorySlot.getTopItem()!!.itemShortDescription)
            pack()
        } else {
            keterangan.setText("")
        }
    }
}