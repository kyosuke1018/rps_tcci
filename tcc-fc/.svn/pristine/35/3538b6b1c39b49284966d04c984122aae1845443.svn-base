/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fc.controller.bpm;

import com.tcci.fc.entity.bpm.enumeration.ExecutionStateEnum;

/**
 *
 * @author Greg.Chou
 */
class TcciWorkflowStatusMapper {
    public static WorkflowStatus mapStatus(Object status) {
        ExecutionStateEnum execState = (ExecutionStateEnum)status;

        if (execState.equals(execState.COMPLETED)) {
            return WorkflowStatus.COMPLETED;
        }else if (execState.equals(execState.RUNNING)) {
            return WorkflowStatus.PENDING;
        }else if (execState.equals(execState.TERMINATED)) {
            return WorkflowStatus.CANCEL;
        }else {
            return WorkflowStatus.UNKNOWN;
        }
    }
}
