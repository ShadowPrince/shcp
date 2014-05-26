import org.sp.shcp.llapi.LLAPI;

public class Test {

    public static void main(String[] args) {
        System.out.println(LLAPI.cd("/usr/bin/naxiy/"));
        System.out.println(LLAPI.cd("/usr/bin/"));
    }
}
