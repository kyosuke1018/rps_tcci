/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.solr.server.queue;

/**
 *
 * @author Peter
 */
public interface QueueContainer<T extends Object> {
    public T dequque(String queueName);
    public boolean enqueue(String queueName, T entry);
    public int getSize(String queueName);
    public int restore(String queueName, T entry);
}
