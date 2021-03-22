package com.ulys

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import java.util.*

abstract class Peta(var jenisPeta: PengeluarPeta.JenisPeta, pathPeta: String) {

    lateinit var tiledMap: TiledMap
    var collisionLayer: MapLayer
    var portalLayer: MapLayer
    var spawnsLayer: MapLayer
    var questItemSpawnLayer: MapLayer?
    var questDiscoverLayer: MapLayer?

    var posisiMula = Vector2()
    var posisiMulaNPCs: Array<Vector2>
    val semuaEntiti = Array<Entiti>()
    protected val jadualPosEntitiSpesyel = Hashtable<String, Vector2>()

    init {
        Util.muatAsetPeta(pathPeta)
        if (Util.asetDimuat(pathPeta)) {
            tiledMap = Util.getAsetPeta(pathPeta)
        }

        collisionLayer = tiledMap.layers.get(MAP_COLLISION_LAYER)
        portalLayer = tiledMap.layers.get(MAP_PORTAL_LAYER)
        spawnsLayer = tiledMap.layers.get(MAP_SPAWNS_LAYER)
        questItemSpawnLayer = tiledMap.layers.get(QUEST_ITEM_SPAWN_LAYER)
        questDiscoverLayer = tiledMap.layers.get(QUEST_DISCOVER_LAYER)

        cacheTempatSpawnHampir(posisiMula)
        posisiMulaNPCs = dapatkanLokasiMulaSemuaNPC()
        dapatLokasiMulaEntitiSpesyel()
    }

    open fun updateMapEntities(delta: Float, batch: Batch, pengurusPeta: PengurusPeta) {
        for (i in 0 until semuaEntiti.size) {
            semuaEntiti[i].kemaskini(delta, batch, pengurusPeta)
        }
    }

    private fun dapatkanLokasiMulaSemuaNPC(): Array<Vector2> {
        val positions = Array<Vector2>()
        spawnsLayer.objects.filter { it.name == "NPC_START" }
            .map {
                it as RectangleMapObject
                Vector2(
                    it.rectangle.x + it.rectangle.width / 2,
                    it.rectangle.y + it.rectangle.height / 2
                ) // get center of rectangle
            }
            .map { it.scl(kpp) } // convert from map coordinates
            .forEach { positions.add(it) }
        return positions
    }

    private fun dapatLokasiMulaEntitiSpesyel() {
        spawnsLayer.objects.filterNot {
            it.name == "NPC_START" ||
                    it.name == "PLAYER_START" ||
                    it.name.isNullOrEmpty()
        }
            .forEach {
                it as RectangleMapObject
                val lokasi = Vector2(
                    it.rectangle.x + it.rectangle.width / 2,
                    it.rectangle.y + it.rectangle.height / 2
                ).scl(kpp)
                jadualPosEntitiSpesyel[it.name] = lokasi
            }
    }

    fun getQuestItemSpawnPositions(objectName: String, objectTaskID: String): Array<Vector2> {
        val positions = Array<Vector2>()
        val mapObjects: MapObjects = questItemSpawnLayer!!.objects

        for (mapObject in mapObjects) {
            val name = mapObject.name
            val taskID = mapObject.properties.get("taskID") as String?

            if (name == null || taskID == null ||
                name.isEmpty() || taskID.isEmpty() ||
                !name.equals(objectName, true) ||
                !taskID.equals(objectTaskID, true)
            ) {
                continue
            }
            //Get center of rectangle
            var x = (mapObject as RectangleMapObject).rectangle.getX()
            var y = mapObject.rectangle.getY()

            //scale by the unit to convert from map coordinates
            x *= kpp
            y *= kpp

            positions.add(Vector2(x, y))
        }
        return positions
    }

    fun addMapEntities(entities: Array<Entiti>) {
        semuaEntiti.addAll(entities)
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
//                    Gdx.app.debug("Peta", "closest START is: $spawnPos in $jenisPeta")
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
        private const val QUEST_ITEM_SPAWN_LAYER = "MAP_QUEST_ITEM_SPAWN_LAYER"
        private const val QUEST_DISCOVER_LAYER = "MAP_QUEST_DISCOVER_LAYER"

        private const val PLAYER_START = "PLAYER_START"
        const val kpp = 1 / 16f // 1 kaki = 16 piksel


        fun initEntity(entityConfig: Konfigurasi, position: Vector2): Entiti {
            val entity = Pengeluar.NPC.get()
            entity.konfigurasi = entityConfig

            entity.apply {
                posMesej(Penerima.Mesej.MUAT_ANIMASI, j.toJson(entity.konfigurasi))
                posMesej(Penerima.Mesej.POS_MULA, j.toJson(position))
                posMesej(Penerima.Mesej.GERAK_MULA, j.toJson(entity.konfigurasi.state))
                posMesej(Penerima.Mesej.ARAH_MULA, j.toJson(entity.konfigurasi.direction))
            }
            return entity
        }
    }
}