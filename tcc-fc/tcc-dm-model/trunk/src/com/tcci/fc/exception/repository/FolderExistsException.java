/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.exception.repository;

import com.tcci.fc.util.TcException;

/**
 *
 * @author Neo.Fu
 */
public class FolderExistsException extends TcException {

    public FolderExistsException() {

    }

    public FolderExistsException(String errorMessage) {
        super(errorMessage);
    }
}
