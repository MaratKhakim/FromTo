package io.fromto.analytics

import io.fromto.domain.util.TranslateError

/** Based on https://aslansari.com/posts/firebase-analytics-w-compose/ */
interface Analytics {
    fun logEvent(event: AnalyticsEvent)
    fun logError(error: TranslateError)
}

class DebugLoggingAnalytics : Analytics {
    override fun logEvent(event: AnalyticsEvent) {
        println("Logging: $event")
    }

    override fun logError(error: TranslateError) {
        println("Error: $error")
    }
}
