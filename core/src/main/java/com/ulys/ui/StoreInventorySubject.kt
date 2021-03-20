package com.ulys.ui

import com.badlogic.gdx.utils.Array

interface StoreInventorySubject {

    val storeInventoryObservers: Array<StoreInventoryObserver>

    fun addStoreInventoryObserver(observer: StoreInventoryObserver) {
        storeInventoryObservers.add(observer)
    }

    fun removeObserver(observer: StoreInventoryObserver) {
        storeInventoryObservers.removeValue(observer, true)
    }

    fun removeAllObservers() {
        storeInventoryObservers.forEach { removeObserver(it) }
    }

    fun notify(value: String, event: StoreInventoryObserver.StoreInventoryEvent) {
        storeInventoryObservers.forEach { it.onNotify(value, event) }
    }
}