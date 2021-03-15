package com.ulys.ui

import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.badlogic.gdx.utils.Scaling

class InventoriUI(skin: Skin, textureAtlas: TextureAtlas) :
    Window("Inventori", skin, "solidbackground") {

    private val jumlahSlot = 50
    private val sebaris = 10
    private val drag = DragAndDrop()

    init {
        drag.setKeepWithinStage(false) // penting
        setFillParent(false)

        val playerTable = Table()
        playerTable.add(Image(NinePatch(textureAtlas.createPatch("dialog")))).size(200f, 250f)

        val inventoryTable = Table()
        for (i in 1..jumlahSlot) {
            val slot = Slot()
            drag.addTarget(SlotTarget(slot))

            if (i == 5 || i == 10 || i == 15 || i == 20) {
                val image = Image(HUD.itemsTexAtlas.findRegion("armor01"))
                image.setScaling(Scaling.none)
                slot.add(image)

                drag.addSource(SlotSumber(slot, drag))
            }

            inventoryTable.add(slot).size(52f, 52f)

            if (i % sebaris == 0) inventoryTable.row()
        }

        add(playerTable).padBottom(20f).row()
        add(inventoryTable).row()
        pack()
    }
}
