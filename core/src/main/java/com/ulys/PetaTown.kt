package com.ulys

import com.ulys.PengeluarPeta.JenisPeta

class PetaTown : Peta(JenisPeta.TOWN, "maps/town.tmx") {

    init {
        for (i in 0 until posisiMulaNPCs.size) {
            val npc = Pengeluar.NPC.get()
            npc.muatKonfigurasiGrafik("scripts/town_guard_walking.json")
            npc.posMesej(Penerima.Mesej.MUAT_ANIMASI, j.toJson(npc.konfigurasi))
            npc.posMesej(Penerima.Mesej.POS_MULA, j.toJson(posisiMulaNPCs[i]))
            semuaEntiti.add(npc)
        }
    }

}