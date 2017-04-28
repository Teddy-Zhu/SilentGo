import com.google.gson.Gson;
import com.silentgo.utils.ReflectKit;

import java.util.Map;

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

        Gson json = new Gson();

        hello aaa = json.fromJson("{\"aaa\":\"adsa\"}", hello.class);

        Map<String,Object> map = ReflectKit.beanToMap(aaa);

        System.out.println(map);
    }
}
