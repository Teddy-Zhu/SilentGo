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
 *         Created by teddyzhu on 16/8/20.
 */
public class StringToLongConvertor implements ITypeConvertor<String, Long> {

    public static final Logger LOGGER = LoggerFactory.getLogger(StringToLongConvertor.class);

    @Override
    public Long convert(String source) {
        Long result = null;
        try {
            result = Long.parseLong(source);
        } catch (NumberFormatException e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }
}
