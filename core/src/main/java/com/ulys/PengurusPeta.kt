package com.ulys

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array

class PengurusPeta {

    lateinit var kamera: OrthographicCamera
    var berpindah = false

    lateinit var peta: Peta
    val tiledMap: TiledMap get() = peta.tiledMap
    val collisionLayer: MapLayer get() = peta.collisionLayer
    val portalLayer: MapLayer get() = peta.portalLayer
    val spawnsLayer: MapLayer get() = peta.spawnsLayer

    val posisiMula: Vector2 get() = peta.posisiMula
    val semuaEntiti: Array<Entiti> get() = peta.semuaEntiti
    var entitiPemain: Array<Entiti> = Array(1)

    fun setupPeta(jenisPeta: PengeluarPeta.JenisPeta) {
        peta = PengeluarPeta.getPeta(jenisPeta)
    }

    fun cacheTempatSpawnHampir(pos: Vector2) {
        peta.cacheTempatSpawnHampir(pos)
    }

    fun updateMapEntities(delta: Float, batch: Batch, pengurusPeta: PengurusPeta) {
        peta.updateMapEntities(delta, batch, pengurusPeta)
    }
}