import com.silentgo.utils.asm.ParameterNameUtils;

/**
 * Project : SilentGo
 * Package : PACKAGE_NAME
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2017/1/5.
 */
public class Test {


    public static void adxxxads(String xxazxcz) {

    }

    public void adads(String azxcz) {

    }

    public static void main(String[] args) throws NoSuchMethodException {

        String[] names = ParameterNameUtils.getMethodParameterNames(Test.class, Test.class.getMethod("adads", String.class));

        System.out.println(names);
    }
}
