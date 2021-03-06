package com.ulys.ui

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array

class StatusUI(skin: Skin, textureAtlas: TextureAtlas) :
    Window("statistik", skin),
    StatusSubject {

    val suisInventori = ImageButton(skin, "inventory-button").also {
        it.imageCell.size(32f, 32f)
    }
    private val hpBar = Image(textureAtlas.findRegion("HP_Bar"))
    private val mpBar = Image(textureAtlas.findRegion("MP_Bar"))
    private val xpBar = Image(textureAtlas.findRegion("XP_Bar"))
    private val goldVal: Label

    private var level = 1
    private var gold = 0
    private var hp = 50
    private var mp = 50
    private var xp = 0
    override val statusObservers = Array<StatusObserver>()
    val questButton = ImageButton(skin, "quest-button")

    init {
        val bar = Image(textureAtlas.findRegion("Bar"))
        val groupHealth = WidgetGroup()
        groupHealth.addActor(bar)
        groupHealth.addActor(hpBar)
        hpBar.setPosition(3f, 6f)

        val bar2 = Image(textureAtlas.findRegion("Bar"))
        val groupMagic = WidgetGroup()
        mpBar.setPosition(3f, 6f)
        groupMagic.addActor(bar2)
        groupMagic.addActor(mpBar)

        val mpLabel = Label(" mp:", skin)
        val mp = Label(mp.toString(), skin)

        val groupXp = WidgetGroup()
        xpBar.setPosition(3f, 6f)
        val bar3 = Image(textureAtlas.findRegion("Bar"))
        groupXp.addActor(bar3)
        groupXp.addActor(xpBar)

        val xpLabel = Label(" xp:", skin)
        val xp = Label(xp.toString(), skin)

        val levelLabel = Label("lv:", skin)
        val levelVal = Label(level.toString(), skin)

        val goldLabel = Label("gp: ", skin)
        goldVal = Label(gold.toString(), skin)

        questButton.imageCell.size(32f,32f)
        //Add to layout
        defaults().expand().fill()

        //account for the title padding
        pad(padTop + 10, 10f, 10f, 10f)

        add()
        add(questButton).align(Align.right)
        add(suisInventori).align(Align.right)
        row()

        add(groupHealth).size(bar.width, bar.height)
        add(Label(" hp:", skin))
        add(Label(hp.toString(), skin)).align(Align.left)
        row()

        add(groupMagic).size(bar2.width, bar2.height)
        add(mpLabel)
        add(mp).align(Align.left)
        row()

        add(groupXp).size(bar3.width, bar3.height)
        add(xpLabel)
        add(xp).align(Align.left)
        row()

        add(levelLabel)
        add(levelVal).align(Align.left)
        row()
        add(goldLabel)
        add(goldVal).align(Align.left)
        pad(40f)
        pack()
//        debugTable()
//        debug()
    }

    fun getGoldValue(): Int = gold

    fun setGoldValue(value: Int) {
        gold = value
        goldVal.setText(gold.toString())
        notify(gold, StatusObserver.StatusEvent.UPDATED_GP)
    }

}
