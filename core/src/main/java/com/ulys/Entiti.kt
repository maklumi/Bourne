package com.ulys

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.JsonValue
import com.ulys.ui.BualSubjek
import com.ulys.ui.Bualan
import java.util.*

class Entiti(
    val komponenInput: KomponenInput,
    private val komponenFizik: KomponenFizik,
    private val komponenGrafik: KomponenGrafik
) : BualSubjek() {

    private val lisKomponen = arrayOf(
        komponenInput,
        komponenGrafik,
        komponenFizik
    )

    enum class Arah { LEFT, RIGHT, UP, DOWN }
    enum class Gerak {
        IDLE, WALKING, IMMOBILE;

        companion object {
            fun randomDiamAtauJalan(): Gerak {
                return values()[MathUtils.random(values().size - 2)]
            }
        }
    }

    enum class AnimationType {
        WALK_LEFT, WALK_RIGHT, WALK_UP, WALK_DOWN,
        IDLE, IMMOBILE
    }

    val rect: Rectangle
        get() = komponenFizik.nextRect

    var konfigurasi: Konfigurasi = Konfigurasi()

    fun kemaskini(delta: Float, batch: Batch, pengurusPeta: PengurusPeta) {
        komponenInput.kemaskini(delta, this)
        komponenFizik.kemaskini(delta, this, pengurusPeta)
        komponenGrafik.kemaskini(delta, this, batch, pengurusPeta)
    }

    fun posMesej(kod: Penerima.Mesej, vararg args: String) {
        val mesej = args.fold(kod.name, { acc, s -> acc + Penerima.PEMISAH + s })
        for (i in lisKomponen.indices) {
            lisKomponen[i].terima(mesej)
        }
    }

    fun registerPendengarBualan(observer: Bualan) {
        addPendengar(observer)
    }

    fun unregisterPendengarBualan() {
        removeSemuaPendengar()
    }

    fun dispose() {
        for (i in lisKomponen.indices) {
            lisKomponen[i].dispose()
        }
    }

    companion object {
        fun muatKonfigurasi(path: String): Konfigurasi {
            return j.fromJson(Konfigurasi::class.java, Gdx.files.internal(path))
        }

        fun muatKonfigurasiMulti(path: String): Array<Konfigurasi> {
            val jsonValues = j.fromJson(ArrayList::class.java, Gdx.files.internal(path))
            val configs = Array<Konfigurasi>()
            for (jsonValue in jsonValues) {
                val config = j.readValue(Konfigurasi::class.java, jsonValue as JsonValue?)
                configs.add(config)
            }
            return configs
        }


        const val LEBAR_FREM = 16
        const val TINGGI_FREM = 16
    }
}