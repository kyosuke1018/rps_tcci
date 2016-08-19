/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpm;

import com.tcci.fc.entity.bpm.TcActivity;
import com.tcci.fc.entity.bpm.TcProcess;
import com.tcci.fc.entity.bpm.TcWorkitem;
import com.tcci.fc.entity.org.TcUser;
import java.util.List;
import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import static javax.interceptor.Interceptor.Priority.APPLICATION;

/**
 *
 * @author Jimmy.Lee
 */
@Alternative @Priority(APPLICATION+1)
public class BpmEngineEx extends BPMEngineImpl {
    // 暫存執行completeWorkitem的operator
    private static ThreadLocal<TcUser> threadLocal = new ThreadLocal<TcUser>();

    // 當workitem啟動時
    // app可實作通知owner有一筆待簽核項目
    @Override
    public void onWorkitemStartNotification(TcWorkitem workitem) {
        System.out.println(workitem.getOwner().getDisplayIdentifier() + " has a workitem id:" + workitem.getId());
    }
    
    // 當執行 EXPRESSION_ROBOT activity
    // app 可依據 activity.getExpression() 執行不同功能。
    // 回傳值請指定 next activity route
    @Override
    public String onExecuteExpressionRobot(TcActivity activity) {
        String result = "success";
        System.out.println("Robot activity(" + activity.getId() + ") " + activity.getActivityname() + " return " + result);
        return result;
    }

    // case 1:activity必簽,但沒有指定role users
    // case 2:onExecuteExpressionRobot 拋出exception
    // app 可實作通知系統管理員來處理。
    @Override
    public void onWaitingActivity(TcActivity activity) {
        System.out.println("activity(" + activity.getId() + ") " + activity.getActivityname() + " 等待處理!");
    }

    // 當activity執行到END
    // app 可實作修改表單狀態, 通知申請人...
    @Override
    public void onCompleteProcess(TcProcess process) {
        TcUser operator = threadLocal.get();
        String completedBy = null == operator ? "" : " by " + operator.getDisplayIdentifier();
        System.out.println("process(" + process.getId() + ") completed" + completedBy + ".");
    }

    // 當直接呼叫terminateProcess或簽核時routename是'TERMINATE'
    // app 可實作修改表單狀態, 通知申請人...
    @Override
    public void onTerminateProcess(TcProcess process) {
        TcUser operator = process.getTerminator();
        String terminatedBy = null == operator ? "" : " by " + operator.getDisplayIdentifier();
        System.out.println("process(" + process.getId() + ") terminated" + terminatedBy + ".");
    }

    // 簽核API
    // ballot: UI的簽核項目,例如 approve, reject, reassign, partialApproval (請於msgBpm.properties定義其中文名稱)
    // routeName: 實際的 next activity route. 例如 approve, partialApproval可設為approve. 若是設TERMINATE將立即停止流程
    //            若設為不存在的route時(如reject) 最後會選最的實際route為next activity
    // operator是執行的人,若與workitem.owner不同時且allowAgent是false時會拋出exception
    @Override
    public void completeWorkitem(TcWorkitem workitem, String ballot, String routeName, String comments, TcUser operator, boolean allowAgent) {
        System.out.println("completeWorkitem by " + operator);
        threadLocal.set(operator);
        super.completeWorkitem(workitem, ballot, routeName, comments, operator, allowAgent);
        threadLocal.remove();
    }

    // 批次通知待簽事項
    // APP自訂schedule來呼叫batchNotifyRunningWorkitems, 並Override底下實作
    @Override
    public void onBatchNotifyRunningWorkitems(TcUser owner, List<TcWorkitem> workitems) {
        System.out.println(owner.getCname() + "有下列申請單待簽核:");
        for (TcWorkitem workitem : workitems) {
            StringBuilder sb = new StringBuilder();
            sb.append("id:").append(workitem.getId())
              .append(", ").append(workitem.getActivityname())
              .append(", ").append(workitem.getStarttimestamp());
            System.out.println(sb.toString());
        }
    }

}
