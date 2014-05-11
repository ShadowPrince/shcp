package org.sp.shcp.llapi;

public class LLAPI {
    static {
        System.loadLibrary("llapi");
    }

    public static void main(String[] args) {
        popen("top -b");
    }

    native public static void popen(String cmd);
}
