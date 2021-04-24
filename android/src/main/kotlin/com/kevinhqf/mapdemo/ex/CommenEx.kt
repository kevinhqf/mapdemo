package com.kevinhqf.mapdemo.ex

import com.google.gson.Gson
import com.google.gson.GsonBuilder

val gson: Gson = GsonBuilder().serializeNulls().create()

/**
 * 使用字段来序列化json的方法
 */
fun Any.toFieldJson(): String {
    return gson.toJson(this)
}