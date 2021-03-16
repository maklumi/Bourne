package com.ulys.ui

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.ulys.ui.Barang.Sifat.STACKABLE

class Barang(
    region: TextureRegion,
    val itemAttributes: Int,    // sifat
    val itemType: Int,          // fungsi
    val itemID: String
) : Image(region) {

    enum class Sifat(val bit: Int) {
        CONSUMABLE(1),
        WEARABLE(2),
        STACKABLE(4),
    }

    enum class Fungsi(val bit: Int) {
        RESTORE_HEALTH(1),
        RESTORE_MP(2),
        DAMAGE(4),
        WEAPON_ONEHAND(8),
        WEAPON_TWOHAND(16),
        WAND_ONEHAND(32),
        WAND_TWOHAND(64),
        ARMOR_SHIELD(128),
        ARMOR_HELMET(256),
        ARMOR_CHEST(512),
        ARMOR_FEET(1024),
    }

    fun isStackable(): Boolean {
        return itemAttributes.and(STACKABLE.bit) == STACKABLE.bit
    }

    fun isSameItemType(calon: Barang): Boolean {
        return itemID == calon.itemID
    }
}