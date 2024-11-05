package com.developbharat.yogsudhaar.common

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.toRoute
import com.developbharat.yogsudhaar.domain.models.Asana
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Serializable
sealed interface Screens {
    @Serializable
    data object HomeScreen : Screens

    @Serializable
    data class CheckScreen(val asana: Asana) : Screens {
        companion object {
            val typeMap = mapOf(typeOf<Asana>() to serializableType<Asana>())
            fun from(savedStateHandle: SavedStateHandle) = savedStateHandle.toRoute<CheckScreen>(typeMap)
        }
    }
}


inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        bundle.getString(key)?.let<String, T>(json::decodeFromString)

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = json.encodeToString(value)

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, json.encodeToString(value))
    }
}