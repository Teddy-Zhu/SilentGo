// Copyright (C) 1998-2001 by Jason Hunter <jhunter_AT_acm_DOT_org>.
// All rights reserved.  Use of this class is limited.
// Please see the LICENSE for more information.

package com.silentgo.servlet.oreilly;

import com.silentgo.servlet.http.MultiFile;
import com.silentgo.servlet.http.MultiPartRequest;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.oreilly.multipart.Part;
import com.silentgo.servlet.oreilly.multipart.FilePart;
import com.silentgo.servlet.oreilly.multipart.FileRenamePolicy;
import com.silentgo.servlet.oreilly.multipart.MultipartParser;
import com.silentgo.servlet.oreilly.multipart.ParamPart;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * A utility class to handle <code>multipart/form-data</code> requests,
 * the kind of requests that support file uploads.  This class emulates the
 * interface of <code>HttpServletRequest</code>, making it familiar to use.
 * It uses a "push" model where any incoming files are read and saved directly
 * to disk in the constructor. If you wish to have more flexibility, e.g.
 * write the files to a database, use the "pull" model
 * <code>MultipartParser</code> instead.
 * <p>
 * This class can receive arbitrarily large files (up to an artificial limit
 * you can set), and fairly efficiently too.
 * It cannot handle nested data (multipart content within multipart content).
 * It <b>can</b> now with the latest release handle internationalized content
 * (such as non Latin-1 filenames).
 * <p>
 * To avoid collisions and have fine control over file placement, there's a
 * constructor variety that takes a pluggable FileRenamePolicy implementation.
 * A particular policy can choose to rename or change the location of the file
 * before it's written.
 * <p>
 * See the included upload.war for an example of how to use this class.
 * <p>
 * The full file upload specification is contained in experimental RFC 1867,
 * available at <a href="http://www.ietf.org/rfc/rfc1867.txt">
 * http://www.ietf.org/rfc/rfc1867.txt</a>.
 *
 * @author Jason Hunter
 * @author Geoff Soutter
 * @version 1.0, 1998/09/18<br>
 * @see MultipartParser
 */
public class MultipartRequestParser {

    private static final int DEFAULT_MAX_POST_SIZE = 1024 * 1024;  // 1 Meg

    /**
     * Constructs a new MultipartRequestParser to handle the specified request,
     * saving any uploaded files to the given directory, and limiting the
     * upload size to the specified length.  If the content is too large, an
     * IOException is thrown.  This constructor actually parses the
     * <tt>multipart/form-data</tt> and throws an IOException if there's any
     * problem reading or parsing the request.
     * <p>
     * To avoid file collisions, this constructor takes an implementation of the
     * FileRenamePolicy interface to allow a pluggable rename policy.
     *
     * @param request       the servlet request.
     * @param saveDirectory the directory in which to save any uploaded files.
     * @param maxPostSize   the maximum size of the POST content.
     * @param encoding      the encoding of the response, such as ISO-8859-1
     * @param policy        a pluggable file rename policy
     * @throws IOException if the uploaded content is larger than
     *                     <tt>maxPostSize</tt> or there's a problem reading or parsing the request.
     */
    public static MultiPartRequest parseMultiPartRequest(Request request,
                                                         String saveDirectory,
                                                         int maxPostSize,
                                                         String encoding,
                                                         FileRenamePolicy policy) throws IOException {
        // Sanity check values
        if (request == null)
            throw new IllegalArgumentException("request cannot be null");
        if (saveDirectory == null)
            throw new IllegalArgumentException("saveDirectory cannot be null");

        // Save the dir
        File dir = new File(saveDirectory);

        if (!dir.exists()) {
            dir.mkdirs();
        }
        // Check saveDirectory is truly a directory
        if (!dir.isDirectory())
            throw new IllegalArgumentException("Not a directory: " + saveDirectory);

        // Check saveDirectory is writable
        if (!dir.canWrite())
            throw new IllegalArgumentException("Not writable: " + saveDirectory);

        // Parse the incoming multipart, storing files in the dir provided,
        // and populate the meta objects which describe what we found
        MultipartParser parser =
                new MultipartParser(request, maxPostSize > -1 ? maxPostSize : DEFAULT_MAX_POST_SIZE, true, true, encoding);

        Map<String, MultiFile> fileMap = new HashMap<>();

        Part part;
        boolean hasParameters = false;
        while ((part = parser.readNextPart()) != null) {
            String name = part.getName();
            if (name == null) {
                throw new IOException("Malformed input: parameter name missing (known Opera 7 bug)");
            }
            if (part.isParam()) {
                hasParameters = true;
                // It's a parameter part, add it to the vector of values
                ParamPart paramPart = (ParamPart) part;
                String value = paramPart.getStringValue();
                String[] existingValues = request.getParameterValues(name);
                if (existingValues == null) {
                    existingValues = new String[]{value};
                    request.addParameter(name, existingValues);
                }
            } else if (part.isFile()) {
                // It's a file part
                FilePart filePart = (FilePart) part;
                String fileName = filePart.getFileName();
                if (fileName != null) {
                    filePart.setRenamePolicy(policy);  // null policy is OK
                    // The part actually contained a file
                    filePart.writeTo(dir);
                    MultiFile file = new MultiFile();
                    file.setContentType(filePart.getContentType());
                    file.setFormName(name);
                    file.setFileName(filePart.getFileName());
                    String newFileName = filePart.getFileName();
                    file.setTmpName(fileName);
                    file.setExt(newFileName.substring(newFileName.lastIndexOf('.'), newFileName.length()));
                    file.setFile(new File(dir + File.separator + newFileName));
                    fileMap.put(name, file);
                } else {
                    //ignore
                }
            }
        }

        if (hasParameters) request.rebuildResovedMap();

        return new MultiPartRequest(request, fileMap);
    }

}