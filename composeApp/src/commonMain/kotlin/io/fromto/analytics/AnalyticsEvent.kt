package io.fromto.analytics

data class AnalyticsEvent(
    val type: Types,
    val extras: List<Param> = emptyList(),
) {
    // Standard analytics types.
    enum class Types(val value: String) {
        BUTTON_CLICK("button_click"),
        SCREEN_VIEW("screen_view"),
        SELECT_ITEM("select_item"),
    }

    /**
     * A key-value pair used to supply extra context to an analytics event.
     *
     * @param key - the parameter key. Wherever possible use one of the standard `ParamKeys`,
     *   however, if no suitable key is available you can define your own as long as it is
     *   configured in your backend analytics system (for example, by creating a Firebase Analytics
     *   custom parameter).
     * @param value - the parameter value.
     */
    data class Param(val key: ParamKeys, val value: String)

    // Standard parameter keys.
    enum class ParamKeys(val value: String) {
        SCREEN_NAME("screen_name"),
        BUTTON_ID("button_id"),
        ITEM_ID("item_id"),
        ITEM_NAME("item_name"),
        ITEM_CATEGORY("item_category"),
    }
}
