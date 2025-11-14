package com.se.assignment

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class GLView(context: Context, attrs: AttributeSet? = null) : GLSurfaceView(context, attrs) {
    val renderer: GLRenderer

    init {
        setEGLContextClientVersion(2)
        renderer = GLRenderer()
        setRenderer(renderer)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    fun updateFrame(rgba: ByteArray, w: Int, h: Int) {
        renderer.updateFrame(rgba, w, h)
        requestRender()
    }
}
