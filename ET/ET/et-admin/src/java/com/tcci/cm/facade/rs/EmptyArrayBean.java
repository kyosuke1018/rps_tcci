/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.rs;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@XmlRootElement
public class EmptyArrayBean {

    public String[] emtpyArray = new String[0];
}
