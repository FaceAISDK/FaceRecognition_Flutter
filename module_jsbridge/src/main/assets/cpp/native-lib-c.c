#include <jni.h>
extern "C"

JNIEXPORT jstring JNICALL
Java_com_zenglb_framework_activity_ndk_NDKActivity_getHtmlContent(JNIEnv *env, jobject instance,
                                                                  jstring str_) {
    const char *str = (*env)->GetStringUTFChars(env, str_, 0);



    (*env)->ReleaseStringUTFChars(env, str_, str);

    return (*env)->NewStringUTF(env, "aaaaaaaaa");
}

