package com.silentgo.orm.sqlparser.methodnameparser;

import com.silentgo.orm.base.BaseTableInfo;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.sqlparser.methodnameparser
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/24.
 */
public interface MethodParser {

    public void parseMethodName(List<Annotation> annotations, List<String> parsedString, BaseTableInfo baseTableInfo);
}
