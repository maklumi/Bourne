package com.ulys

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2

abstract class Peta(private val jenisPeta: PengeluarPeta.JenisPeta, val pathPeta: String) {

    lateinit var tiledMap: TiledMap
    lateinit var collisionLayer: MapLayer
    lateinit var portalLayer: MapLayer
    lateinit var spawnsLayer: MapLayer

    var posisiMula = Vector2()

    init {
        setupPeta()
    }

    private fun setupPeta() {
        Util.muatAsetPeta(pathPeta)
        if (Util.asetDimuat(pathPeta)) {
            tiledMap = Util.getAsetPeta(pathPeta)
        }

        collisionLayer = tiledMap.layers.get(MAP_COLLISION_LAYER)
        portalLayer = tiledMap.layers.get(MAP_PORTAL_LAYER)
        spawnsLayer = tiledMap.layers.get(MAP_SPAWNS_LAYER)

        cacheTempatSpawnHampir(posisiMula)
    }

    fun cacheTempatSpawnHampir(pos: Vector2) {
        val posPemain = pos.cpy().scl(1 / kpp)
        val rectVector = Vector2()
        val spawnPos = Vector2()
        var jarakTerdekat = Float.MAX_VALUE

        //Go through all player start positions and choose closest to last known position
        for (mapObject in spawnsLayer.objects) {
            if (mapObject.name.equals(PLAYER_START, ignoreCase = true)) {
                (mapObject as RectangleMapObject).rectangle.getPosition(rectVector)
                val distance = posPemain.dst2(rectVector)
//                Gdx.app.debug("Peta", "distance:$distance peta:$jenisPeta  from:$posPemain to:$rectVector")
                if (distance < jarakTerdekat) {
                    spawnPos.set(rectVector.scl(kpp))
                    jarakTerdekat = distance
                    Gdx.app.debug("Peta", "closest START is: $spawnPos in $jenisPeta")
                }
            }
        }
        // cache dalam unit kaki
        posisiMula = spawnPos.cpy()
    }

    companion object {
        //Map layers
        private const val MAP_COLLISION_LAYER = "MAP_COLLISION_LAYER"
        private const val MAP_SPAWNS_LAYER = "MAP_SPAWNS_LAYER"
        private const val MAP_PORTAL_LAYER = "MAP_PORTAL_LAYER"

        private const val PLAYER_START = "PLAYER_START"
        const val kpp = 1 / 16f // 1 kaki = 16 piksel
    }
}