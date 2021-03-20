package com.ulys.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.ulys.ui.StoreInventoryObserver.StoreInventoryEvent

class KedaiUI : Window("Inventori Kedai", HUD.statusuiSkin, "solidbackground"),
    Transaksi, StoreInventorySubject {

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
    private var playerTotal = 0

    private val sellTotalLabel = Label("$SELL : $tradeInVal $GP", skin)
    private val buyTotalLabel = Label("$BUY : $fullValue $GP", skin)
    private val playerTotalGP = Label("$PLAYER_TOTAL : $playerTotal $GP", skin)
    private val sellButton = TextButton(SELL, skin, "inventory")
    private val buyButton = TextButton(BUY, skin, "inventory")

    private val buttonsTable = Table()
    private val totalLabelsTable = Table()
    val closeButton = TextButton("X", skin)

    override val storeInventoryObservers = Array<StoreInventoryObserver>()

    init {
//        setFillParent(true)
        setKeepWithinStage(false)

        // create
        inventorySlotTable.name = STORE_INVENTORY
        playerInventorySlotTable.name = PLAYER_INVENTORY

        disableButton(sellButton, true)

        sellTotalLabel.setAlignment(Align.center)
        buyTotalLabel.setAlignment(Align.center)

        disableButton(buyButton, true)

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
        row()
        add(playerTotalGP)
        pack()

        // listeners
        buyButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (fullValue in 1..playerTotal) {
                    playerTotal -= fullValue
                    notify(playerTotal.toString(), StoreInventoryEvent.PLAYER_GP_TOTAL_UPDATED)
                    fullValue = 0
                    buyTotalLabel.setText("$BUY : $fullValue $GP")
                    disableButton(buyButton, true)
                }
            }
        })

        sellButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (tradeInVal > 0) {
                    playerTotal += tradeInVal
                    notify(playerTotal.toString(), StoreInventoryEvent.PLAYER_GP_TOTAL_UPDATED)
                    tradeInVal = 0
                    sellTotalLabel.setText("$SELL : $tradeInVal $GP")
                    disableButton(sellButton, true)

                    // remove sold items
                    val cells = inventorySlotTable.cells
                    for (i in 0 until cells.size) {
                        val slot = cells[i].actor as Slot? ?: continue
                        if (slot.adaBarang() &&
                            slot.getTopItem()?.name == PLAYER_INVENTORY
                        ) {
                            slot.popBarangan()
                        }
                    }
                }
            }
        })
    }

    fun loadPlayerInventory(playerInventoryItems: Array<LokasiBarang>) {
        InventoriUI.isiInventori(playerInventorySlotTable, playerInventoryItems, drop)
    }

    fun loadStoreInventory(storeInventoryItems: Array<LokasiBarang>) {
        InventoriUI.isiInventori(inventorySlotTable, storeInventoryItems, drop)
    }

    override fun onTransaksi(slot: Slot, event: Transaksi.SlotEvent) {
        when (event) {
            Transaksi.SlotEvent.ADDED_ITEM -> {
                if (slot.getTopItem()?.name == PLAYER_INVENTORY &&
                    slot.name == STORE_INVENTORY
                ) {
                    tradeInVal += slot.getTopItem()?.hargaJual() ?: 0
                    sellTotalLabel.setText("$SELL : $tradeInVal $GP")
                    if (tradeInVal > 0) {
                        disableButton(sellButton, false)
                    }
                }
                if (slot.getTopItem()?.name == STORE_INVENTORY &&
                    slot.name == PLAYER_INVENTORY
                ) {
                    fullValue += slot.getTopItem()?.itemValue ?: 0
                    buyTotalLabel.setText("$BUY : $fullValue $GP")
                    if (fullValue > 0) {
                        disableButton(buyButton, false)
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
                        disableButton(sellButton, true)
                    }
                }
                if (slot.getTopItem()?.name == STORE_INVENTORY &&
                    slot.name == PLAYER_INVENTORY
                ) {
                    fullValue -= slot.getTopItem()?.itemValue ?: 0
                    buyTotalLabel.setText("$BUY : $fullValue $GP")
                    if (fullValue <= 0) {
                        disableButton(buyButton, true)
                    }
                }
            }
        }
    }

    fun setPlayerGP(value: Int) {
        playerTotal = value
        playerTotalGP.setText("$PLAYER_TOTAL : $playerTotal $GP")
    }

    private fun disableButton(button: Button, disable: Boolean) {
        if (disable) {
            button.isDisabled = true
            button.touchable = Touchable.disabled
        } else {
            button.isDisabled = false
            button.touchable = Touchable.enabled
        }
    }

    companion object {
        private const val STORE_INVENTORY = "Store_Inventory"
        private const val PLAYER_INVENTORY = "Player_Inventory"
        private const val SELL = "JUAL"
        private const val BUY = "BELI"
        private const val GP = "RM"
        private const val PLAYER_TOTAL = "Player Total"
    }
}
