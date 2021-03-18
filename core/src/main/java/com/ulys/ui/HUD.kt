package com.ulys.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.ulys.Entiti
import com.ulys.Konfigurasi
import com.ulys.sejarah.Penyelia.getProp
import com.ulys.sejarah.Penyelia.setProp
import com.ulys.sejarah.Profil
import com.ulys.sejarah.Profil.ProfileEvent
import com.ulys.sejarah.Profil.ProfileEvent.PROFILE_LOADED
import com.ulys.sejarah.Profil.ProfileEvent.SAVING_PROFILE
import com.ulys.ui.Bualan.UIEvent.*

class HUD(camera: Camera, private val player: Entiti) : Screen, Profil, Bualan {

    companion object {
        val statusuiTexAtlas = TextureAtlas("skins/statusui.atlas")
        val statusuiSkin = Skin(Gdx.files.internal("skins/statusui.json"), statusuiTexAtlas)
        val itemsTexAtlas = TextureAtlas("skins/items.atlas")
    }

    private val statusUI = StatusUI(statusuiSkin, statusuiTexAtlas)
    private val inventoryUI = InventoriUI(statusuiSkin, statusuiTexAtlas)
    private val perbualanUI = PerbualanUI()
    private val viewport = ScreenViewport(camera)
    val stage = Stage(viewport).also {
        it.addActor(statusUI)
        it.addActor(inventoryUI)
        it.addActor(inventoryUI.tooltip)
        it.addActor(perbualanUI)
    }

    init {
        inventoryUI.isMovable = true
        inventoryUI.isVisible = false
        statusUI.suisInventori.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                inventoryUI.isVisible = !inventoryUI.isVisible
            }
        })
        perbualanUI.also {
            it.isMovable = true
            it.isVisible = false
            it.setPosition(stage.width / 2f, 0f)
            it.setSize(stage.width / 2f, stage.height / 2f)
        }
        perbualanUI.closeButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                perbualanUI.isVisible = false
            }
        })
    }

    override fun onTerima(event: ProfileEvent) {
        when (event) {
            PROFILE_LOADED -> {
                val inventory = getProp<Array<LokasiBarang>>("playerInventory") ?: Array()
                if (!inventory.isEmpty) {
                    inventoryUI.isiInventori(inventoryUI.inventoryTable, inventory)
                } else {
                    //add default items if nothing is found
                    val items: Array<Barang.ItemTypeID> = player.konfigurasi.inventory
                    val itemLocations = Array<LokasiBarang>()
                    for (i in 0 until items.size) {
                        itemLocations.add(LokasiBarang(i, items.get(i).toString(), 1))
                    }
                    inventoryUI.isiInventori(inventoryUI.inventoryTable, itemLocations)
                }

                val equipInventory = getProp<Array<LokasiBarang>>("playerEquipInventory") ?: Array()
                if (!equipInventory.isEmpty) {
                    inventoryUI.isiInventori(inventoryUI.equipSlots, equipInventory)
                }
            }
            SAVING_PROFILE -> {
                setProp("playerInventory", inventoryUI.getInventory(inventoryUI.inventoryTable))
                setProp("playerEquipInventory", inventoryUI.getInventory(inventoryUI.equipSlots))
            }
        }
    }

    override fun onBual(k: Konfigurasi, event: Bualan.UIEvent) {
        when (event) {
            LOAD_CONVERSATION -> {
                perbualanUI.loadConversation(k)
            }
            SHOW_CONVERSATION -> {
                if (k.entityID == perbualanUI.currentEntityID) {
                    perbualanUI.isVisible = true
                }
            }
            HIDE_CONVERSATION -> {
                if (k.entityID == perbualanUI.currentEntityID) {
                    perbualanUI.isVisible = false
                }
            }
        }
    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()
    }

    override fun show() {}

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
        inventoryUI.setPosition(statusUI.width, (height - inventoryUI.height) / 2)
        perbualanUI.setPosition(stage.width / 2f, 0f)
    }

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {
        stage.dispose()
    }
}