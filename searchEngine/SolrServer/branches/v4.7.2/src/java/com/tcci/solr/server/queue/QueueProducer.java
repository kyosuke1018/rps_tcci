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
public interface QueueProducer<T extends Object> {
    public QueueContainer<T> getContainer();
    public boolean produce(T entry);
}
