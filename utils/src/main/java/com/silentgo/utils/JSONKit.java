package com.silentgo.utils;

import com.silentgo.utils.json.FastJsonPaser;
import com.silentgo.utils.json.GsonPaser;
import com.silentgo.utils.json.JsonPaser;
import com.silentgo.utils.json.SilentGoParser;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2017/1/23.
 */
public class JSONKit {

    public static final JsonPaser getJSONParser() {
        JsonPaser paser = null;
        try {
            paser = new SilentGoParser();
        } catch (Exception e) {
            try {
                paser = new GsonPaser();
            } catch (Exception ex) {
                paser = new FastJsonPaser();
            }
        }
        return paser;
    }
}
