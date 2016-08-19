/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sap.jco.model;

import java.util.concurrent.Future;

/**
 *
 * @author Peter
 * @param <T>
 */
public interface QueueConsumer<T extends Object> {
    public QueueContainer<T> getContainer();
    public Future<Long> consume();
}
