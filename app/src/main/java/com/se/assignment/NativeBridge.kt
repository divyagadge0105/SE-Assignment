package com.se.assignment

object NativeBridge {
    init { System.loadLibrary("native-lib") }
    external fun processI420(i420: ByteArray, width: Int, height: Int): ByteArray
}
