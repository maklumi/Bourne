package com.ulys.ui

import com.badlogic.gdx.utils.Array

interface StatusSubject {

    val statusObservers: Array<StatusObserver>

    fun addStatusObserver(observer: StatusObserver) {
        statusObservers.add(observer)
    }

    fun removeObserver(observer: StatusObserver) {
        statusObservers.removeValue(observer, true)
    }

    fun removeAllObservers() {
        statusObservers.forEach { removeObserver(it) }
    }

    fun notify(value: Int, event: StatusObserver.StatusEvent) {
        statusObservers.forEach { it.onNotify(value, event) }
    }

}