package com.ulys.ui

interface StatusObserver {
    enum class StatusEvent {
        UPDATED_GP,
        UPDATED_LEVEL,
        UPDATED_HP,
        UPDATED_MP,
        UPDATED_XP
    }

    fun onNotify(value: Int, event: StatusEvent)
}