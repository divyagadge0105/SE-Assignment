package com.se.assignment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    lateinit var glView: GLView
    lateinit var camera: Camera2Controller

    private val requestPerm = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
        val ok = perms[Manifest.permission.CAMERA] == true
        if (ok) startCamera()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glView = GLView(this)
        setContentView(FrameLayout(this).apply { addView(glView) })

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPerm.launch(arrayOf(Manifest.permission.CAMERA))
        } else startCamera()
    }

    private fun startCamera() {
        camera = Camera2Controller(this) { i420, w, h ->
            Thread {
                val rgba = NativeBridge.processI420(i420, w, h) // RGBA bytes
                runOnUiThread { glView.updateFrame(rgba, w, h) }
            }.start()
        }
        camera.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        camera.stop()
    }
}
