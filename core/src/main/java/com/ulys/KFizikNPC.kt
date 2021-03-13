package com.ulys

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle

class KFizikNPC : KomponenFizik() {

    override var boundingBoxLocation = BoundingBoxLocation.CENTER

    override fun kemaskini(delta: Float, entiti: Entiti, pengurusPeta: PengurusPeta) {
        setNextBoundSize(0.6f, 0.85f)

        if (!akanBerlagaDenganLayer(entiti, nextRect, pengurusPeta.collisionLayer)
            && !berlagaPortalLayer(nextRect, pengurusPeta)
            && gerak == Entiti.Gerak.JALAN
        ) {
            setCalculatedPosAsCurrent(entiti)
        }
        if (gerak == Entiti.Gerak.JALAN) kiraPosisi(delta, arah)

    }

    private fun berlagaPortalLayer(rect: Rectangle, pengurusPeta: PengurusPeta): Boolean {
        val mapObjects = pengurusPeta.portalLayer.objects
        return mapObjects.filterIsInstance(RectangleMapObject::class.java)
            .any { rect.overlaps(it.rectangle) }
    }

}