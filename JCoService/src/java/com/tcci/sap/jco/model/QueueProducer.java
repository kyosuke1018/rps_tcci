/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sap.jco.model;


/**
 *
 * @author Peter
 * @param <T>
 */
public interface QueueProducer<T extends Object> {
    public QueueContainer<T> getContainer();
    public boolean produce(T entry);
}
