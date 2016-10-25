package com.silentgo.core.db.methodnameparser;

import com.silentgo.core.db.funcanalyse.AnalyseKit;
import com.silentgo.orm.base.BaseTableInfo;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.db.methodnameparser
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/24.
 */
public class MethodParserKit {

    public static void parse(String methodName, List<Annotation> annotations, List<String> parsedString, BaseTableInfo baseTableInfo) {
        AnalyseKit.analyse(methodName, parsedString);
        new QueryMethodParser().parseMethodName(annotations, parsedString, baseTableInfo);
        new OrderMethodParser().parseMethodName(annotations, parsedString, baseTableInfo);
    }
}
