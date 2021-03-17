package com.ulys

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.ulys.skrin.SkrinBaru
import com.ulys.skrin.SkrinLoad
import com.ulys.skrin.SkrinMenu
import com.ulys.skrin.SkrinUtama

class Bourne : Game() {

    lateinit var skrinMenu: SkrinMenu
    lateinit var skrinLoad: SkrinLoad
    lateinit var skrinBaru: SkrinBaru
    lateinit var skrinUtama: SkrinUtama

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        skrinMenu = SkrinMenu(this)
        skrinLoad = SkrinLoad(this)
        skrinBaru = SkrinBaru(this)
        skrinUtama = SkrinUtama()
        setScreen(skrinMenu)
    }

    override fun dispose() {
        skrinUtama.dispose()
    }

    enum class JenisSkrin {
        SKRIN_MENU,
        SKRIN_LOAD,
        SKRIN_BARU,
        SKRIN_UTAMA,
    }

    fun getSkrin(jenisSkrin: JenisSkrin): Screen {
        return when (jenisSkrin) {
            JenisSkrin.SKRIN_MENU -> skrinMenu
            JenisSkrin.SKRIN_LOAD -> skrinLoad
            JenisSkrin.SKRIN_BARU -> skrinBaru
            JenisSkrin.SKRIN_UTAMA -> skrinUtama
        }
    }
}