#include <string.h>
#include <jni.h>

#ifndef _Included_com_herald_ezherald_api_APPID
#define _Included_com_herald_ezherald_api_APPID

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_herald_ezherald_api_APPID
 * Method:    getAPPID
 * Signature: ()Ljava/lang/String;
 */

JNIEXPORT jstring JNICALL Java_com_herald_ezherald_api_APPID_getAPPID(JNIEnv *env, jobject obj){
    return env->NewStringUTF("34cc6df78cfa7cd457284e4fc377559e");
}



#ifdef __cplusplus
}
#endif
#endif