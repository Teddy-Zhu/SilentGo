import com.silentgo.config.Const;
import com.silentgo.config.Regex;
import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.kit.StringKit;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project : silentgo
 * PACKAGE_NAME
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/22.
 */
public class TestCGLib {

    public static void main(String[] args) {

        String ac = "xxxxx.asas";

        System.out.println(StringKit.getLeft(ac, "."));


        String a = "/{id}/xxx/{oa:}/asxd/{od:d+}/11/";

        // String pattern = "/(?<xx>.*?)/xxx/(?<dd>.*?)/asxd";
        String pattern = Regex.RoutePath;

        Pattern pattern1 = Pattern.compile(pattern);

        Matcher matcher = pattern1.matcher(a);

//        if (matcher.find()) {
//            for (int i = 1; i <= matcher.groupCount(); i++) {
//                a = a.replace(matcher.group(i), "a");
//            }
//            System.out.println(a);
//        }

        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }
}
