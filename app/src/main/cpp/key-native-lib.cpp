#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_ameldev_tiktokprodownloader_utils_Keys_getApiKey(
        JNIEnv* env,
        jclass clazz) {
    std::string apiKey = "";
    return env->NewStringUTF(apiKey.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ameldev_tiktokprodownloader_utils_Keys_getApiURL(
        JNIEnv* env,
        jclass clazz) {
    std::string apiUrl = "";
    return env->NewStringUTF(apiUrl.c_str());
}