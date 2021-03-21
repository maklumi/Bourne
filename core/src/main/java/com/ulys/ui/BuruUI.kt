package com.ulys.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.ulys.buru.GrafBuruan
import com.ulys.buru.Tugas
import com.ulys.j

class BuruUI : Window("Quest Log", HUD.statusuiSkin, "solidbackground") {

    private val listQuests = List<GrafBuruan>(skin)
    private val listTasks = List<Tugas>(skin)
    private val questLabel = Label("Quests:", skin)
    private val tasksLabel = Label("Tasks:", skin)
    var quests: Array<GrafBuruan> = Array()
        set(value) {
            field = value
            updateQuests()
        }

    init {
        //create
        val spQuest = ScrollPane(listQuests, skin, "inventoryPane")
        spQuest.setOverscroll(false, false)
        spQuest.fadeScrollBars = false
        spQuest.setForceScroll(true, false)

        val spTasks = ScrollPane(listTasks, skin, "inventoryPane")
        spTasks.setOverscroll(false, false)
        spTasks.fadeScrollBars = false
        spTasks.setForceScroll(true, false)

        //layout
        add(questLabel).align(Align.left)
        add(tasksLabel).align(Align.left)
        row()
        defaults().expand().fill()
        add(spQuest).padRight(5f)
        add(spTasks).padLeft(5f)

        debug()
        pack()

        //Listeners
        listQuests.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                val quest = listQuests.selected ?: return
                populateQuestDialog(quest)
            }
        }
        )
    }

    private fun populateQuestDialog(graph: GrafBuruan) {
        listTasks.clearItems()
        val arrayList = graph.getAllQuestTasks()
        val array = Array<Tugas>()
        arrayList.forEach { array.add(it) }
        listTasks.setItems(array)
        listTasks.selectedIndex = -1
    }

    fun addQuest(questConfigPath: String) {
        if (questConfigPath.isEmpty() || !Gdx.files.internal(questConfigPath).exists()) {
            Gdx.app.debug(TAG, "Quest file does not exist!")
            return
        }

        clearDialog()

        val graph = j.fromJson(GrafBuruan::class.java, Gdx.files.internal(questConfigPath))
        quests.add(graph)
        updateQuests()
    }

    private fun updateQuests() {
        clearDialog()
        listQuests.setItems(quests)
        Gdx.app.debug("BuruUI","sais list ${quests.size}")
        listQuests.selectedIndex = -1
    }

    private fun clearDialog() {
        listQuests.clearItems()
    }

    companion object {
        private const val TAG = "BuruUI"
    }
}
