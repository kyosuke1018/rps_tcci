/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.member;

/**
 *
 * @author Jimmy.Lee
 */
public class AccountNotExistException extends Exception {

    public AccountNotExistException(String msg) {
        super(msg);
    }
}
