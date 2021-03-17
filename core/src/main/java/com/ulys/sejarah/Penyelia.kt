package com.ulys.sejarah

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.ObjectMap
import com.ulys.j
import java.util.*

object Penyelia : Sejarah() {

    private val profiles = Hashtable<String, FileHandle>().also {
        if (Gdx.files.isLocalStorageAvailable) {
            val files = Gdx.files.local(".").list(".sav")
            for (file in files) {
                it[file.nameWithoutExtension()] = file
            }
        }
    }
    private var profileName: String = "default"
    private var profileProperties = ObjectMap<String, Any>()

    fun setProp(key: String, objek: Any) {
        profileProperties.put(key, objek)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getProp(key: String): T? {
        return profileProperties.get(key) as T?
    }

    @Suppress("UNCHECKED_CAST")
    fun loadProfile(profileName: String = this.profileName) {
        val fullProfileFileName = "$profileName.sav"
        val doesProfileFileExist = Gdx.files.internal(fullProfileFileName).exists()
        if (!doesProfileFileExist) {
            println("File doesn't exist!")
            return
        }

        this.profileName = profileName
        profiles[profileName] = Gdx.files.internal(fullProfileFileName)
        profileProperties = j.fromJson(ObjectMap::class.java, profiles[profileName]) as ObjectMap<String, Any>
        ajar(Profil.ProfileEvent.PROFILE_LOADED)
        Gdx.app.debug("PenyeliaSejarah", Profil.ProfileEvent.PROFILE_LOADED.name)
    }

    fun saveProfile() {
        val text = j.prettyPrint(j.toJson(profileProperties))
        writeProfileToStorage(profileName, text, true)
//        ajar(Murid.ProfileEvent.SAVING_PROFILE)
        Gdx.app.debug("PenyeliaSejarah", text)
    }

    private fun writeProfileToStorage(profileName: String, fileData: String, overwrite: Boolean) {
        val localFileExists = Gdx.files.internal("$profileName.sav").exists()

        //If we cannot overwrite and the file exists, exit
        if (localFileExists && !overwrite) {
            return
        }

        var file: FileHandle? = null
        if (Gdx.files.isLocalStorageAvailable) {
            file = Gdx.files.local("$profileName.sav")
            file?.writeString(fileData, !overwrite)
        }

        profiles[profileName] = file
    }

}