package com.rhuarhri.imagetracer.navigation

sealed class Route(val route : String) {
    object MenuScreen : Route("menu_screen")

    object ImageSelectionScreen : Route("image_selection_screen")

    object CameraTraceScreen : Route("camera_trace_screen")

    object LightBoxScreen : Route("light_box_screen")

    /*
    useful for adding arguments
    fun withArgs(vararg arguments : String) : String {
        return buildString {
            append(route)
            arguments.forEach { argument ->
                append("/$argument")
            }
        }
    }*/
}
