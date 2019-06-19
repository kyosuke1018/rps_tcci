/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.model.interfaces;

import java.util.List;

/**
 *
 * @author Peter
 */
public interface IResultSet<T> {
    public List<T> getResultSet();
    public long getRealSize();
    public boolean isOverSize();
}
