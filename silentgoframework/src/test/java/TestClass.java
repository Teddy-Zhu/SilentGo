/**
 * Project : silentgo
 * PACKAGE_NAME
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/22.
 */
public class TestClass {
    public String cc = "asdasda";

    public TestClass(String cc) {
        this.cc = cc;
    }

    public TestClass() {
    }

    public void say(Integer a) {
        a = 10;
        int c = a + 1;
    }


    public void say(int a) {
        a = 10;
        int c = a + 1;
    }

    public static void main(String[] args) throws NoSuchMethodException {
        System.out.println(TestClass.class.getMethod("say", int.class));
        System.out.println(TestClass.class.getMethod("say", Integer.class));

        System.out.println(TestClass.class.getMethod("say", int.class).hashCode());
        System.out.println(TestClass.class.getMethod("say", Integer.class).hashCode());
    }
}
