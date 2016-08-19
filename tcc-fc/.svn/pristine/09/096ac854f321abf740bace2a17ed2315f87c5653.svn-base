package com.tcci.fc.util;


public class DefaultSequenceGenerator implements SequenceGenerator {

    private int allocationSize = 1;
    private String startValue = "1";

    public String getNextValue(String value) {
        return String.valueOf(Integer.parseInt(value == null ? "0" : value) + getAllocationSize());
    }

    public String getStartValue() {
        return this.startValue;
    }

    public void setStartValue(String startValue) {
        this.startValue = startValue;
    }

    public int getAllocationSize() {
        return allocationSize;
    }

    public void setAllocationSize(int allocationSize) {
        this.allocationSize = allocationSize;
    }
}
