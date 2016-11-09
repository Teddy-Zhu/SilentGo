package com.silentgo.utils.convertor;


import com.silentgo.utils.inter.ITypeConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/26.
 */
public class StringToIntegerConvertor implements ITypeConvertor<String, Integer> {

    private Logger LOGGER = LoggerFactory.getLogger(StringToIntegerConvertor.class);

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
