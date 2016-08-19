/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sap.jco.model;

import java.util.List;

/**
 *
 * @author Peter
 * @param <T>
 */
public interface QueueContainer<T extends Object> {
    public T dequque(String queueName);
    public boolean enqueue(String queueName, T entry);
    public int getSize(String queueName);
    public int restore(String queueName, T entry);
    public List<T> getAll(String queueName);
}
