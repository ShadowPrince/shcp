package org.sp.shcp.llapi;

public class LLAPI {
    static {
        loadLibrary();
    }

    public static void loadLibrary() {
        System.loadLibrary("llapi");
    }

    native public static int exec(String cmd, String[] args);
    native public static int execBackground(String cmd, String[] args);

    native public static int waitPid(int pid);
    native public static int checkPid(int pid);

    native public static int cd(String path);
    
    native public static int setEnv(String key, String value);
    native public static int unsetEnv(String key);
    native public static String getEnv(String key);
    native public static String getAllEnv();
}
