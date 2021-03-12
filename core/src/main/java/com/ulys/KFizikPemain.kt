package com.ulys

class KFizikPemain : KomponenFizik() {

    override fun kemaskini(delta: Float, entiti: Entiti, pengurusPeta: PengurusPeta) {
        super.kemaskini(delta, entiti, pengurusPeta)

        val kamera = pengurusPeta.kamera
        kamera.position.set(pos.x, pos.y, 0f)
        kamera.update()
    }

}