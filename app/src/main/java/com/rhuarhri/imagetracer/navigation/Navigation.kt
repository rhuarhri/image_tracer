package com.rhuarhri.imagetracer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rhuarhri.imagetracer.cameratrace.CameraTraceScreen
import com.rhuarhri.imagetracer.image_selection.ImageSelectionScreen
import com.rhuarhri.imagetracer.lightbox.LightBoxScreen
import com.rhuarhri.imagetracer.menu.MenuScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.MenuScreen.route) {
        composable(route = Route.MenuScreen.route) {
            MenuScreen().Screen(navController = navController)
        }
        /*composable(route = Route.SettingsScreen.route + "/{test}",
            arguments = listOf(
                navArgument("test") {
                    type = NavType.StringType
                    defaultValue = "default test argument"
                    nullable = false
                }
            )
        ) { backStack ->
            SettingsScreen.Screen(backStack.arguments?.getString("test", "no argument") ?: "null")
        }*/
        
        composable(route = Route.ImageSelectionScreen.route) {
            ImageSelectionScreen().Screen(navController = navController)
        }

        composable(route = Route.CameraTraceScreen.route) {
            CameraTraceScreen().Screen(navController = navController)
        }
        composable(route = Route.LightBoxScreen.route) {
            LightBoxScreen().Screen(controller = navController)
        }
    }
}