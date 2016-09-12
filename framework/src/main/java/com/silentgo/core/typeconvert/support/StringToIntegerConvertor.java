package com.silentgo.core.typeconvert.support;

import com.silentgo.core.typeconvert.ITypeConvertor;
import com.silentgo.core.typeconvert.annotation.Convertor;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/26.
 */
@Convertor
public class StringToIntegerConvertor implements ITypeConvertor<String, Integer> {

    private Logger LOGGER = LoggerFactory.getLog(StringToIntegerConvertor.class);

    @Override
    public Integer convert(String source) {
        Integer target = null;
        try {
            target = Integer.valueOf(source);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        return target;
    }
}
