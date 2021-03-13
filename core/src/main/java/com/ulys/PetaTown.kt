package com.ulys

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.ulys.PengeluarPeta.JenisPeta

class PetaTown : Peta(JenisPeta.TOWN, "maps/town.tmx") {

    private var counter = 1

    init {
        for (i in 0 until posisiMulaNPCs.size) {
            val npc = setupEntiti(posisiMulaNPCs[i], "scripts/town_guard_walking.json")
            semuaEntiti.add(npc)
        }
        // Entiti spesyel
        semuaEntiti.add(setupSpesyel("TOWN_BLACKSMITH", "scripts/town_blacksmith.json"))
        semuaEntiti.add(setupSpesyel("TOWN_MAGE", "scripts/town_mage.json"))
        semuaEntiti.add(setupSpesyel("TOWN_INNKEEPER", "scripts/town_innkeeper.json"))
    }

    private fun setupEntiti(posisiMula: Vector2, path: String): Entiti {
        val npc = Pengeluar.NPC.get()
        npc.muatKonfigurasiGrafik(path)
        npc.posMesej(Penerima.Mesej.MUAT_ANIMASI, j.toJson(npc.konfigurasi))
        npc.posMesej(Penerima.Mesej.POS_MULA, j.toJson(posisiMula))
        npc.posMesej(Penerima.Mesej.GERAK_MULA, j.toJson(npc.konfigurasi?.state))
        npc.posMesej(Penerima.Mesej.ARAH_MULA, j.toJson(npc.konfigurasi?.direction))
        return npc
    }

    private fun setupSpesyel(namaPetak: String, path: String): Entiti {
        jadualPosEntitiSpesyel[namaPetak]?.let { pos ->
            return setupEntiti(pos, path)
        }
        Gdx.app.debug("PetaTown", "$namaPetak tiada lokasi spawn")
        counter++
        return setupEntiti(Vector2(counter.toFloat(), -3f), path) // letak kat luar
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