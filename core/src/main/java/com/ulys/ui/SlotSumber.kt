package com.ulys.ui

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.*
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target

class SlotSumber(
    var sumber: Slot,
    private var dragAndDrop: DragAndDrop
) :
    Source(sumber.getTopItem()) {

    override fun dragStart(event: InputEvent, x: Float, y: Float, pointer: Int): Payload {
        val payload = Payload()

        sumber = actor.parent as Slot
        sumber.paparKiraan()

        payload.dragActor = actor // actor is top item or null
        dragAndDrop.setDragActorPosition(-event.stageX + actor.width, -event.stageY)

        return payload
    }

    override fun dragStop(event: InputEvent, x: Float, y: Float, pointer: Int, payload: Payload?, target: Target?) {
        // kalau target tak valid, pulang balik ke slot stack
        if (target == null && payload != null) {
            sumber.add(payload.dragActor)
        }
        sumber.paparKiraan()
    }
}
