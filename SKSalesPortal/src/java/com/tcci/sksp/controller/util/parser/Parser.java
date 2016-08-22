/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.util.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author jason.yu
 */
public interface Parser {

    public void setFileName(String fileName);

    public void setInputStream(InputStream inputStream);

    public List<List<Object>> parse()throws IOException;
}
