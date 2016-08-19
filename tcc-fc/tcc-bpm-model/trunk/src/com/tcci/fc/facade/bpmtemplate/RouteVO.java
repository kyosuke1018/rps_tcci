/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpmtemplate;

import com.tcci.fc.entity.bpm.TcActivityroutetemplate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
public class RouteVO extends ExcelVOBase {
    @NotNull(message = "fromactivity不能為空")
    @Size(min=1, max = 255, message = "fromactivity至少1個字,最多255個字")
    private String fromactivity;
    @NotNull(message = "routename不能為空")
    @Size(min=1, max = 255, message = "routename至少1個字,最多255個字")
    private String routename;
    @NotNull(message = "toactivity不能為空")
    @Size(min=1, max = 255, message = "toactivity至少1個字,最多255個字")
    private String toactivity;

    // c'tor
    public RouteVO() {
    }
    
    public RouteVO(TcActivityroutetemplate route) {
        fromactivity = route.getFromactivity().getActivityname();
        toactivity = route.getToactivity().getActivityname();
        routename = route.getRoutename();
    }
    
    // getter, setter
    public String getFromactivity() {
        return fromactivity;
    }

    public void setFromactivity(String fromactivity) {
        this.fromactivity = fromactivity;
    }

    public String getRoutename() {
        return routename;
    }

    public void setRoutename(String routename) {
        this.routename = routename;
    }

    public String getToactivity() {
        return toactivity;
    }

    public void setToactivity(String toactivity) {
        this.toactivity = toactivity;
    }
    
}
