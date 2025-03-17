package io.fromto.analytics

/** Classes and functions associated with analytics events for the UI. */
fun Analytics.screenView(screen: ScreenName) {
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Types.SCREEN_VIEW,
            extras =
                listOf(
                    AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.SCREEN_NAME, screen.screen),
                ),
        ),
    )
}

fun Analytics.buttonClick(screen: ScreenName, button: Buttons) {
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Types.BUTTON_CLICK,
            extras =
                listOf(
                    AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.SCREEN_NAME, screen.screen),
                    AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.BUTTON_ID, button.id),
                ),
        ),
    )
}

fun Analytics.itemSelect(
    screen: ScreenName,
    category: String,
    itemId: String,
    itemName: String = ""
) {
    val extras =
        mutableListOf(
            AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.SCREEN_NAME, screen.screen),
            AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.ITEM_ID, itemId),
            AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.ITEM_CATEGORY, category),
            AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.ITEM_NAME, itemName),
        )

    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Types.SELECT_ITEM,
            extras = extras,
        ),
    )
}
