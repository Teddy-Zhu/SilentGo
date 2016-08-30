import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * PACKAGE_NAME
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/19.
 */
public class JsonTest {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "xxx");
        map.put("size", "22");
        map.put("s", "2");
        Map<String, Object> c = new HashMap<>();
        c.put("name", "asdxxx");
        map.put("c", c);
        String pojS = JSON.toJSONString(map);
        PoJo poj = JSON.parseObject(pojS, PoJo.class);

        String[] aa = new String[12];
        List<String> ca = new ArrayList<>();
        System.out.println(aa.getClass().isArray());
        System.out.println(ca.getClass().isArray());


        String a = "";
        System.out.println(aa.getClass().getComponentType().equals(a.getClass()));

        PoJo[] pa = new PoJo[2];
        String js = "{\"user\":[{\"name\":\"asda\"},{\"name\":\"xxxx\"}]}";
        JSONObject poJos = JSON.parseObject(js);
        PoJo[] poJos1 = poJos.getObject("user", pa.getClass());
    }
}
