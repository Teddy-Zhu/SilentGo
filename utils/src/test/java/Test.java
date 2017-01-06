import com.silentgo.utils.ReflectKit;
import com.silentgo.utils.reflect.SGClass;

/**
 * Project : SilentGo
 * Package : PACKAGE_NAME
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2017/1/5.
 */
public class Test {

    public static void main(String[] args) {
        SGClass sgClass = ReflectKit.getSGClass(intera.class);

        System.out.println(sgClass);
    }
}
