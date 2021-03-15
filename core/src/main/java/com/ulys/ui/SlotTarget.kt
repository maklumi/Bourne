package com.ulys.ui

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

        if (slotTarget.adaBarang()) {
            if (target != null && actor.isSameItemType(target) && actor.isStackable()) {
                slotTarget.add(actor)
            } else {
                Slot.tukarTempat(sumber, slotTarget, actor)
            }
        } else {
            slotTarget.add(actor)
        }

    }
}
