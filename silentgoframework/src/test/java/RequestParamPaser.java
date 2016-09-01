import com.silentgo.core.exception.AppException;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Project : silentgo
 * PACKAGE_NAME
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/1.
 */
public class RequestParamPaser {
    private static String array = "[]";
    private static String keySplit = ".";
    private static Pattern pattern = Pattern.compile(".*?\\[[0-9]+\\]");

    public static void main(String[] args) throws AppException {
        Map<String, String[]> originMap = new HashMap<>();
        Map<String, Object> list = new HashMap<>();
        originMap.put("list.user[0].name", new String[]{"ercv"});
        originMap.put("list.user[0].id", new String[]{"21a3s13"});
        originMap.put("list.user[1].id", new String[]{"xx101q23"});
        originMap.put("list.user[1].name", new String[]{"1212x4"});
        originMap.put("list.users.roles[]", new String[]{"555", "xxxx", "1233"});
        originMap.put("list.admin[0].roles[0].rolename", new String[]{"123123"});
        originMap.put("list.admin[0].roles[0].roleid", new String[]{"123xx"});
        originMap.put("list.admin[1].roles[1].rolename", new String[]{"555"});
        originMap.put("list.admin[1].roles[1].roleid", new String[]{"555"});
        originMap.put("list.admin[1].xxxx", new String[]{"512555", "xxadxxx", "13e4d233"});
        originMap.put("list.admin[1].names[]", new String[]{"512555", "xxadxxx", "13e4d233"});
        originMap.put("name", new String[]{"xxx"});
        originMap.put("id", new String[]{"xzdas"});
        originMap.put("an[0].hh", new String[]{"xxads"});
        originMap.put("an[0].xx", new String[]{"123;llp4"});


        List<String> keys = new ArrayList<>(originMap.keySet());
        Map<String, Object> parsed = new HashMap<>();

        originMap.forEach((k, v) -> {
            parseMap(k.split("\\" + keySplit), parsed, v, k);
        });

    }
    private static void parseMap(String[] keyArray, Map<String, Object> parentMap, String[] value, String fullPath) {
        if (keyArray.length == 1) {
            String key = keyArray[0];
            if (isPrimitiveArray(key, value)) {
                // is array
                key = trimEnd(key, array);
                parentMap.put(key, value);
            } else if (isArray(key)) {
                // eg : user.iis[0] = "1223" it's error
                parentMap.put(fullPath, value.length > 1 ? value : value[0]);
            } else {
                //not array find first
                parentMap.put(key, value[0]);
            }

        } else {
            //it's length >= 2
            String key = keyArray[0];

            Object map = null;
            //resolve first
            if (key.endsWith(array)) {
                //as common value eg: user[].userid = "33" ,it's error
                parentMap.put(fullPath, value.length > 1 ? value : value[0]);
                return;
            } else if (isArray(key)) {
                int index = getIndex(key);
                key = removeIndex(key);
                map = parentMap.get(key);
                if (map == null) {
                    map = new HashMap[index + 1];
                    parentMap.put(key, map);
                } else if (((Map<String, Object>[]) map).length <= index) {
                    //resize array
                    Map<String, Object>[] newArrayMap = new HashMap[index + 1];
                    System.arraycopy(map, 0, newArrayMap, 0, ((Map<String, Object>[]) map).length);
                    parentMap.put(key, newArrayMap);
                    map = newArrayMap;
                }

                Map<String, Object> addMap = ((Map<String, Object>[]) map)[index];
                if (addMap == null) {
                    addMap = new HashMap<>();
                    ((Map<String, Object>[]) map)[index] = addMap;
                }
                String[] newKey = new String[keyArray.length - 1];
                System.arraycopy(keyArray, 1, newKey, 0, newKey.length);
                parseMap(newKey, addMap, value, fullPath);
                return;
            }
            // common  user.iis.xx  ="123"
            map = parentMap.get(key);
            if (map == null) {
                map = new HashMap<>();
                parentMap.put(key, map);
            }
            String[] newKey = new String[keyArray.length - 1];
            System.arraycopy(keyArray, 1, newKey, 0, newKey.length);
            parseMap(newKey, (Map<String, Object>) map, value, fullPath);
        }
    }

    //common array eg:  ["11","1123"] or [1,213] etc...
    private static boolean isPrimitiveArray(String key, String[] values) {
        return key.endsWith(array) || values.length > 1;
    }

    //array bean eg: [{"a":"c"},{"a":"x"}]
    private static boolean isArray(String key) {
        return pattern.matcher(key).matches();
    }

    private static String trimEnd(String key, String suffix) {
        return key.endsWith(suffix) ? key.substring(0, key.length() - suffix.length()) : key;
    }

    private static String removeIndex(String key) {
        return key.substring(0, key.lastIndexOf('['));
    }

    private static Integer getIndex(String key) {
        // num can not be empty  , before validate by function isArray
        String num = key.substring(key.lastIndexOf('[') + 1, key.length() - 1);
        return Integer.valueOf(num);
    }


    private static Integer getSize(Integer index) {
        return index + 20;
    }
}
