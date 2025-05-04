package com.robotbot.finance_tracker_client.remote.util

import android.annotation.SuppressLint
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateTypeAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    @SuppressLint("NewApi")
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @SuppressLint("NewApi")
    override fun serialize(
        src: LocalDate,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.format(formatter))
    }

    @SuppressLint("NewApi")
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): LocalDate {
        return LocalDate.parse(json.asString)
    }
}
