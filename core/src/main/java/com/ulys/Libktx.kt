package com.ulys

import com.badlogic.gdx.utils.Json

val j = Json()

inline fun <reified T> Json.fromJson(string: String): T = fromJson(T::class.java, string)

inline fun <reified T> fromJson(string: String): T = j.fromJson(string)

fun <T> toJson(objek: T): String = j.toJson(objek)