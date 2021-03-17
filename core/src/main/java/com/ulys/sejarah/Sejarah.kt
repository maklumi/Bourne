package com.ulys.sejarah

import com.badlogic.gdx.utils.Array

abstract class Sejarah {

    private val array = Array<Profil>()

    fun addMurid(profil: Profil) {
        array.add(profil)
    }

    fun ajar(event: Profil.ProfileEvent) {
        for (murid in array) {
            murid.onTerima(event)
        }
    }

}