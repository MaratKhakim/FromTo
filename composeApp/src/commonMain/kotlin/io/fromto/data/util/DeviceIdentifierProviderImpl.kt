package io.fromto.data.util

import com.russhwolf.settings.Settings
import io.fromto.domain.util.DeviceIdentifierProvider
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DeviceIdentifierProviderImpl(
    private val settings: Settings
) : DeviceIdentifierProvider {
    companion object {
        private const val KEY_DEVICE_ID = "customDeviceId"
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getDeviceId(): String {
        return settings.getStringOrNull(KEY_DEVICE_ID) ?: run {
            val newId = Uuid.random().toString()
            settings.putString(KEY_DEVICE_ID, newId)
            newId
        }
    }
}