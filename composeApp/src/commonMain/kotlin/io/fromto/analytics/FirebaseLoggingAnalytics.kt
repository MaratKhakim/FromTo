package io.fromto.analytics

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics
import dev.gitlive.firebase.analytics.logEvent
import dev.gitlive.firebase.crashlytics.crashlytics
import io.fromto.domain.util.TranslateError

class FirebaseAnalyticsHelper : Analytics {
    override fun logEvent(event: AnalyticsEvent) {
        Firebase.analytics.logEvent(event.type.value) {
            for (extra in event.extras) {
                // Truncate parameter keys and values
                // according to firebase maximum length values.
                param(
                    key = extra.key.value.take(40),
                    value = extra.value.take(100),
                )
            }
        }
    }

    override fun logError(error: TranslateError) {
        Firebase.crashlytics.recordException(
            Throwable(
                message = error.message ?: error.toString(),
                cause = error.throwable,
            )
        )
    }
}
