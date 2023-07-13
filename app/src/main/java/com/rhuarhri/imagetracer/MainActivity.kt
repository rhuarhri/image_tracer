package com.rhuarhri.imagetracer

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rhuarhri.imagetracer.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import org.opencv.android.OpenCVLoader

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        /*How to install open cv
        step 1 get a release for open cv from https://opencv.org/releases/

        step 2 in android studio go to file -> new -> import module

        step 3 find the sdk folder in the open cv release

        step 4 change the module name and press finish

        step 5 this may cause a namespace issue. To fix it find the modules android manifest file.
        There should be a line like package="org.opencv". Copy the package value (i.e. "org.opencv")
        and past it in the modules build.grable file like so.
        android {
            namespace 'org.opencv'
        }

        step 6 update the sdk versions to match the app's sdk versions.

        step 7 Add the following to the module's build.grable file.
        buildFeatures{
            buildConfig = true
            aidl = true
        }
        This will allow the module to use the BuildConfig and aidl files.
        Also will resolve the OpenCVEngineInterface does not exist error.

        step 8 in android studio go to Tools -> AGP upgrade assistant and run selected steps.

        At this point the modules build.grable file should look something like this.
        android {
            namespace 'org.opencv'
            compileSdkVersion 33

            defaultConfig {
                minSdkVersion 24
                targetSdkVersion 33
                externalNativeBuild {
                    cmake {
                        arguments "-DANDROID_STL=c++_shared"
                        targets "opencv_jni_shared"
                    }
                }
            }

            compileOptions {
                sourceCompatibility JavaVersion.VERSION_1_8
                targetCompatibility JavaVersion.VERSION_1_8
            }

            buildFeatures{
                buildConfig = true
                aidl = true
            }
            //rest of file

        step 9 in android studio go to file -> project structure -> dependencies. Click on app.
        Click the plus button under declared dependencies. Click module dependency.
        Select the open cv sdk, and set it to implementation.

        step 10 go to the app's main activity and past the following in the on create function.
        if (OpenCVLoader.initDebug()) {
            Log.d("OPEN CV", "open cv loaded")
        } else {
            Log.d("OPEN CV", "open cv not loaded")
        }
        This will check if open cv is available.

        step 11 run the app

         */

        OpenCVLoader.initDebug()

        setContent {

            Navigation()

        }
    }
}
