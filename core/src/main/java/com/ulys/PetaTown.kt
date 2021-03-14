package com.ulys

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.ulys.PengeluarPeta.JenisPeta

class PetaTown : Peta(JenisPeta.TOWN, "maps/town.tmx") {

    private var counter = 1

    init {
        for (i in 0 until posisiMulaNPCs.size) {
            val npc = setupEntiti(posisiMulaNPCs[i], Entiti.muatKonfigurasi("scripts/town_guard_walking.json"))
            semuaEntiti.add(npc)
        }
        // OEntiti spesyel
        semuaEntiti.add(setupSpesyel(Entiti.muatKonfigurasi("scripts/town_blacksmith.json")))
        semuaEntiti.add(setupSpesyel(Entiti.muatKonfigurasi("scripts/town_mage.json")))
        semuaEntiti.add(setupSpesyel(Entiti.muatKonfigurasi("scripts/town_innkeeper.json")))
        // Guna fail yang ada multiple konfigurasi dalam satu fail
        val townFolkConfigs = Entiti.muatKonfigurasiMulti("scripts/town_folk.json")
        townFolkConfigs.forEach { semuaEntiti.add(setupSpesyel(it)) }
    }

    private fun setupEntiti(posisiMula: Vector2, konf: Konfigurasi): Entiti {
        val npc = Pengeluar.NPC.get()
        npc.konfigurasi = konf
        npc.posMesej(Penerima.Mesej.MUAT_ANIMASI, j.toJson(konf))
        npc.posMesej(Penerima.Mesej.POS_MULA, j.toJson(posisiMula))
        npc.posMesej(Penerima.Mesej.GERAK_MULA, j.toJson(konf.state))
        npc.posMesej(Penerima.Mesej.ARAH_MULA, j.toJson(konf.direction))
        return npc
    }

    private fun setupSpesyel(konf: Konfigurasi): Entiti {
        jadualPosEntitiSpesyel[konf.entityID]?.let { pos ->
            return setupEntiti(pos, konf)
        }
        Gdx.app.debug("PetaTown", "${konf.entityID} tiada lokasi spawn")
        counter++
        return setupEntiti(Vector2(counter.toFloat(), -3f), konf) // letak kat luar
    }
}
/*
Player0.png
contoh town_mage posisi (baris = 3, kolumn = 6)
susunan gambar
(0,0),(0,1),(0,2),(0,3)..
(1,0),(1,1),(1,2)..
(2,0)..
 */