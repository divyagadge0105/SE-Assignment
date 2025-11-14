#include <jni.h>
#include <string>
#include <android/log.h>
#include <opencv2/opencv.hpp>

#define LOG_TAG "native-lib"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

using namespace cv;

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_se_assignment_NativeBridge_processI420(JNIEnv *env, jclass clazz,
                                                jbyteArray i420, jint width, jint height) {
    jbyte* bytes = env->GetByteArrayElements(i420, NULL);
    jsize len = env->GetArrayLength(i420);
    std::vector<unsigned char> data((unsigned char*)bytes, (unsigned char*)bytes + len);

    // Create Mat from I420: height + height/2 rows, width cols, single channel
    Mat yuvImg(height + height/2, width, CV_8UC1, data.data());
    Mat rgba;
    cvtColor(yuvImg, rgba, COLOR_YUV2RGBA_I420);

    Mat gray;
    cvtColor(rgba, gray, COLOR_RGBA2GRAY);
    Mat edges;
    Canny(gray, edges, 80, 200);

    Mat edgesRGBA;
    cvtColor(edges, edgesRGBA, COLOR_GRAY2RGBA);

    int outSize = edgesRGBA.total() * edgesRGBA.elemSize();
    jbyteArray out = env->NewByteArray(outSize);
    env->SetByteArrayRegion(out, 0, outSize, (const jbyte*)edgesRGBA.data);

    env->ReleaseByteArrayElements(i420, bytes, JNI_ABORT);
    return out;
}
