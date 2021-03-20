package com.ulys.ui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Window

class Tooltip(skin: Skin) : Window("", skin) {

    private val keterangan = Label("", skin, "inventory-item-count")

    init {
        add(keterangan)
        isVisible = false
    }

    fun bolehTunjuk(slot: Slot, visible: Boolean) {
        super.setVisible(visible)

        if (!slot.adaBarang()) {
            super.setVisible(false)
        }
    }

    fun infoTerkiniBarang(inventorySlot: Slot) {
        if (inventorySlot.adaBarang()) {
            val string = StringBuilder()
            string.append(inventorySlot.getTopItem()?.itemShortDescription)
            string.append(System.getProperty("line.separator"))
            string.append(String.format("Original Value: %s GP", inventorySlot.getTopItem()?.itemValue))
            string.append(System.getProperty("line.separator"))
            string.append(String.format("Trade Value: %s GP", inventorySlot.getTopItem()?.hargaJual()))
            keterangan.setText(string)
            pack()
        } else {
            keterangan.setText("")
        }
    }
}