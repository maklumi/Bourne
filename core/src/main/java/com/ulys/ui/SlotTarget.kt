package com.ulys.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source

class SlotTarget(private val slotTarget: Slot) : DragAndDrop.Target(slotTarget) {

    override fun drag(source: Source, payload: Payload, x: Float, y: Float, pointer: Int): Boolean {
        // zIndex ke depan semua children
        source.actor.parent.toFront()
        // true = anggap flying over valid target
        return true
    }

    override fun reset(source: Source, payload: Payload) {}

    override fun drop(source: Source, payload: Payload, x: Float, y: Float, pointer: Int) {
        val actor = payload.dragActor as Barang? ?: return
        val target = slotTarget.getTopItem()
        val sumber = (source as SlotSumber).sumber

        //First, does the slot accept the source item type?
        if (!slotTarget.bolehTerimaFungsi(actor.itemUseType)) {
            //Put item back where it came from, slot doesn't accept item
            sumber.add(actor)
            return
        }

        if (slotTarget.adaBarang()) {
            if (target != null && actor.isSameItemType(target) && actor.isStackable()) {
                slotTarget.add(actor)
            } else {
                Slot.tukarTempat(slotTarget, slotTarget, actor)
            }
        } else {
            slotTarget.add(actor)
            sumber.transaksi(slotTarget, Transaksi.SlotEvent.ADDED_ITEM)
        }

    }
}
