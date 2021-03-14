package com.ulys.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align

class StatusUI : Group() {
    private val textureAtlas = TextureAtlas("skins/statusui.pack")
    private val background = NinePatch(textureAtlas.findRegion("dialog"))
    private val hudBackground = Image(background)
    private val skin = Skin().also { it.load(Gdx.files.internal("skins/uiskin.json")) }
    private val hpBar = Image(textureAtlas.findRegion("HP_Bar"))
    private val mpBar = Image(textureAtlas.findRegion("MP_Bar"))
    private val xpBar = Image(textureAtlas.findRegion("XP_Bar"))

    private var level = 1
    private var gold = 0
    private var hp = 50
    private var mp = 50
    private var xp = 0

    init {

        hudBackground.setSize(265f, 135f)

        val bar = Image(textureAtlas.findRegion("Bar"))
        val groupHealth = WidgetGroup()
        groupHealth.addActor(bar)
        groupHealth.addActor(hpBar)
        hpBar.setPosition(3f, 6f)

        val table = Table()
        val cell = table.add(groupHealth)
        cell.width(bar.width)
        cell.height(bar.height)
        table.add(Label(" hp:", skin))
        table.add(Label(hp.toString(), skin))
        table.row()

        val bar2 = Image(textureAtlas.findRegion("Bar"))
        val groupMagic = WidgetGroup()
        mpBar.setPosition(3f, 6f)
        groupMagic.addActor(bar2)
        groupMagic.addActor(mpBar)
        val cell2 = table.add(groupMagic)
        cell2.width(bar2.width)
        cell2.height(bar2.height)
        val mpLabel = Label(" mp:", skin)
        table.add(mpLabel)
        val mp = Label(mp.toString(), skin)
        table.add(mp)
        table.row()

        val groupXp = WidgetGroup()
        xpBar.setPosition(3f, 6f)
        val bar3 = Image(textureAtlas.findRegion("Bar"))
        groupXp.addActor(bar3)
        groupXp.addActor(xpBar)
        val cell3 = table.add(groupXp)
        cell3.width(bar3.width)
        cell3.height(bar3.height)
        val xpLabel = Label(" xp:", skin)
        table.add(xpLabel)
        val xp = Label(xp.toString(), skin)
        table.add(xp)
        table.row()

        val levelLabel = Label("lv:", skin)
        table.add(levelLabel)
        val levelVal = Label(level.toString(), skin)
        table.add(levelVal).align(Align.left)

        val goldLabel = Label("gp: ", skin)
        table.add(goldLabel)
        val goldVal = Label(gold.toString(), skin)
        table.add(goldVal)

        table.debugTable()
        table.setFillParent(true)
        table.setPosition(hudBackground.width / 2, hudBackground.height / 2)
        addActor(hudBackground)
        addActor(table)
        Gdx.app.debug(
            "StatusUI",
            "table position (${hudBackground.width}, ${hudBackground.height}), w:${table.width}, h:${table.height}"
        )

    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
//        background.draw(batch, 0f, 0f, 600f, 400f)
    }

}
