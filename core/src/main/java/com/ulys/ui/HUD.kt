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
import com.ulys.*
import com.ulys.buru.GrafBuruan
import com.ulys.buru.Tugas
import com.ulys.dialog.Graf
import com.ulys.dialog.TindakanGraf
import com.ulys.dialog.TindakanGraf.Tujuan.*
import com.ulys.sejarah.Penyelia.getProp
import com.ulys.sejarah.Penyelia.setProp
import com.ulys.sejarah.Profil
import com.ulys.sejarah.Profil.ProfileEvent
import com.ulys.sejarah.Profil.ProfileEvent.PROFILE_LOADED
import com.ulys.sejarah.Profil.ProfileEvent.SAVING_PROFILE
import com.ulys.ui.Bualan.UIEvent.*
import com.ulys.ui.StoreInventoryObserver.StoreInventoryEvent

class HUD(camera: Camera, private val player: Entiti, private val pengurusPeta: PengurusPeta) :
    Screen, Profil, Bualan, TindakanGraf, StoreInventoryObserver, StatusObserver {

    companion object {
        val statusuiTexAtlas = TextureAtlas("skins/statusui.atlas")
        val statusuiSkin = Skin(Gdx.files.internal("skins/statusui.json"), statusuiTexAtlas)
        val itemsTexAtlas = TextureAtlas("skins/items.atlas")
    }

    private val statusUI = StatusUI(statusuiSkin, statusuiTexAtlas)
    private val inventoryUI = InventoriUI(statusuiSkin, statusuiTexAtlas)
    private val perbualanUI = PerbualanUI()
    private val kedaiUI = KedaiUI()
    private val buruUI = BuruUI()
    private val viewport = ScreenViewport(camera)
    val stage = Stage(viewport).also {
        it.addActor(statusUI)
        it.addActor(inventoryUI)
        it.addActor(inventoryUI.tooltip)
        it.addActor(perbualanUI)
        it.addActor(kedaiUI)
        it.addActor(buruUI)
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
        kedaiUI.also {
            it.isMovable = false
            it.isVisible = false
            it.setPosition(0f, 0f)
            it.closeButton.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    kedaiUI.isVisible = false
                    pengurusPeta.clearCurrentSelectedMapEntity()
                    kedaiUI.savePlayerInventory()
                }
            })
            // tambah tooltips
            it.inventoryActors.forEach { tip -> stage.addActor(tip) }
        }
        buruUI.also {
            it.isMovable = false
            it.isVisible = false
            it.setPosition(0f, stage.height * 0.5f)
            it.setSize(stage.width, stage.height - statusUI.height)
        }
        statusUI.questButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                buruUI.isVisible = !buruUI.isVisible
            }
        })
        // observers
        kedaiUI.addStoreInventoryObserver(this)
        statusUI.addStatusObserver(this)
    }

    override fun onTerima(event: ProfileEvent) {
        when (event) {
            PROFILE_LOADED -> {
                //if goldval is negative, this is our first save
                var goldVal = getProp("currentPlayerGP") ?: -1
                if (goldVal < 0) { // first time
                    //add default items if nothing is found
                    val items: Array<Barang.ItemTypeID> = player.konfigurasi.inventory
                    val itemLocations = Array<LokasiBarang>()
                    for (i in 0 until items.size) {
                        itemLocations.add(LokasiBarang(i, items.get(i).toString(), 1))
                    }
                    InventoriUI.isiInventori(inventoryUI.inventoryTable, itemLocations, inventoryUI.drag)
                    setProp("playerInventory", InventoriUI.getInventory(inventoryUI.inventoryTable))
                }

                val inventory = getProp<Array<LokasiBarang>>("playerInventory") ?: Array()
                InventoriUI.isiInventori(inventoryUI.inventoryTable, inventory, inventoryUI.drag)

                val equipInventory = getProp<Array<LokasiBarang>>("playerEquipInventory") ?: Array()
                if (!equipInventory.isEmpty) {
                    InventoriUI.isiInventori(inventoryUI.equipSlots, equipInventory, inventoryUI.drag)
                }

                // check gold
                if (goldVal < 0) {
                    // start the player with some money
                    goldVal = 20
                }
                statusUI.setGoldValue(goldVal)

                buruUI.quests = getProp<Array<GrafBuruan>>("playerQuests") ?: Array()

            }
            SAVING_PROFILE -> {
                setProp("playerInventory", InventoriUI.getInventory(inventoryUI.inventoryTable))
                setProp("playerEquipInventory", InventoriUI.getInventory(inventoryUI.equipSlots))
                setProp("currentPlayerGP", statusUI.getGoldValue())
                setProp("playerQuests", buruUI.quests)
            }
        }
    }

    override fun onBual(k: Konfigurasi, event: Bualan.UIEvent) {
        when (event) {
            LOAD_CONVERSATION -> {
                perbualanUI.loadConversation(k)
                perbualanUI.graf.addPemerhatiTindakan(this)
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

    override fun onTindakanGraf(t: TindakanGraf.Tujuan, g: Graf) {
        when (t) {
            LOAD_STORE_INVENTORY -> {
                // inventori pemain
                val inventory = InventoriUI.getInventory(inventoryUI.inventoryTable)
                kedaiUI.loadPlayerInventory(inventory)
                // inventori penjual black smith
                val blackSmith = pengurusPeta.currentSelectedEntity ?: return
                val items = blackSmith.konfigurasi.inventory
                val itemLocations = Array<LokasiBarang>().also {
                    for (i in 0 until items.size) {
                        it.add(LokasiBarang(i, items[i].toString(), 1))
                    }
                }
                kedaiUI.loadStoreInventory(itemLocations)

                perbualanUI.isVisible = false
                kedaiUI.toFront()
                kedaiUI.isVisible = true
            }
            EXIT_CONVERSATION -> {
                perbualanUI.isVisible = false
                pengurusPeta.clearCurrentSelectedMapEntity()
            }
            ACCEPT_QUEST -> {
                val entiti = pengurusPeta.currentSelectedEntity ?: return
                buruUI.addQuest(entiti.konfigurasi.questConfigPath)

                perbualanUI.isVisible = false
                pengurusPeta.clearCurrentSelectedMapEntity()
            }
            NONE -> {
            }
        }
    }

    override fun onNotify(value: String, event: StoreInventoryEvent) {
        when (event) {
            StoreInventoryEvent.PLAYER_GP_TOTAL_UPDATED -> {
                statusUI.setGoldValue(value.toInt())
            }
            StoreInventoryEvent.PLAYER_INVENTORY_UPDATED -> {
                val array = j.fromJson<Array<LokasiBarang>>(value)
                InventoriUI.isiInventori(inventoryUI.inventoryTable, array, inventoryUI.drag)
            }
        }
    }

    override fun onNotify(value: Int, event: StatusObserver.StatusEvent) {
        when (event) {
            StatusObserver.StatusEvent.UPDATED_GP -> {
                kedaiUI.setPlayerGP(value)
            }
            else -> {
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