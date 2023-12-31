package com.rhuarhri.imagetracer.cameratrace

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraDisplay() {

    val lensFacing = CameraSelector.LENS_FACING_BACK
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val previewView = remember {
        PreviewView(context)
    }

    val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    /*val imageAnalysis = ImageAnalysis.Builder()
        .setImageQueueDepth(STRATEGY_KEEP_ONLY_LATEST).build()

    imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()) { image ->

        /*if (previous != null) {
            val currentMat = Mat()
            Utils.bitmapToMat(image.toBitmap(), currentMat)

            val previousMat = Mat()
            Utils.bitmapToMat(previous, previousMat)

            //image.setCropRect(rect)
        }*/

        val bitmap : Bitmap = EditingUtils.createBlankBitmap(100, 100)
        image.close()

        val drawable = BitmapDrawable(resources, bitmap)
        previewView.overlay.clear()
        previewView.overlay.add(drawable)
    }*/

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview
        ).cameraControl.cancelFocusAndMetering()
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }


    AndroidView({ previewView }, Modifier.fillMaxSize())
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        },
            ContextCompat.getMainExecutor(this)
        )

    }
}