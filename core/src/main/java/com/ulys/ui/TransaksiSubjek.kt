package com.ulys.ui

import com.badlogic.gdx.utils.Array

interface TransaksiSubjek {

    val lisTransaksi: Array<Transaksi>

    fun addTransaksi(transaksi: Transaksi) {
        lisTransaksi.add(transaksi)
    }

    fun removeTransaksi(transaksi: Transaksi) {
        lisTransaksi.removeValue(transaksi, true)
    }

    fun removeSemuaTransaksi() {
        lisTransaksi.forEach { removeTransaksi(it) }
    }

    fun transaksi(slot: Slot, event: Transaksi.SlotEvent) {
        lisTransaksi.forEach { it.onTransaksi(slot, event) }
    }

}