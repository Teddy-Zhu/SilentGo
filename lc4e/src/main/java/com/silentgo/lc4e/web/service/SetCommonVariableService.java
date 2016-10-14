package com.silentgo.lc4e.web.service;

import com.silentgo.core.exception.AppException;
import com.silentgo.core.ioc.annotation.Inject;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.lc4e.dao.SysCommonVariable;
import com.silentgo.lc4e.tool.ReflectTool;
import com.silentgo.servlet.http.Request;
import com.silentgo.utils.StringKit;

/**
 * Project : SilentGo
 * Package : com.silentgo.lc4e.web.service
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/13.
 */
@Service
public class SetCommonVariableService {

    @Inject
    ComVarService comVarService;


    public void setComVar(String name, String attrName, Class<?> type, Request request) throws AppException {

        if (StringKit.isBlank(name)) {
            throw new AppException("ComVar Field must be not empty!");
        }
        SysCommonVariable variable = comVarService.getComVarByName(name);
        if (variable == null) {
            throw new AppException("No ComVar Record Found in Database or Cache");
        } else {
            request.setAttribute(StringKit.isBlank(attrName) ? name : attrName,
                    ReflectTool.wrapperObject(type, variable.getValue()));
        }
    }

}
