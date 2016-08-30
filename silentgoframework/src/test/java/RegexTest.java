import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project : silentgo
 * PACKAGE_NAME
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/29.
 */
public class RegexTest {

    public static void main(String[] args) {
        String pattern = "/thread/(?<id>\\d+)/t/(?<token>[a-zA-Z]+)";

        Pattern p = Pattern.compile(pattern);

        Matcher matcher = p.matcher("/thread/123/t/a");
        if (matcher.matches()) {
            System.out.println("true");
        }else{
            System.out.println("false");
        }
    }
}
