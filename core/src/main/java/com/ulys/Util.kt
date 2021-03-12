package com.ulys

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader

object Util {

    val man = AssetManager()

    fun muatAsetPeta(path: String) {
        val resolver = InternalFileHandleResolver()
        if (resolver.resolve(path).exists()) {
            man.setLoader(TiledMap::class.java, TmxMapLoader(resolver))
            man.load(path, TiledMap::class.java)
            man.finishLoadingAsset<TiledMap>(path)
            Gdx.app.debug("Util19", "Peta dimuat: $path")
        } else {
            Gdx.app.debug("Util21", "Peta gagal dimuat: $path")
        }
    }

    fun getAsetPeta(path: String): TiledMap {
        return if (man.isLoaded(path)) {
            man.get(path, TiledMap::class.java)
        } else {
            Gdx.app.debug("Util29", "Tak dapat peta : $path")
            TiledMap()
        }
    }

    fun muatAsetTekstur(path: String) {
        val resolver = InternalFileHandleResolver()
        if (resolver.resolve(path).exists()) {
            man.setLoader(Texture::class.java, TextureLoader(resolver))
            man.load(path, Texture::class.java)
            man.finishLoadingAsset<Texture>(path)
            Gdx.app.debug("Util42", "Tekstur dimuat: $path")
        } else {
            Gdx.app.debug("Util44", "Tekstur gagal dimuat: $path")
        }
    }

    fun getAsetTekstur(path: String): Texture {
        if (!man.isLoaded(path)) muatAsetTekstur(path)
        return if (man.isLoaded(path)) {
            man.get(path, Texture::class.java)
        } else {
            Gdx.app.debug("Util53", "Tak dapat tekstur : $path")
            Texture(16, 16, Pixmap.Format.RGBA8888)
        }
    }

    fun dispose(path: String) {
        if (man.isLoaded(path)) {
            man.unload(path)
        } else {
            Gdx.app.debug("Util38", "Aset tak perlu dinyah: $path")
        }
    }

    fun asetDimuat(path: String): Boolean {
        return man.isLoaded(path)
    }
}