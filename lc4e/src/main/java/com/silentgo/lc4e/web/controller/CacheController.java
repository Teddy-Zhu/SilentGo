package com.silentgo.lc4e.web.controller;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.validator.annotation.RequestString;
import com.silentgo.core.cache.CacheManager;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.route.annotation.ResponseBody;
import com.silentgo.core.route.annotation.Route;
import com.silentgo.lc4e.entity.Message;

/**
 * Created by teddy on 2015/8/12.
 */
@Controller
@Route("/cache")
public class CacheController {

    //    @ValidateParams(value = {
//            @ValidateParam(value = "cacheName", defaultValue = Const.COMVAR),
//            @ValidateParam(value = "key")})
    @ResponseBody
    @Route("/clear")
    public Message clear(@RequestString(defaultValue = "ComVar") String cacheName,
                         @RequestString String key) {
        CacheManager cacheManager = SilentGo.getInstance().getConfig().getCacheManager();
        cacheManager.evict(cacheName, key);
        return new Message(true, "clear " + cacheName + "--" + key + " success");
    }
}
