package com.ulys.ui

import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array

class Slot() : Stack(), TransaksiSubjek {

    //All slots have this default image
    private val image = Image(NinePatch(HUD.statusuiTexAtlas.createPatch("dialog")))
    val kiraan: Int get() = children.size - 2
    private val label = Label("0", HUD.statusuiSkin, "inventory-item-count")
    private var stiker: Image? = null
    private val rakLatar = Stack()
    private var bitFungsi: Int = 0

    constructor(bitFn: Int, nama: String) : this() {
        bitFungsi = bitFn
        stiker = Image(HUD.itemsTexAtlas.findRegion(nama))
        rakLatar.add(stiker) // lepas init bawah
    }

    override val lisTransaksi: Array<Transaksi> = Array()

    init {
        rakLatar.add(image)
        rakLatar.name = "Decal"
        add(rakLatar)
        add(label)
        label.setAlignment(Align.bottomRight)
        paparKiraan()
    }

    fun bolehTerimaFungsi(bit: Int): Boolean {
        if (bitFungsi == 0) return true
        return bitFungsi.and(bit) == bit
    }

    override fun add(actor: Actor) {
        super.add(actor)
        if (actor != image && actor != label) {
            paparKiraan()
        }
    }

    fun paparKiraan() {
        label.setText(kiraan.toString())
        label.isVisible = kiraan > 1
        stiker?.isVisible = children.size == 2 //raklatar dan label
    }

    fun getTopItem(): Barang? {
        return if (hasChildren() && children.size > 2) {
            children.peek() as Barang
        } else null
    }

    fun adaBarang(): Boolean {
        return children.size > 2
    }

    fun tambahBarangan(array: Array<Actor>) {
        for (aktor in array) {
            add(aktor)
        }
    }

    fun popBarangan(): Array<Actor> {
        val semua = Array<Actor>()
        for (i in 2 until children.size) {
            semua.add(children.pop())
            paparKiraan()
        }
        return semua
    }

    fun updateAllInventoryItemNames(name: String) {
        if (adaBarang()) {
            val arrayChildren = children
            //skip first two elements
            for (i in arrayChildren.size - 1 downTo 2) {
                arrayChildren.get(i).name = name
            }
        }
    }

    fun removeAllInventoryItemsWithName(name: String) {
        if (adaBarang()) {
            val arrayChildren = children
            //skip first two elements
            for (i in arrayChildren.size - 1 downTo 2) {
                val itemName = arrayChildren.get(i).name
                if (itemName == name) {
                    transaksi(this, Transaksi.SlotEvent.REMOVED_ITEM)
                    arrayChildren.removeIndex(i)
                }
            }
        }
    }

    fun getNumItems(name: String): Int {
        return children.count { it.name == name }
    }

    fun sendAddNotification() {

    }

    fun sendRemoveNotification() {
        transaksi(this, Transaksi.SlotEvent.REMOVED_ITEM)
    }

    companion object {
        fun tukarTempat(sumber: Slot, target: Slot, dragActor: Barang) {
            //check if items can accept each other, otherwise, no swap
            if (!target.bolehTerimaFungsi(dragActor.itemUseType) ||
                !sumber.bolehTerimaFungsi(target.getTopItem()!!.itemUseType)
            ) {
                sumber.add(dragActor)
                return
            }
            //swap
            val senaraiSumber = sumber.popBarangan()
            senaraiSumber.add(dragActor)
            val senaraiTarget = target.popBarangan()
            sumber.tambahBarangan(senaraiTarget)
            target.tambahBarangan(senaraiSumber)
        }
    }

}
