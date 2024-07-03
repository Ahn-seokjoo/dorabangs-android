package com.mashup.dorabangs.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.dorabangs.feature.navigation.navigateToSaveLink
import com.dorabangs.feature.navigation.saveLinkNavigation
import com.mashup.core.navigation.NavigationRoute
import com.mashup.dorabangs.feature.navigation.homeNavigation
import com.mashup.dorabangs.feature.navigation.navigateToHome
import com.mashup.dorabangs.feature.navigation.onBoardingNavigation
import com.mashup.dorabangs.feature.storage.navigation.storageDetailNavigation
import com.mashup.dorabangs.feature.storage.navigation.storageNavigation

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    appState: DoraAppState,
    startDestination: String = NavigationRoute.OnBoardingScreen.route,
) {
    NavHost(
        modifier = modifier,
        navController = appState.navController,
        startDestination = startDestination,
    ) {
        onBoardingNavigation(
            navigateToHome = { appState.navController.navigateToHome() },
        )
        homeNavigation {
            appState.navController.navigateToSaveLink()
        }
        storageNavigation(appState.navController)
        storageDetailNavigation(appState.navController)
        saveLinkNavigation(appState.navController)
    }
}
