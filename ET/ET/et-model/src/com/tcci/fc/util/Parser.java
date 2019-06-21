/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/**
 *
 * @author jason.yu
 */
public interface Parser {

    public void setFileName(String fileName);

    public void setInputStream(InputStream inputStream);

    public Vector parse()throws IOException;
}
