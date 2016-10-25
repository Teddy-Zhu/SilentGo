package com.silentgo.core.db.methodnameparser;

import com.silentgo.core.db.annotation.Order;
import com.silentgo.core.db.annotation.Query;
import com.silentgo.core.db.daoresolve.DaoResolveKit;
import com.silentgo.core.db.funcanalyse.AnalyseKit;
import com.silentgo.core.db.funcanalyse.DaoKeyWord;
import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.utils.StringKit;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.db.methodnameparser
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/24.
 */
public class OrderMethodParser implements MethodParser {
    @Override
    public void parseMethodName(List<Annotation> annotations, List<String> parsedString, BaseTableInfo baseTableInfo) {
        int index = parsedString.indexOf(DaoKeyWord.Order.innername);
        if (index == -1) return;
        Optional<Annotation> queryAn = annotations.stream().filter(annotation -> Order.class.equals(annotation.annotationType())).findFirst();

        if (queryAn.isPresent()) {
            String by1 = parsedString.get(index + 1);
            int start = DaoKeyWord.By.equals(by1) ? index + 1 : -1;
            if (start > -1) {
                start += 1;
                boolean nextIsField = DaoResolveKit.isField(parsedString.get(start), baseTableInfo);
                Order order = (Order) queryAn.get();
                List<String> parsedNow = new ArrayList<>();
                AnalyseKit.analyse(StringKit.join(order.value(), "And"), parsedNow);
                if (nextIsField) parsedNow.add("And");
                for (int i = 0, len = parsedNow.size(); i < len; i++) {
                    int now = start + i;
                    if (now >= len) {
                        parsedString.add(parsedNow.get(i));
                    } else {
                        parsedString.add(start + i, parsedNow.get(i));
                    }
                }
            }
        }
    }
}
