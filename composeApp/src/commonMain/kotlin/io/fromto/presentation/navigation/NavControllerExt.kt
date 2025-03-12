package io.fromto.presentation.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

fun NavHostController.navigateWithPopUp(route: Route) {
    val startDestination = this.graph.findStartDestination()
    this.navigate(route) {
        popUpTo(startDestination.id)
        launchSingleTop = true
    }
}

fun NavDestination.getRoute(): Route? {
    return when (this.route) {
        Route.History::class.qualifiedName -> Route.History
        Route.Translate::class.qualifiedName -> Route.Translate
        else -> null
    }
}