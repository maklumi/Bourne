package com.ulys.ui

import com.ulys.Konfigurasi

interface Bualan {

    enum class UIEvent {
        LOAD_CONVERSATION,
        SHOW_CONVERSATION,
        HIDE_CONVERSATION,
    }

    fun onBual(k: Konfigurasi, event: UIEvent)

}