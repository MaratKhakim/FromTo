package io.fromto.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import io.fromto.analytics.Analytics
import io.fromto.analytics.LocalAnalytics
import io.fromto.analytics.ScreenName
import io.fromto.analytics.screenView

/** A side-effect which records a screen view event. */
@Composable
fun TrackScreenViewEvent(
    screenName: ScreenName,
    analytics: Analytics = LocalAnalytics.current,
) =
    DisposableEffect(Unit) {
        analytics.screenView(screenName)
        onDispose {}
    }
