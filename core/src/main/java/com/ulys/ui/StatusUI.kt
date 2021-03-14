package com.ulys.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align

class StatusUI : Group() {
    private val textureAtlas = TextureAtlas("skins/statusui.pack")
    private val hudBackground = Image(textureAtlas.findRegion("HUD_Background"))
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

//        table.debug()
        table.setPosition(135f, 68f)
        table.setFillParent(true)

        addActor(hudBackground)
        addActor(table)
    }

    /*
      override fun draw(batch: Batch, parentAlpha: Float) {
          super.draw(batch, parentAlpha)
          batch.draw(hudBackgroundImage, 0f, 0f, 4f, 3f)
      }
  */
}
