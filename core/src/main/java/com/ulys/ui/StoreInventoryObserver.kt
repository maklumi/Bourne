package com.ulys.ui

interface StoreInventoryObserver {

    enum class StoreInventoryEvent {
        PLAYER_GP_TOTAL_UPDATED,
        PLAYER_INVENTORY_UPDATED
    }

    fun onNotify(value: String, event: StoreInventoryEvent)

}