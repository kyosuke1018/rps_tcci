/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author bryant.yu
 */
public class ByteArrayUploadedFile implements UploadedFile {

    private final byte [] data;

    private final String filename;

    private final String contentType;

    public ByteArrayUploadedFile(byte [] data, String filename, String contentType) {
        this.data = data;
        this.filename = filename;
        this.contentType = contentType;
    }

    @Override
    public String getFileName() {
        return filename;
    }

    @Override
    public InputStream getInputstream() throws IOException {
        return new ByteArrayInputStream(data);
    }

    @Override
    public long getSize() {
        return data.length;
    }

    @Override
    public byte[] getContents() {
        return data;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void write(String filePath) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(data);
        }
    }

    @Override
    public List<String> getFileNames() {
        List<String> list = new ArrayList<String>();
        list.add(filename);
        
        return list;
    }
}
