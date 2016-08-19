/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpm;

/**
 *
 * @author Jimmy.Lee
 */
public enum BpmEventEnum {
    CompleteProcess,
    TerminateProcess,
    ExecuteExpressionRobot,
    StartActivity,
    CompleteActivity,
    NextActivtyRoute,
    WaitingActivity,
    WorkitemStartNotification
    ;
}
