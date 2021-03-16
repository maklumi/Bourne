package com.ulys.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.Scaling
import com.ulys.j
import java.util.*

object FaktoriBarang {

    private val jadualBarang = Hashtable<Barang.ItemTypeID, Barang>().also {
        val senarai = j.fromJson(ArrayList::class.java, Gdx.files.internal("scripts/inventory_items.json"))
        for (jsonvalue in senarai) {
            val barang = j.readValue(Barang::class.java, jsonvalue as JsonValue)
            it[barang.itemTypeID] = barang
        }
    }

    fun buatBarang(itemTypeID: Barang.ItemTypeID): Barang {
        val barang = jadualBarang[itemTypeID] ?: Barang()
        val klon = buatClone(barang)
        klon.drawable = TextureRegionDrawable(HUD.itemsTexAtlas.findRegion(klon.itemTypeID?.name))
        klon.name = UUID.randomUUID().toString()
        klon.setScaling(Scaling.none)
        return klon
    }

    private fun buatClone(barang: Barang): Barang {
        val serialize = j.toJson(barang)
        return j.fromJson(Barang::class.java, serialize)
    }
}