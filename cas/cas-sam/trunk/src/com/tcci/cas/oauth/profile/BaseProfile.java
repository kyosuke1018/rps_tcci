/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cas.oauth.profile;

import java.util.Map;

/**
 *
 * @author Neo.Fu
 */
public class BaseProfile {

    private Map<String, Object> attributes;

    public BaseProfile(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }

    @Override
    public String toString() {
        String result = "[";
        String attributes = "";
        for (String key : this.attributes.keySet()) {
            result = result + key + "=" + this.attributes.get(key);
        }
        result = result + attributes + "]";
        return result;
    }
}
