package com.ulys.ui

interface Transaksi {

    enum class SlotEvent {
        ADDED_ITEM,
        REMOVED_ITEM
    }

    fun onTransaksi(slot: Slot, event: SlotEvent)

}