#include "org_sp_shcp_llapi_LLAPI.h"
#include <iostream>
#include "stdlib.h"
#include "unistd.h"
#include "string.h"
#include "sys/wait.h"
#include <vector>
#include <string>

extern char **environ;

int spawnv(const char *cmd, char *const args[]) {
    int process = 0;
    process = fork();
    if (process == 0) {
        execvpe(cmd, args, environ);
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
        execvpe(cmd, args, environ);
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
    return (jint) result;
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

JNIEXPORT jint JNICALL Java_org_sp_shcp_llapi_LLAPI_setEnv (JNIEnv *env, jclass cls, jstring jkey, jstring jvalue) {
    const char *key = env->GetStringUTFChars(jkey, JNI_FALSE);
    const char *value = env->GetStringUTFChars(jvalue, JNI_FALSE);

    int result = setenv(key, value, 1);

    env->ReleaseStringUTFChars(jkey, key);
    env->ReleaseStringUTFChars(jvalue, value);
    return result;
}

JNIEXPORT jint JNICALL Java_org_sp_shcp_llapi_LLAPI_unsetEnv (JNIEnv *env, jclass cls, jstring jkey) {
    const char *key = env->GetStringUTFChars(jkey, JNI_FALSE);

    int result = unsetenv(key);

    env->ReleaseStringUTFChars(jkey, key);
    return result;
}

JNIEXPORT jstring JNICALL Java_org_sp_shcp_llapi_LLAPI_getEnv (JNIEnv *env, jclass cls, jstring jkey) {
    const char *key = env->GetStringUTFChars(jkey, JNI_FALSE);
    const char *value = getenv(key);

    env->ReleaseStringUTFChars(jkey, key);

    jstring result = env->NewStringUTF(value);
    return result;
}

JNIEXPORT jstring JNICALL Java_org_sp_shcp_llapi_LLAPI_getAllEnv (JNIEnv *env, jclass cls) {
    std::string result;

    for (char **e = environ; *e != 0; e++) {
        char *v = *e;
        result += std::string(v);
        result += "\n";
    }

    jstring jresult = env->NewStringUTF(result.c_str());
    return jresult;
}

JNIEXPORT jint JNICALL Java_org_sp_shcp_llapi_LLAPI_cd (JNIEnv *env, jclass cls, jstring str) {
    const char *path = env->GetStringUTFChars(str, JNI_FALSE);
    int result = chdir(path);
    env->ReleaseStringUTFChars(str, path);
    return result;
}

JNIEXPORT jint JNICALL Java_org_sp_shcp_llapi_LLAPI_waitPid (JNIEnv *env, jclass cls, jint pid) {
    int status;
    return (jint) waitpid(pid, &status, 0);
    //return (jint) status;
}

JNIEXPORT jint JNICALL Java_org_sp_shcp_llapi_LLAPI_checkPid (JNIEnv *env, jclass cls, jint pid) {
    int status;
    return (jint) waitpid(pid, &status, WNOHANG);
    //return (jint) status;
}
