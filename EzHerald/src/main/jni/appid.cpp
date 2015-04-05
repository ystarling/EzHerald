#include <string.h>
#include <jni.h>

jstring Java_com_herald_ezherald_api_APPID_getAPPID(JNIEnv* env,jobject javaThis){
    return env->NewStringUTF("aaaaaaaaaaaaaaaabbbbbbbbbbbbbbbb");
}

