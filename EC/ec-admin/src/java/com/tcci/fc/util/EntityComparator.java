/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason.Yu
 * @param <T>
 */
public class EntityComparator<T> implements Comparator<T>, Serializable  {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private static final long serialVersionUID = 0L;
    private String property;
    private OrderTypeEnum orderType;

    public enum OrderTypeEnum {
        ASC, DESC
    }

    public EntityComparator(String property, OrderTypeEnum orderType) {
        this.property = property;
        this.orderType = orderType;
    }

    public EntityComparator(String property) {
        this.property = property;
        this.orderType = OrderTypeEnum.ASC;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public int compare(T object1, T object2) {
        try {
            Comparable id1 = (Comparable) PropertyUtils.getProperty(object1, property);
            Comparable id2 = (Comparable) PropertyUtils.getProperty(object2, property);
            switch (orderType) {
            case ASC:
                return id1.compareTo(id2);
            case DESC:
                return id2.compareTo(id1);
            }
        } catch (Exception e) {
            logger.debug("EntityComparator compare:\n", e);
        }
        return -1;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public OrderTypeEnum getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderTypeEnum orderType) {
        this.orderType = orderType;
    }


}
