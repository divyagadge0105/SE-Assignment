package com.se.assignment

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Size

class Camera2Controller(private val ctx: Context, private val onFrame: (ByteArray, Int, Int) -> Unit) {
    private var cameraDevice: CameraDevice? = null
    private var session: CameraCaptureSession? = null
    private var reader: ImageReader? = null
    private val cameraManager = ctx.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private var bgThread: HandlerThread? = null
    private var bgHandler: Handler? = null

    fun start(cameraId: String = getBackCameraId()) {
        startBackground()
        val size = Size(640, 480)
        reader = ImageReader.newInstance(size.width, size.height, ImageFormat.YUV_420_888, 2)
        reader?.setOnImageAvailableListener({ r ->
            val image = r.acquireLatestImage() ?: return@setOnImageAvailableListener
            val i420 = yuvToI420(image)
            onFrame(i420, image.width, image.height)
            image.close()
        }, bgHandler)

        cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(device: CameraDevice) {
                cameraDevice = device
                createSession()
            }
            override fun onDisconnected(device: CameraDevice) { device.close(); cameraDevice=null }
            override fun onError(device: CameraDevice, error: Int) { device.close(); cameraDevice=null }
        }, bgHandler)
    }

    private fun createSession() {
        val surface = reader!!.surface
        cameraDevice?.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(sessionParam: CameraCaptureSession) {
                session = sessionParam
                val request = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                request.addTarget(surface)
                request.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
                session.setRepeatingRequest(request.build(), null, bgHandler)
            }
            override fun onConfigureFailed(session: CameraCaptureSession) {}
        }, bgHandler)
    }

    fun stop() {
        session?.close()
        cameraDevice?.close()
        reader?.close()
        stopBackground()
    }

    private fun startBackground() {
        bgThread = HandlerThread("cam-bg").also { it.start() }
        bgHandler = Handler(bgThread!!.looper)
    }
    private fun stopBackground() {
        bgThread?.quitSafely()
        bgThread = null
        bgHandler = null
    }

    private fun getBackCameraId(): String {
        cameraManager.cameraIdList.forEach { id ->
            val cm = cameraManager.getCameraCharacteristics(id)
            val facing = cm.get(CameraCharacteristics.LENS_FACING)
            if (facing == CameraCharacteristics.LENS_FACING_BACK) return id
        }
        return cameraManager.cameraIdList[0]
    }

    // convert Image YUV_420_888 to I420 byte array (Y + U + V)
    private fun yuvToI420(image: android.media.Image): ByteArray {
        val w = image.width
        val h = image.height
        val yPlane = image.planes[0]
        val uPlane = image.planes[1]
        val vPlane = image.planes[2]

        val ySize = w * h
        val uvSize = ySize / 4
        val out = ByteArray(ySize + uvSize*2)

        // copy Y
        yPlane.buffer.get(out, 0, ySize)

        // read U and V planes into temporary buffers
        val rowStrideU = uPlane.rowStride
        val rowStrideV = vPlane.rowStride
        val pixelStrideU = uPlane.pixelStride
        val pixelStrideV = vPlane.pixelStride

        val ubuf = ByteArray(rowStrideU * ((h+1)/2))
        val vbuf = ByteArray(rowStrideV * ((h+1)/2))
        uPlane.buffer.get(ubuf)
        vPlane.buffer.get(vbuf)

        var outIndexU = ySize
        var outIndexV = ySize + uvSize
        val halfH = h / 2
        val halfW = w / 2
        for (r in 0 until halfH) {
            var ui = r * rowStrideU
            var vi = r * rowStrideV
            for (c in 0 until halfW) {
                out[outIndexU++] = ubuf[ui]
                out[outIndexV++] = vbuf[vi]
                ui += pixelStrideU
                vi += pixelStrideV
            }
        }
        return out
    }
}
