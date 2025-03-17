package io.fromto.analytics

import androidx.compose.runtime.compositionLocalOf

val LocalAnalytics = compositionLocalOf<Analytics> { error("Analytics not provided") }
