package com.silentgo.web.testInject;

import com.silentgo.core.ioc.annotation.Service;

/**
 * Project : silentgo
 * com.silentgo.web.testInject
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/8/31.
 */
@Service
public class bImpl implements bInter {
    @Override
    public void say(String a) {
        System.out.println(a);
    }
}
