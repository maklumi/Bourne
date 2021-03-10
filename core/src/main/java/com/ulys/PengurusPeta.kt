package com.ulys

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2

class PengurusPeta(private val util: Util) {

    private val jadualPeta = hashMapOf(
        TOP_WORLD to "maps/topworld.tmx",
        TOWN to "maps/town.tmx",
        CASTLE_OF_DOOM to "maps/castle_of_doom.tmx"
    )
    private val jadualPosisiAwal = hashMapOf(
        TOP_WORLD to Vector2(),
        TOWN to Vector2(),
        CASTLE_OF_DOOM to Vector2()
    )
    lateinit var peta: TiledMap
    private var namaPeta = TOWN
    lateinit var collisionLayer: MapLayer
    lateinit var portalLayer: MapLayer
    lateinit var spawnsLayer: MapLayer

    val posisiMula: Vector2
        get() = jadualPosisiAwal.getOrDefault(namaPeta, Vector2())

    fun setupPeta(nama: String) {
        val path = jadualPeta.getOrDefault(nama, TOWN)
        util.muatAsetPeta(path)
        if (util.asetDimuat(path)) {
            namaPeta = nama
            peta = util.getAsetPeta(path)
        }

        collisionLayer = peta.layers.get(MAP_COLLISION_LAYER)
        portalLayer = peta.layers.get(MAP_PORTAL_LAYER)
        spawnsLayer = peta.layers.get(MAP_SPAWNS_LAYER)

        var tempatMula = jadualPosisiAwal.getOrDefault(nama, Vector2())
        if (tempatMula.isZero) {
            cacheTempatSpawnHampir(tempatMula)
            tempatMula = jadualPosisiAwal.getOrDefault(nama, Vector2())
        }
        jadualPosisiAwal[namaPeta] = tempatMula
//        Gdx.app.debug("PengurusPeta", "posisiSpawn $namaPeta $tempatMula kaki")
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
//                Gdx.app.debug("PengurusPeta", "distance:$distance peta:$namaPeta  from:$posPemain to:$rectVector")
                if (distance < jarakTerdekat) {
                    spawnPos.set(rectVector.scl(kpp))
                    jarakTerdekat = distance
                    Gdx.app.debug("PengurusPeta", "closest START is: $spawnPos in $namaPeta")
                }
            }
        }
        // cache dalam unit kaki
        jadualPosisiAwal[namaPeta] = spawnPos.cpy()
    }

    companion object {
        //maps
        const val TOP_WORLD = "TOP_WORLD"
        const val TOWN = "TOWN"
        const val CASTLE_OF_DOOM = "CASTLE_OF_DOOM"

        //Map layers
        private const val MAP_COLLISION_LAYER = "MAP_COLLISION_LAYER"
        private const val MAP_SPAWNS_LAYER = "MAP_SPAWNS_LAYER"
        private const val MAP_PORTAL_LAYER = "MAP_PORTAL_LAYER"

        private const val PLAYER_START = "PLAYER_START"
        const val kpp = 1 / 16f // 1 kaki = 16 piksel
    }
}