package org.sp.shcp.llapi;

public class LLAPI {
    static {
        System.loadLibrary("llapi");
    }

    native public static int exec(String cmd, String[] args);
    native public static int execBackground(String cmd, String[] args);

    native public static int waitPid(int pid);
    native public static int checkPid(int pid);

    native public static int cd(String path);
}
