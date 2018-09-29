#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_zenglb_framework_activity_ndk_NDKActivity_stringFromJNITest
        (JNIEnv *env, jobject instance) {


    std::string hello = "Hello from C++,188562x751111";
    return env->NewStringUTF(hello.c_str());

}


