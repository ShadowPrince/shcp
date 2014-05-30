/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_sp_shcp_llapi_LLAPI */

#ifndef _Included_org_sp_shcp_llapi_LLAPI
#define _Included_org_sp_shcp_llapi_LLAPI
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_sp_shcp_llapi_LLAPI
 * Method:    popen
 * Signature: (Ljava/lang/String;)V
 */

JNIEXPORT jint JNICALL Java_org_sp_shcp_llapi_LLAPI_exec
  (JNIEnv *env, jclass cls, jstring str, jobjectArray stringArray);

JNIEXPORT jint JNICALL Java_org_sp_shcp_llapi_LLAPI_execBackground
  (JNIEnv *env, jclass cls, jstring str, jobjectArray stringArray);

JNIEXPORT jint JNICALL Java_org_sp_shcp_llapi_LLAPI_cd
  (JNIEnv *env, jclass cls, jstring str);

JNIEXPORT jint JNICALL Java_org_sp_shcp_llapi_LLAPI_setEnv
  (JNIEnv *env, jclass cls, jstring key, jstring value);

JNIEXPORT jint JNICALL Java_org_sp_shcp_llapi_LLAPI_unsetEnv 
  (JNIEnv *env, jclass cls, jstring jkey);

JNIEXPORT jstring JNICALL Java_org_sp_shcp_llapi_LLAPI_getEnv 
  (JNIEnv *env, jclass cls, jstring jkey);

JNIEXPORT jstring JNICALL Java_org_sp_shcp_llapi_LLAPI_getAllEnv 
    (JNIEnv *env, jclass cls);

JNIEXPORT jint JNICALL Java_org_sp_shcp_llapi_LLAPI_waitPid 
  (JNIEnv *env, jclass cls, jint pid);

JNIEXPORT jint JNICALL Java_org_sp_shcp_llapi_LLAPI_checkPid 
  (JNIEnv *env, jclass cls, jint pid);



#ifdef __cplusplus
}
#endif
#endif
