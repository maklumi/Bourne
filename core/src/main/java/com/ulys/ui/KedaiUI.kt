package com.ulys.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array

class KedaiUI : Window("Inventori Kedai", HUD.statusuiSkin, "solidbackground"),
    Transaksi {

    private val numStoreInventorySlots = 30
    private val lengthSlotRow = 10
    private val slotWidth = 52
    private val slotHeight = 52

    private val inventorySlotTable = Table()
    private val playerInventorySlotTable = Table()
    private val drop = DragAndDrop()
    val inventoryActors = Array<Actor>()

    private val inventorySlotTooltip = Tooltip(skin)

    private var tradeInVal = 0
    private var fullValue = 0

    private val sellTotalLabel = Label("$SELL : $tradeInVal $GP", skin)
    private val buyTotalLabel = Label("$BUY : $fullValue $GP", skin)

    private val sellButton = TextButton(SELL, skin, "inventory")
    private val buyButton = TextButton(BUY, skin, "inventory")

    private val buttonsTable = Table()
    private val totalLabelsTable = Table()
    val closeButton = TextButton("X", skin)

    init {
//        setFillParent(true)
        setKeepWithinStage(false)

        // create
        inventorySlotTable.name = STORE_INVENTORY
        playerInventorySlotTable.name = PLAYER_INVENTORY

        sellButton.isDisabled = true
        sellButton.touchable = Touchable.disabled

        sellTotalLabel.setAlignment(Align.center)
        buyTotalLabel.setAlignment(Align.center)

        buyButton.isDisabled = true
        buyButton.touchable = Touchable.disabled

        buttonsTable.defaults().expand().fill()
        buttonsTable.add(sellButton).padLeft(10f).padRight(10f)
        buttonsTable.add(buyButton).padLeft(10f).padRight(10f)

        totalLabelsTable.defaults().expand().fill()
        totalLabelsTable.add(sellTotalLabel).padLeft(40f)
        totalLabelsTable.add()
        totalLabelsTable.add(buyTotalLabel).padRight(40f)

        //layout
        for (i in 1..numStoreInventorySlots) {
            val inventorySlot = Slot()
            inventorySlot.addListener(TooltipListener(inventorySlotTooltip))
            inventorySlot.name = STORE_INVENTORY
            inventorySlot.addTransaksi(this)

            drop.addTarget(SlotTarget(inventorySlot))

            inventorySlotTable.add(inventorySlot).size(slotWidth.toFloat(), slotHeight.toFloat())

            if (i % lengthSlotRow == 0) {
                inventorySlotTable.row()
            }
        }

        for (i in 1..InventoriUI.jumlahSlot) {
            val inventorySlot = Slot()
            inventorySlot.addListener(TooltipListener(inventorySlotTooltip))
            inventorySlot.name = PLAYER_INVENTORY
            inventorySlot.addTransaksi(this)

            drop.addTarget(SlotTarget(inventorySlot))

            playerInventorySlotTable.add(inventorySlot).size(slotWidth.toFloat(), slotHeight.toFloat())

            if (i % lengthSlotRow == 0) {
                playerInventorySlotTable.row()
            }
        }

        inventoryActors.add(inventorySlotTooltip)

//        this.debugAll()
        add()
        add(closeButton)
        row()
        defaults().expand().fill()
        add(inventorySlotTable).pad(10f, 10f, 10f, 10f)
        row()
        add(buttonsTable)
        row()
        add(totalLabelsTable)
        row()
        add(playerInventorySlotTable).pad(10f, 10f, 10f, 10f)
        pack()
    }

    fun loadPlayerInventory(playerInventoryItems: Array<LokasiBarang>) {
        InventoriUI.isiInventori(playerInventorySlotTable, playerInventoryItems, drop)
    }

    fun loadStoreInventory(storeInventoryItems: Array<LokasiBarang>) {
        InventoriUI.isiInventori(inventorySlotTable, storeInventoryItems, drop)
    }

    override fun onTransaksi(slot: Slot, event: Transaksi.SlotEvent) {
        Gdx.app.debug("KedaiUI", "$event : ${slot.getTopItem()} ${slot.name}")
        when (event) {
            Transaksi.SlotEvent.ADDED_ITEM -> {
                if (slot.getTopItem()?.name == PLAYER_INVENTORY &&
                    slot.name == STORE_INVENTORY
                ) {
                    tradeInVal += slot.getTopItem()?.hargaJual() ?: 0
                    sellTotalLabel.setText("$SELL : $tradeInVal $GP")
                    if (tradeInVal > 0) {
                        sellButton.isDisabled = false
                        sellButton.touchable = Touchable.enabled
                    }
                }
                if (slot.getTopItem()?.name == STORE_INVENTORY &&
                    slot.name == PLAYER_INVENTORY
                ) {
                    fullValue += slot.getTopItem()?.itemValue ?: 0
                    buyTotalLabel.setText("$BUY : $fullValue $GP")
                    if (fullValue > 0) {
                        buyButton.isDisabled = false
                        sellButton.touchable = Touchable.enabled
                    }
                }
            }
            Transaksi.SlotEvent.REMOVED_ITEM -> {
                if (slot.getTopItem()?.name == PLAYER_INVENTORY &&
                    slot.name == STORE_INVENTORY
                ) {
                    tradeInVal -= slot.getTopItem()?.hargaJual() ?: 0
                    sellTotalLabel.setText("$SELL : $tradeInVal $GP")
                    if (tradeInVal <= 0) {
                        sellButton.isDisabled = true
                        sellButton.touchable = Touchable.disabled
                    }
                }
                if (slot.getTopItem()?.name == STORE_INVENTORY &&
                    slot.name == PLAYER_INVENTORY
                ) {
                    fullValue -= slot.getTopItem()?.itemValue ?: 0
                    buyTotalLabel.setText("$BUY : $fullValue $GP")
                    if (fullValue <= 0) {
                        buyButton.isDisabled = true
                        buyButton.touchable = Touchable.disabled
                    }
                }
            }
        }
    }

    companion object {
        private const val STORE_INVENTORY = "Store_Inventory"
        private const val PLAYER_INVENTORY = "Player_Inventory"
        private const val SELL = "JUAL"
        private const val BUY = "BELI"
        private const val GP = "RM"
    }
}
