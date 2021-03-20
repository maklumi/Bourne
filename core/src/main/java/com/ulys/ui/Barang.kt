package com.ulys.ui

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.ulys.ui.Barang.Sifat.STACKABLE

class Barang(
    region: TextureRegion? = null,
    var itemAttributes: Int = 0,    // sifat
    var itemUseType: Int = 0,          // fungsi
    var itemTypeID: ItemTypeID? = null
) : Image(region) {

    var itemShortDescription: String? = null
    var itemValue: Int = 0

    enum class ItemTypeID {
        ARMOR01, ARMOR02, ARMOR03, ARMOR04, ARMOR05,
        BOOTS01, BOOTS02, BOOTS03, BOOTS04, BOOTS05,
        HELMET01, HELMET02, HELMET03, HELMET04, HELMET05,
        SHIELD01, SHIELD02, SHIELD03, SHIELD04, SHIELD05,
        WANDS01, WANDS02, WANDS03, WANDS04, WANDS05,
        WEAPON01, WEAPON02, WEAPON03, WEAPON04, WEAPON05,
        POTIONS01, POTIONS02, POTIONS03,
        SCROLL01, SCROLL02, SCROLL03
    }

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
        return itemTypeID == calon.itemTypeID
    }

    fun hargaJual(): Int {
        //For now, we will set the trade in value of items at about one third their original value
//        return MathUtils.floor(itemValue * .33f) + 2
        return itemValue
    }
}