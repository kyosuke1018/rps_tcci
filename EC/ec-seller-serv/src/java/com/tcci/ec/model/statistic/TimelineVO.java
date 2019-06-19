/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.statistic;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Peter.pan
 */
public class TimelineVO {
    private String title;
    private String timeLevel;
    private List<String> timeAxis;

    private Map<String, List<Double>> valueAxisMap;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeLevel() {
        return timeLevel;
    }

    public void setTimeLevel(String timeLevel) {
        this.timeLevel = timeLevel;
    }

    public Map<String, List<Double>> getValueAxisMap() {
        return valueAxisMap;
    }

    public void setValueAxisMap(Map<String, List<Double>> valueAxisMap) {
        this.valueAxisMap = valueAxisMap;
    }

    public List<String> getTimeAxis() {
        return timeAxis;
    }

    public void setTimeAxis(List<String> timeAxis) {
        this.timeAxis = timeAxis;
    }
}
