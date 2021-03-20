package com.ulys

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.ulys.PengeluarPeta.JenisPeta
import com.ulys.PengeluarPeta.JenisPeta.*
import com.ulys.PengeluarPeta.getPeta
import com.ulys.sejarah.Penyelia
import com.ulys.sejarah.Penyelia.setProp
import com.ulys.sejarah.Profil
import com.ulys.sejarah.Profil.ProfileEvent.PROFILE_LOADED
import com.ulys.sejarah.Profil.ProfileEvent.SAVING_PROFILE

class PengurusPeta : Profil {

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
    var currentSelectedEntity: Entiti? = null

    fun setupPeta(jenisPeta: JenisPeta) {
        clearCurrentSelectedMapEntity()
        peta = getPeta(jenisPeta)
        semuaEntiti.forEach(Entiti::unregisterPendengarBualan)
    }

    fun clearCurrentSelectedMapEntity() {
        currentSelectedEntity?.posMesej(Penerima.Mesej.ENTITI_TAK_DIPILIH)
        currentSelectedEntity = null
    }

    fun cacheTempatSpawnHampir(pos: Vector2) {
        peta.cacheTempatSpawnHampir(pos)
    }

    fun updateMapEntities(delta: Float, batch: Batch, pengurusPeta: PengurusPeta) {
        peta.updateMapEntities(delta, batch, pengurusPeta)
    }

    override fun onTerima(event: Profil.ProfileEvent) {
        when (event) {
            PROFILE_LOADED -> {
                val jenisPeta = Penyelia.getProp<String>("currentMapType")
                val mapType = if (jenisPeta.isNullOrEmpty())
                    TOWN else JenisPeta.valueOf(jenisPeta)
                setupPeta(mapType)
                berpindah = true

                // Persisted the closest player position values for different maps
                val topWorldMapStartPosition = Penyelia.getProp<Vector2>("topWorldMapStartPosition")
                if (topWorldMapStartPosition != null) {
                    getPeta(TOP_WORLD).posisiMula = topWorldMapStartPosition
                }

                val castleOfDoomMapStartPosition = Penyelia.getProp<Vector2>("castleOfDoomMapStartPosition")
                if (castleOfDoomMapStartPosition != null) {
                    getPeta(CASTLE_OF_DOOM).posisiMula = castleOfDoomMapStartPosition
                }

                val townMapStartPosition = Penyelia.getProp<Vector2>("townMapStartPosition")
                if (townMapStartPosition != null) {
                    getPeta(TOWN).posisiMula = townMapStartPosition
                }

            }
            SAVING_PROFILE -> {
                setProp("currentMapType", peta.jenisPeta.name)
                setProp("topWorldMapStartPosition", getPeta(TOP_WORLD).posisiMula)
                setProp("castleOfDoomMapStartPosition", getPeta(CASTLE_OF_DOOM).posisiMula)
                setProp("townMapStartPosition", getPeta(TOWN).posisiMula)
            }
        }
    }
}