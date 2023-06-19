package com.rhuarhri.imagetracer.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionUtils {

    val REQUIRED_PERMISSIONS =
        mutableListOf (
            android.Manifest.permission.CAMERA
        ).toTypedArray()

    fun hasCameraPermission(context : Context) : Boolean {
        return ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    fun hasAllPermission(context : Context) : Boolean {
        var granted = false
        REQUIRED_PERMISSIONS.forEach {
            granted = ContextCompat.checkSelfPermission(
                context, it) == PackageManager.PERMISSION_GRANTED
        }
        return granted
    }
}