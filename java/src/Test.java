import org.sp.shcp.llapi.LLAPI;

public class Test {
    public static void slp(long s) {
        try {
            Thread.sleep(s);
        } catch (Exception e) {}
    }

    public static void main(String[] args) {
        System.out.println(LLAPI.getAllEnv());
    }
}
