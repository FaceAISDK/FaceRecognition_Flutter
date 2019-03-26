
#include <jni.h>

/**
 *  Http 的配置信息，
 *
 */
JNIEXPORT jobject JNICALL
Java_com_zlb_jniInterface_JniInvokeInterface_getJniHashMap(JNIEnv *env, jclass type) {

    jclass class_hashmap = (*env)->FindClass(env, "java/util/HashMap");
    jmethodID hashmap_init = (*env)->GetMethodID(env, class_hashmap, "<init>",
                                                 "()V");
    jobject HashMap = (*env)->NewObject(env, class_hashmap, hashmap_init, "");
    jmethodID HashMap_put = (*env)->GetMethodID(env, class_hashmap, "put",
                                                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");


    //1. baseUrl
    (*env)->CallObjectMethod(env, HashMap, HashMap_put, (*env)->NewStringUTF(env, "baseUrl"),
                             (*env)->NewStringUTF(env, "http://t1.int.owl1024.com/"));

    //2.clientId  -- grantType: refresh_token
    (*env)->CallObjectMethod(env, HashMap, HashMap_put, (*env)->NewStringUTF(env, "clientId"),
                             (*env)->NewStringUTF(env, "5e96eac06151d0ce2dd9554d7ee167ce"));

    //3.clientSecret
    (*env)->CallObjectMethod(env, HashMap, HashMap_put, (*env)->NewStringUTF(env, "clientSecret"),
                             (*env)->NewStringUTF(env, "aCE34n89Y277n3829S7PcMN8qANF8Fh"));


    //4.oauthPath
    (*env)->CallObjectMethod(env, HashMap, HashMap_put, (*env)->NewStringUTF(env, "oauthPath"),
                             (*env)->NewStringUTF(env, "api/lebang/oauth/access_token"));

    //5.meProfile .
    (*env)->CallObjectMethod(env, HashMap, HashMap_put, (*env)->NewStringUTF(env, "meProfile"),
                             (*env)->NewStringUTF(env, "api/lebang/staffs/me/detail"));


    //6.Account
    (*env)->CallObjectMethod(env, HashMap, HashMap_put, (*env)->NewStringUTF(env, "account"),
                             (*env)->NewStringUTF(env, "18826562075"));

    //7.password .
    (*env)->CallObjectMethod(env, HashMap, HashMap_put, (*env)->NewStringUTF(env, "password"),
                             (*env)->NewStringUTF(env, "zxc123"));


    return HashMap;
}


/**
 * 测试的部分
 *
 */
JNIEXPORT jstring JNICALL
Java_com_zlb_jniInterface_JniInvokeInterface_getHttpHostStr(JNIEnv *env, jclass type) {
    return (*env)->NewStringUTF(env, "C String2564365436534");
}

