package com.srk.srklocationservices.utils

import com.google.gson.*
import java.lang.reflect.Type
import java.util.*

val retrofitGson: Gson = GsonBuilder()
    .registerTypeAdapter(
        Date::class.java,
        JsonDeserializer { json: JsonElement, _: Type?, _: JsonDeserializationContext? ->
            Date(json.asJsonPrimitive.asLong)
        } as JsonDeserializer<Date?>?
    )
    .create()