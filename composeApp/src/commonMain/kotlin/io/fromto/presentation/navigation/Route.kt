package io.fromto.presentation.navigation

import fromto.composeapp.generated.resources.Res
import fromto.composeapp.generated.resources.app_name
import fromto.composeapp.generated.resources.history
import fromto.composeapp.generated.resources.translate
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Serializable
sealed class Route(
    @Contextual val title: StringResource,
    @Contextual val label: StringResource,
    @Contextual val icon: DrawableResource,
) {
    @Serializable
    data object Translate : Route(
        title = Res.string.app_name,
        label = Res.string.translate,
        icon = Res.drawable.translate
    )

    @Serializable
    data object History : Route(
        title = Res.string.history,
        label = Res.string.history,
        icon = Res.drawable.history
    )
}

