package com.silentgo.core.db.funcanalyse;

import com.silentgo.core.config.Const;

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

    public static String[] keyWords = DaoKeyWord.getValues();

    public static void analyse(String originMethod, List<String> prepared) {
        int count = 0;
        int len = keyWords.length;
        for (int i = 0; i < len; i++) {
            String keyWord = keyWords[i];
            int index = originMethod.indexOf(keyWord);
            if (index == -1) {
                count++;
                continue;
            }
            String left = originMethod.substring(0, index);
            String right = originMethod.substring(index + keyWord.length());

            if (!Const.DEFAULT_NONE.equals(left)) {
                analyse(left, prepared);
            }
            prepared.add(keyWord);
            if (!Const.DEFAULT_NONE.equals(right)) {
                analyse(right, prepared);
            }
            break;
        }
        if (count == len) prepared.add(originMethod);
    }

}
