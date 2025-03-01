package io.fromto.presentation.main

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class TopBarAction(
    val icon: DrawableResource,
    val contentDescription: StringResource,
    val onClick: () -> Unit
)
