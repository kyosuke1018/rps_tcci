/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.security;

import java.util.Map;

/**
 *
 * @author Peter.pan
 */
public interface AESPasswordHash {
    public void initialize(Map<String, String> parameters);
    public String generate(char[] password);
    public boolean verify(char[] password, String hashedPassword);
    public String encrypt(String generatedKey);
}
