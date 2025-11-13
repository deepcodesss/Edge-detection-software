#include <jni.h>
#include <opencv2/opencv.hpp>
#include <vector>
#include <android/log.h>
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "native-lib", __VA_ARGS__)
using namespace cv; using namespace std;
static void nv21_to_bgr(const unsigned char* nv21, int width, int height, Mat &outBgr){ Mat yuv(height + height/2, width, CV_8UC1, (void*)nv21); cvtColor(yuv, outBgr, COLOR_YUV2BGR_NV21); }
extern "C" JNIEXPORT jbyteArray JNICALL Java_com_example_openglcamera_GLRenderer_processFrameNative(JNIEnv *env, jobject thiz, jbyteArray data, jint width, jint height, jboolean edges) {
    jbyte* bytes = env->GetByteArrayElements(data, NULL);
    Mat bgr; nv21_to_bgr((unsigned char*)bytes, width, height, bgr);
    Mat gray; cvtColor(bgr, gray, COLOR_BGR2GRAY);
    Mat out; if (edges) Canny(gray, out, 50, 150); else out = gray;
    int outLen = out.total() * out.elemSize(); jbyteArray result = env->NewByteArray(outLen); env->SetByteArrayRegion(result, 0, outLen, (const jbyte*)out.data);
    env->ReleaseByteArrayElements(data, bytes, 0); return result;
}
