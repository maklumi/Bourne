package com.ulys.ui

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image

class Barang(region: TextureRegion, var itemAttributes: Int, var itemID: String) : Image(region) {

    companion object {
        val CONSUMABLE = 0x01
        val WEARABLE = 0x02
        val STACKABLE = 0x04
    }

    fun isStackable(): Boolean {
        return itemAttributes.and(STACKABLE) == STACKABLE
    }

    fun isSameItemType(calon: Barang): Boolean {
        return itemID == calon.itemID
    }
}