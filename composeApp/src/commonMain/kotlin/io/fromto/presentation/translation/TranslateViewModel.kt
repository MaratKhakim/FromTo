package io.fromto.presentation.translation

import androidx.lifecycle.ViewModel
import io.fromto.getPlatform

class TranslateViewModel(
    private val language: String
) : ViewModel() {

    fun translate(): String {
        val platform = getPlatform()
        return "$language ${platform.name}!"
    }
}