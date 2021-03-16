package com.ulys.ui

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener

class TooltipListener(private val tooltip: Tooltip) : InputListener() {

    private var didalam = false
    private val koord = Vector2(0f, 0f)
    private val offset = Vector2(20f, 30f)

    override fun mouseMoved(event: InputEvent, x: Float, y: Float): Boolean {
        val slot = event.listenerActor as Slot
        if (didalam) {
            koord.set(x, y)
            slot.localToStageCoordinates(koord)

            tooltip.setPosition(koord.x + offset.x, koord.y + offset.y)
        }
        return false
    }

    override fun touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {
        val slot = event.listenerActor as Slot
        tooltip.bolehTunjuk(slot, false)
    }

    override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        return true
    }

    override fun enter(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
        val slot = event.listenerActor as Slot

        didalam = true

        koord.set(x, y)
        slot.localToStageCoordinates(koord)

        tooltip.infoTerkiniBarang(slot)
        tooltip.setPosition(koord.x + offset.x, koord.y + offset.y)
        tooltip.toFront()
        tooltip.bolehTunjuk(slot, true)
    }

    override fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, toActor: Actor?) {
        val slot = event.listenerActor as Slot
        tooltip.bolehTunjuk(slot, false)
        didalam = false

        koord.set(x, y)
        slot.localToStageCoordinates(koord)
    }
}