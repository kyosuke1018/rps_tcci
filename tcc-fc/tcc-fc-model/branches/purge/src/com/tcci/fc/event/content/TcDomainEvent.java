package com.tcci.fc.event.content;

import com.tcci.fc.entity.content.TcFvvault;
import com.tcci.fc.entity.essential.TcDomain;
import java.util.List;

/**
 *
 * @author Peter.pan
 */
public class TcDomainEvent {
    public static final int CREATE_EVENT = 0;
    public static final int EDIT_EVENT = 1;
    public static final int DESTROY_EVENT = 2;
    private int action;
    private TcDomain tcDomain;
    private List<TcFvvault> tcFvvaultList; // 要處裡的實體檔案存放位置 (null 值表 TcDomain 關聯的全部 TcFvvault)
    
    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public TcDomain getTcDomain() {
        return tcDomain;
    }

    public void setTcDomain(TcDomain tcDomain) {
        this.tcDomain = tcDomain;
    }

    public List<TcFvvault> getTcFvvaultList() {
        return tcFvvaultList;
    }

    public void setTcFvvaultList(List<TcFvvault> tcFvvaultList) {
        this.tcFvvaultList = tcFvvaultList;
    }
        
}
