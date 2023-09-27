package com.tencent.joox.sdk.utils.gson

import android.util.Log
import com.google.gson.JsonParser
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class RawStringJsonAdapter : TypeAdapter<String>() {

    companion object{
        private const val TAG = "RawStringJsonAdapter"
    }
    override fun write(out: JsonWriter?, value: String?) {
        try {
            out?.jsonValue(value)
        }catch (e:Exception){
            Log.d(TAG, "write fail:${e.toString()}")
        }
    }

    override fun read(`in`: JsonReader?): String {
        try {
            return JsonParser().parse(`in`).toString()
        }catch (e:Exception){
            Log.d(TAG, "read fail:${e.toString()}")
        }
        return ""
    }
}