package com.ulys.tests

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonWriter
import com.ulys.dialog.Graf
import com.ulys.dialog.Perbualan
import com.ulys.dialog.Pilihan
import com.ulys.j
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

object GrafTest {
    private val conversations = Hashtable<Int, Perbualan>()
    private var input = ""

    @JvmStatic
    fun main(args: Array<String>) {
        val mula = Perbualan().also {
            it.id = 500
            it.dialog = "Sebagai permulaan, anda nak main?"
        }
        val jawabYa = Perbualan().also {
            it.id = 601
            it.dialog = "ORAIT nak main. 2-2 ialah kosong"
        }
        val jawabTak = Perbualan().also {
            it.id = 802
            it.dialog = "Tak nak main sudah!"
        }
        val takBerkait = Perbualan().also {
            it.id = 205
            it.dialog = "Node tak berkaitan"
        }

        conversations[mula.id] = mula
        conversations[jawabYa.id] = jawabYa
        conversations[jawabTak.id] = jawabTak
        conversations[takBerkait.id] = takBerkait

        val graph = Graf(conversations, mula.id)

        val yesChoice = Pilihan()
        yesChoice.sourceId = mula.id
        yesChoice.destinationId = jawabYa.id
        yesChoice.choicePhrase = "YA"

        val noChoice = Pilihan()
        noChoice.sourceId = mula.id
        noChoice.destinationId = jawabTak.id
        noChoice.choicePhrase = "TAK"

        val startChoice01 = Pilihan()
        startChoice01.sourceId = jawabYa.id
        startChoice01.destinationId = mula.id
        startChoice01.choicePhrase = "Go to beginning!"

        val startChoice02 = Pilihan()
        startChoice02.sourceId = jawabTak.id
        startChoice02.destinationId = mula.id
        startChoice02.choicePhrase = "Go to beginning!"

        graph.addChoice(yesChoice)
        graph.addChoice(noChoice)
        graph.addChoice(startChoice01)
        graph.addChoice(startChoice02)

        println(graph)
        println(graph.displayCurrentConversation())
        val json = Json()
        json.setOutputType(JsonWriter.OutputType.json)
        println(json.prettyPrint(graph))

        while (input != "q") {
            val conversation = showNextChoices(graph)
            if (input == "q") break
            if (conversation.id == -1) {
                println(graph.displayCurrentConversation())
                continue
            }
            graph.setCurrentConversation(conversation.id)
            println(graph.displayCurrentConversation())
        }
    }

    private fun showNextChoices(graf: Graf): Perbualan {
        val choices = graf.getCurrentChoices()
        for (pilihan in choices) {
            println("${pilihan.destinationId}  ${pilihan.choicePhrase}")
        }

        val br = BufferedReader(InputStreamReader(System.`in`))
        input = br.readLine()

        val nomborId: Int = try {
            Integer.parseInt(input)
        } catch (nfe: NumberFormatException) {
            println("$input ditekan, q untuk quit")
            -1
        }
        val choice: Perbualan? = graf.getConversationByID(nomborId)
        return choice ?: Perbualan()
    }
}