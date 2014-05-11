#include "org_sp_shcp_llapi_LLAPI.h"
#include <iostream>
#include "stdlib.h"

void test_system(const char *cmd) {
    system(cmd);
}

void test_popen(const char *cmd) {
    FILE *in;
    char buff[1024];

    if(!(in = popen(cmd, "r"))){
        return;
    }

    while(fgets(buff, sizeof(buff), in)!=NULL){
        printf("%s", buff);
    }

    pclose(in);
}

JNIEXPORT void JNICALL Java_org_sp_shcp_llapi_LLAPI_popen (JNIEnv *env, jclass cls, jstring str) {
    const char *nativeString = env->GetStringUTFChars(str, JNI_FALSE);
    test_system(nativeString);
    env->ReleaseStringUTFChars(str, nativeString);
}
