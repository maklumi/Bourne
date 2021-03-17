package com.ulys.sejarah

interface Profil {

    enum class ProfileEvent {
        PROFILE_LOADED,
        SAVING_PROFILE,
    }

    fun onTerima(event: ProfileEvent)

}