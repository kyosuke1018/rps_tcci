package com.tcci.fc.entity.bpm.enumeration;


public enum ExecutionStateEnum {

    NOT_START,
    RUNNING,
    COMPLETED,
    TERMINATED,
    ARCHIVED,
    WAITING,
    HOLD;

    public String getName() {
        return name();
    }
}
