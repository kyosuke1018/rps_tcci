/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util.ssoclient;

/**
 *
 * @author Jimmy.Lee
 */
class SSOClientExpiredException extends SSOClientException {

    public SSOClientExpiredException() {
        super("認證過期!", 401);
    }
    
}
