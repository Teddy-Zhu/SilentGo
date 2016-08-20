import com.silentgo.servlet.SilentGoFilter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Project : silentgo
 * PACKAGE_NAME
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/20.
 */
public class testparameter {
    public static void main(String[] args) {
        System.out.println(args.getClass());
        for (Method m : SilentGoFilter.class.getMethods()) {
            System.out.println("----------------------------------------");
            for (Parameter p : m.getParameters()) {
                System.out.println("parameter: " + p.getType().getName() + ", " + p.getName());
            }
        }
    }
}
