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

    char appID[] = "45dd7eg89dgb8de568395f5gd48866:f";
    for(int i=0;i<32;i++){
        appID[i] = appID[i] - 2;
    }
    for(int i=0;i<32;i++){
        appID[i] = appID[i] + 1;
    }
    return env->NewStringUTF(appID);
}



#ifdef __cplusplus
}
#endif
#endif