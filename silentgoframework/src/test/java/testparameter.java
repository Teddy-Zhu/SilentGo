import com.silentgo.core.config.Const;
import com.silentgo.core.config.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
//        System.out.println(args.getClass());
//        for (Method m : SilentGoFilter.class.getMethods()) {
//            System.out.println("----------------------------------------");
//            for (Parameter p : m.getParameters()) {
//                System.out.println("parameter: " + p.getType().getName() + ", " + p.getName());
//            }
//        }

        String a = "/u/route/{id}/{a}";
        Pattern pattern = Pattern.compile(Regex.RoutePath);

        Matcher matcher = pattern.matcher(a);
        System.out.println(matcher.groupCount());
        if(matcher.matches()){
            System.out.println("true");
        }
        while (matcher.find()) {
            System.out.println(matcher.group());
        }

    }
}
