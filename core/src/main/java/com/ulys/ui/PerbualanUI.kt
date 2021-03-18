package com.ulys.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.ulys.Konfigurasi
import com.ulys.dialog.Graf
import com.ulys.dialog.Pilihan
import com.ulys.j
import com.badlogic.gdx.scenes.scene2d.ui.List as GdxList

class PerbualanUI : Window("Dialog perbualan", HUD.statusuiSkin, "solidbackground") {

    private val dialogText = Label("Tiada perbualan", skin)
    private val listItems = GdxList<Pilihan>(skin)
    private var graf = Graf()
    var currentEntityID: String = ""
    val closeButton = TextButton("X", skin)

    init {
        //create
        dialogText.wrap = true
        dialogText.setAlignment(Align.center)

        val scrollPane = ScrollPane(listItems)
        scrollPane.setOverscroll(false, true)
        scrollPane.fadeScrollBars = false
        scrollPane.setScrollingDisabled(true, false)
        scrollPane.setScrollbarsOnTop(true)


        //layout
        add()
        add(closeButton)
        row()

        defaults().expand().fill()
        add(dialogText).pad(10f, 10f, 10f, 10f)
        row()
        add(scrollPane).pad(10f, 20f, 10f, 10f)

        debug()
        pack()

        //Listeners
        listItems.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                val choice = listItems.selected ?: return
                graf.setCurrentConversation(choice.destinationId)
                updateDialogDanPilihan(choice.destinationId)
            }
        })
    }

    fun loadConversation(entityConfig: Konfigurasi) {
        val fullFilenamePath = entityConfig.conversationConfigPath
        if (fullFilenamePath.isEmpty() || !Gdx.files.internal(fullFilenamePath).exists()) {
            Gdx.app.debug(tag, "Conversation file does not exist!")
            dialogText.setText("")
            listItems.clearItems()
            return
        }
        currentEntityID = entityConfig.entityID
        val graph = j.fromJson(Graf::class.java, Gdx.files.internal(fullFilenamePath))
        setConversationGraph(graph)
    }

    private fun setConversationGraph(g: Graf) {
        graf = g
        graf.convertType()
        updateDialogDanPilihan(graf.currentConversationId)
    }

    private fun updateDialogDanPilihan(destinationId: Int) {
        val conversation = graf.getConversationByID(destinationId) ?: return
        dialogText.setText(conversation.dialog)
        val arraylist = graf.getCurrentChoices()
        val currentChoices = Array<Pilihan>()
        for (choice in arraylist) {
            currentChoices.add(choice)
        }
        listItems.setItems(currentChoices)
        listItems.selectedIndex = -1
    }

    private val tag = PerbualanUI::class.java.simpleName

}