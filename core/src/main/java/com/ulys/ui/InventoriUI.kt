package com.ulys.ui

import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.badlogic.gdx.utils.Array
import com.ulys.ui.Barang.Fungsi.*


class InventoriUI(skin: Skin, textureAtlas: TextureAtlas) :
    Window("Inventori", skin, "solidbackground") {

    private val sebaris = 10
    val drag = DragAndDrop()
    private val wh = 52f // slot width and height
    private val playerTable = Table()
    val equipSlots = Table()
    val inventoryTable = Table()
    val tooltip = Tooltip(skin)
    private val tooltipListener = TooltipListener(tooltip)

    init {
        inventoryTable.name = "Inventory_Slot_Table"
        equipSlots.name = "Equipment_Slot_Table"

        drag.setKeepWithinStage(false) // penting
        setFillParent(false)

        equipSlots.defaults().space(10f)

        val headSlot = Slot(ARMOR_HELMET.bit, "inv_helmet")
        val leftArmSlot = Slot(
            WEAPON_ONEHAND.bit or WEAPON_TWOHAND.bit or ARMOR_SHIELD.bit or WAND_ONEHAND.bit or WAND_TWOHAND.bit,
            "inv_weapon"
        )
        val rightArmSlot = Slot(
            WEAPON_ONEHAND.bit or WEAPON_TWOHAND.bit or ARMOR_SHIELD.bit or WAND_ONEHAND.bit or WAND_TWOHAND.bit,
            "inv_shield"
        )
        val chestSlot = Slot(ARMOR_CHEST.bit, "inv_chest")
        val legsSlot = Slot(ARMOR_FEET.bit, "inv_boot")

        headSlot.addListener(tooltipListener)
        leftArmSlot.addListener(tooltipListener)
        rightArmSlot.addListener(tooltipListener)
        chestSlot.addListener(tooltipListener)
        legsSlot.addListener(tooltipListener)

        drag.addTarget(SlotTarget(headSlot))
        drag.addTarget(SlotTarget(leftArmSlot))
        drag.addTarget(SlotTarget(rightArmSlot))
        drag.addTarget(SlotTarget(chestSlot))
        drag.addTarget(SlotTarget(legsSlot))

        for (i in 1..jumlahSlot) {
            val slot = Slot()
            slot.addListener(tooltipListener)
            drag.addTarget(SlotTarget(slot))
            inventoryTable.add(slot).size(wh, wh)
            if (i % sebaris == 0) inventoryTable.row()
        }

        equipSlots.add()
        equipSlots.add(headSlot).size(wh, wh)
        equipSlots.row()
        equipSlots.add(leftArmSlot).size(wh, wh)
        equipSlots.add(chestSlot).size(wh, wh)
        equipSlots.add(rightArmSlot).size(wh, wh)
        equipSlots.row()

        equipSlots.add()
        equipSlots.right().add(legsSlot).size(wh, wh)
        playerTable.background = Image(NinePatch(textureAtlas.createPatch("dialog"))).drawable
        playerTable.add(equipSlots)

        add(playerTable).padBottom(20f).row()
        add(inventoryTable).row()
        pack()
    }

    companion object {
        const val jumlahSlot = 50

        fun isiInventori(table: Table, lislokasi: Array<LokasiBarang>, drag: DragAndDrop) {
            val cells = table.cells
            for (i in 0 until lislokasi.size) {
                val loc = lislokasi[i]
                val itemTypeId = Barang.ItemTypeID.valueOf(loc.itemTypeAtLocation)

                val slot = cells[loc.locationIndex].actor as Slot
                slot.popBarangan()

                for (kiraan in 0 until loc.numberItemsAtLocation) {
                    val brg = FaktoriBarang.buatBarang(itemTypeId)
                    brg.name = table.name // set nama untuk transaksi
                    slot.add(brg)
                    drag.addSource(SlotSumber(slot, drag))
                }
            }
        }

        fun getInventory(fromTable: Table): Array<LokasiBarang> {
            val cells = fromTable.cells
            val items = Array<LokasiBarang>()
            for (i in 0 until cells.size) {
                val slot = cells[i].actor as Slot? ?: continue
                if (slot.adaBarang()) {
                    items.add(LokasiBarang(i, slot.getTopItem()?.itemTypeID.toString(), slot.kiraan))
                }
            }
            return items
        }

        fun setInventoryItemNames(targetTable: Table, name: String) {
            val cells = targetTable.cells
            for (i in 0 until cells.size) {
                val inventorySlot = cells[i].actor as Slot? ?: continue
                inventorySlot.updateAllInventoryItemNames(name)
            }
        }
    }
}
