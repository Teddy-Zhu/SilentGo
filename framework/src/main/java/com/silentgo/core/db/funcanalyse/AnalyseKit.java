package com.silentgo.core.db.funcanalyse;

import com.silentgo.utils.StringKit;
import com.sun.org.apache.xpath.internal.compiler.Keywords;

import java.util.ArrayList;
import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.db.funcanalyse
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/29.
 */
public class AnalyseKit {


    public ArrayList keyWords = new ArrayList() {
        {
            add("query");
            add("delete");
            add("update");
            add("insert");
            add("order");
            add("group");
            add("limit");
            add("by");
        }
    };

    public static void main(String[] args) {
        AnalyseKit kit = new AnalyseKit();
        String methodName = "queryByIdOrderByTime";
        methodName = methodName.toLowerCase();
        List<String> keys = new ArrayList<>();
        kit.analyse(methodName, false, keys);

        System.out.println(keys);
//        String cccc = "queryByIdOrderByTime";
//        cccc = cccc.toLowerCase();
//        List<String> cc = queryListByTime(cccc, StringKit.join(kit.keyWords, ","));
    }

    public static List<String> queryListByTime(String des, String a) {
        String stemp[] = a.split(",");                //拆分string传进来的关键字, ','分割
        String destmp = "", whilestmp = des;
        List<String> re = new ArrayList<String>();    //返回备用
        for (String stmp : stemp)                        //第一次遍历
        {
            while (whilestmp.indexOf(stmp) >= 0)        //单个关键字循环
            {
                destmp = whilestmp.substring(whilestmp.indexOf(stmp) + stmp.length());    //切掉找到的开头部分
                whilestmp = destmp;                                            //保留需要继续循环部分
                if (!destmp.equals("")) {
                    for (String stmp1 : stemp)                                //二次循环，重新遍历关键字
                    {
                        if (destmp.indexOf(stmp1) >= 0) {
                            destmp = destmp.substring(0, destmp.indexOf(stmp1));    //去掉关键字以及之后的
                        }
                    }
                    if (!destmp.equals(""))
                        re.add(destmp);                                        //最后剩下的需要的
                }
            }
        }
        return re;
    }

    public void analyse(String methodName, boolean prematch, List<String> prepared) {
        List<String> init = keyWords;
        int len = methodName.length();
        for (int i = 0; i < len; i++) {
            char a = methodName.charAt(i);
            init = match(init, a, i);
            if (init.size() == 0) {
                prematch = false;
            }
            if (init.size() > 0) {
                if (i != 0 && !prematch) {
                    prepared.add(methodName.substring(0, i));
                    methodName = methodName.substring(i);
                    len = methodName.length();
                    i = 0;
                }
                prematch = true;
            }
            if (init.size() == 1 && (init.get(0).length() - 1) == i) {
                prepared.add(init.get(0));
                methodName = methodName.substring(i + 1);
                analyse(methodName, prematch, prepared);
                break;
            }
        }
        return;
    }

    private List<String> match(List<String> keys, char a, int index) {
        List<String> s = new ArrayList<>();
        for (String keyWord : keys) {
            if (match(keyWord, index, a)) {
                s.add(keyWord);
            }
        }
        return s;
    }

    private boolean match(String key, int index, char a) {
        return key.charAt(index) == a;
    }
}
