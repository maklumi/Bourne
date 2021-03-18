package com.ulys

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3

class KFizikNPC : KomponenFizik() {

    override var boundingBoxLocation = BoundingBoxLocation.CENTER

    override fun kemaskini(delta: Float, entiti: Entiti, pengurusPeta: PengurusPeta) {
        setNextBoundSize(0.6f, 0.85f)

        if (!akanBerlagaDenganLayer(entiti, nextRect, pengurusPeta.collisionLayer)
            && !berlagaPortalLayer(nextRect, pengurusPeta)
            && !akanBerlagaEntiti(entiti, pengurusPeta.semuaEntiti)
            && !akanBerlagaEntiti(entiti, pengurusPeta.entitiPemain)
            && gerak == Entiti.Gerak.WALKING
        ) {
            setCalculatedPosAsCurrent(entiti)
        }
        if (gerak == Entiti.Gerak.WALKING) kiraPosisi(delta, arah)

        if ((entiti.komponenGrafik as KGrafikNPC).sedangDipilih &&
            iaJauhDariPemain(pengurusPeta)
        ) {
            entiti.posMesej(Penerima.Mesej.ENTITI_TAK_DIPILIH)
        }
    }

    private fun berlagaPortalLayer(rect: Rectangle, pengurusPeta: PengurusPeta): Boolean {
        val mapObjects = pengurusPeta.portalLayer.objects
        return mapObjects.filterIsInstance(RectangleMapObject::class.java)
            .any { rect.overlaps(it.rectangle) }
    }

    private fun iaJauhDariPemain(pengurusPeta: PengurusPeta): Boolean {
        val pbox = pengurusPeta.entitiPemain[0].komponenFizik.nextRect
        val origin = Vector3(pbox.x, pbox.y, 0.0f)
        val destination = Vector3(nextRect.x, nextRect.y, 0f)
        ray.set(origin, destination)
        val distance = ray.origin.dst(destination)
        return distance > lingkungan
    }
}