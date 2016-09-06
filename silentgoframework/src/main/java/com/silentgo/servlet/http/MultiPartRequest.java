package com.silentgo.servlet.http;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.servlet.http
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/1.
 */
public class MultiPartRequest extends Request {

    private HashMap<String, MultiFile> files;


    public MultiPartRequest(HttpServletRequest request, List<MultiFile> orifiles) {
        super(request);
        this.files = new HashMap<>();
        orifiles.forEach(file -> files.put(file.getFormName(), file));
    }

    public void delete() {
        if (files != null && files.size() > 0) {
            files.forEach((k, v) -> {
                if (v.file.exists()) v.file.delete();
            });
        }
    }

    public MultiPartRequest(HttpServletRequest request) {
        super(request);
    }

    public MultiFile getFile(String name) {
        return files.get(name);
    }
}
