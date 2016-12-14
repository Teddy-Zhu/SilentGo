package com.silentgo.core.action.gzip;

import com.silentgo.servlet.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by teddy on 2015/9/23.
 */

public class GZIPResponseWrapper extends Response {

    private static final Logger LOGGER = LoggerFactory.getLogger(GZIPResponseWrapper.class);
    protected Response origResponse = null;
    protected ServletOutputStream stream = null;
    protected PrintWriter writer = null;

    public GZIPResponseWrapper(Response response) {
        super(response);
        origResponse = response;
    }

    private ServletOutputStream createOutputStream() throws IOException {
        return new GZIPResponseStream(origResponse);
    }

    public void finish() {
        try {
            if (writer != null) {
                writer.close();
            } else {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (IOException e) {
            LOGGER.error("close gzip response stream error", e);
        }
    }

    @Override
    public void flushBuffer() throws IOException {
        stream.flush();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called!");
        }

        if (stream == null)
            stream = createOutputStream();
        return stream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer != null) {
            return writer;
        }

        if (stream != null) {
            throw new IllegalStateException("getOutputStream() has already been called!");
        }

        stream = createOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(stream, origResponse.getCharacterEncoding()));
        return writer;
    }


}
