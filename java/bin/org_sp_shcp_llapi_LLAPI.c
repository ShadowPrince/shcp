#include "org_sp_shcp_llapi_LLAPI.h"
#include <iostream>
#include "stdlib.h"
#include "unistd.h"
#include "string.h"
#include "sys/wait.h"
#include <vector>

int spawnv(const char *cmd, char *const args[]) {
    int process = 0;
    process = fork();
    if (process == 0) {
        execv(cmd, args);
        exit(1);
    } else {
        int status;
        waitpid(process, &status, 0);
        return status;
    }
}

int spawnv_background(const char *cmd, char *const args[]) {
    int process = 0;
    if (process = fork()) {
        return process;
    } else {
        execv(cmd, args);
        exit(1);
    }
}

JNIEXPORT jint JNICALL Java_org_sp_shcp_llapi_LLAPI_exec (JNIEnv *env, jclass cls, jstring str, jobjectArray stringArray) {
    const char *path = env->GetStringUTFChars(str, JNI_FALSE);

    int length = env->GetArrayLength(stringArray);
    char **args = (char**) malloc(sizeof(char*) * length);

    for (int i = 0; i < length; ++i) {
        jstring jstr = (jstring) env->GetObjectArrayElement(stringArray, i);
        const char* cpp_string = env->GetStringUTFChars(jstr, 0);

        args[i] = (char*) malloc(strlen(cpp_string) + 1);
        strcpy(args[i], cpp_string);

        env->ReleaseStringUTFChars(jstr, cpp_string);
        env->DeleteLocalRef(jstr);
    }

    args[length] = (char*) 0;

    int result = spawnv(path, args);
    env->ReleaseStringUTFChars(str, path);
    return result;
}

JNIEXPORT jint JNICALL Java_org_sp_shcp_llapi_LLAPI_execBackground (JNIEnv *env, jclass cls, jstring str, jobjectArray stringArray) {
    const char *path = env->GetStringUTFChars(str, JNI_FALSE);

    int length = env->GetArrayLength(stringArray);
    char **args = (char**) malloc(sizeof(char*) * length);

    for (int i = 0; i < length; ++i) {
        jstring jstr = (jstring) env->GetObjectArrayElement(stringArray, i);
        const char* cpp_string = env->GetStringUTFChars(jstr, 0);

        args[i] = (char*) malloc(strlen(cpp_string) + 1);
        strcpy(args[i], cpp_string);

        env->ReleaseStringUTFChars(jstr, cpp_string);
        env->DeleteLocalRef(jstr);
    }

    args[length] = (char*) 0;

    int result = spawnv_background(path, args);
    env->ReleaseStringUTFChars(str, path);
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_org_sp_shcp_llapi_LLAPI_cd (JNIEnv *env, jclass cls, jstring str) {
    const char *path = env->GetStringUTFChars(str, JNI_FALSE);
    int result = chdir(path);
    env->ReleaseStringUTFChars(str, path);
    return result;
}

JNIEXPORT jint JNICALL Java_org_sp_shcp_llapi_LLAPI_waitPid (JNIEnv *env, jclass cls, jint pid) {
    int status;
    waitpid(pid, &status, 0);
    return (jint) status;
}

JNIEXPORT jint JNICALL Java_org_sp_shcp_llapi_LLAPI_checkPid (JNIEnv *env, jclass cls, jint pid) {
    int status;
    waitpid(pid, &status, WNOHANG);
    return (jint) status;
}
