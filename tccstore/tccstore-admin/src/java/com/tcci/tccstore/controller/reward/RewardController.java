/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.reward;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcReward;
import com.tcci.tccstore.entity.EcRewardLog;
import com.tcci.tccstore.enums.RewardEventEnum;
import com.tcci.tccstore.enums.RewardTypeEnum;
import com.tcci.tccstore.facade.member.EcMemberFacade;
import com.tcci.tccstore.facade.reward.EcRewardFacade;
import com.tcci.tccstore.facade.reward.RewardFilter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean
@ViewScoped
public class RewardController {

    private List<EcMember> members = new ArrayList<>();
    private String accountFilter;
    private List<EcRewardLog> rewardLogs;

    private String editAccount;
    private String editType;
    private String editValue;
    private String editDesc = "管理員調整點數";

    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;

    @EJB
    private EcMemberFacade memberFacade;
    @EJB
    private EcRewardFacade rewardFacade;

    private List<EcMember> _members;
    private Map<EcMember, EcReward> mapBonus = new HashMap<>();
    private Map<EcMember, EcReward> mapGold = new HashMap<>();

    // init
    @PostConstruct
    private void init() {
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -7);
//        dateStart = cal.getTime();
        query();
    }

    // action
    public void query() {
        mapBonus.clear();
        mapGold.clear();
        Set<EcMember> set = new HashSet<>();
        List<EcReward> rewards = rewardFacade.findAll();
        for (EcReward reward : rewards) {
            EcMember ecMember = reward.getEcMember();
            set.add(ecMember);
            if (reward.getEcRewardPK().getType() == EcReward.TYPE_BONUS) {
                mapBonus.put(ecMember, reward);
            } else if (reward.getEcRewardPK().getType() == EcReward.TYPE_GOLD) {
                mapGold.put(ecMember, reward);
            }
        }
        _members = new ArrayList<>(set);
        Collections.sort(_members, new Comparator<EcMember>() {
            @Override
            public int compare(EcMember o1, EcMember o2) {
                return o1.getLoginAccount().compareTo(o2.getLoginAccount());
            }
        });
        accountFilterChange();
    }

    public void accountFilterChange() {
        members.clear();
        String tmp = StringUtils.trimToNull(accountFilter);
        if (null == tmp) {
            members.addAll(_members);
        } else {
            for (EcMember ecMember : _members) {
                if (StringUtils.containsIgnoreCase(ecMember.getLoginAccount(), tmp)) {
                    members.add(ecMember);
                }
            }
        }
    }
    
    public void bonusHistory(EcMember row) {
        RewardFilter filter = new RewardFilter();
        filter.setEcMember(row);
        filter.setRewardType(RewardTypeEnum.BONUS);
        rewardLogs = rewardFacade.query(filter);
    }

    public void goldHistory(EcMember row) {
        RewardFilter filter = new RewardFilter();
        filter.setEcMember(row);
        filter.setRewardType(RewardTypeEnum.GOLD);
        rewardLogs = rewardFacade.query(filter);
    }

    public void addValueInit() {
        editType = "2";
    }

    public void addValueOK() {
        EcMember ecMember = memberFacade.findByLoginAccount(editAccount);
        if (null == ecMember) {
            JsfUtil.addErrorMessage("帳號不存在!");
            return;
        }
        try {
            int value = Integer.parseInt(editValue);
            if ("1".equals(editType)) {
                rewardFacade.awardPoints(ecMember, RewardTypeEnum.BONUS, value, RewardEventEnum.ADMIN_UPDATE, editDesc, userSession.getTcUser(), null);
            } else if ("2".equals(editType)) {
                rewardFacade.awardPoints(ecMember, RewardTypeEnum.GOLD, value, RewardEventEnum.ADMIN_UPDATE, editDesc, userSession.getTcUser(), null);
            } else {
                JsfUtil.addErrorMessage("類型錯誤!");
                return;
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
            return;
        }
        JsfUtil.addSuccessMessage("加值成功!");
        query();
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('wvFormEdit').hide()");
        context.update(":form");
    }

    // helper
    public String typeString(EcRewardLog row) {
        if (null == row) {
            return null;
        }
        if (row.getType() == RewardTypeEnum.BONUS.getValue()) {
            return "紅利";
        }
        if (row.getType() == RewardTypeEnum.GOLD.getValue()) {
            return "金幣";
        }
        return String.valueOf(row.getType());
    }

    public EcReward getBonus(EcMember row) {
        return mapBonus.get(row);
    }

    public EcReward getGold(EcMember row) {
        return mapGold.get(row);
    }

    // getter, setter
    public List<EcMember> getMembers() {
        return members;
    }

    public void setMembers(List<EcMember> members) {
        this.members = members;
    }

    public String getAccountFilter() {
        return accountFilter;
    }

    public void setAccountFilter(String accountFilter) {
        this.accountFilter = accountFilter;
    }

    public String getEditAccount() {
        return editAccount;
    }

    public void setEditAccount(String editAccount) {
        this.editAccount = editAccount;
    }

    public String getEditType() {
        return editType;
    }

    public void setEditType(String editType) {
        this.editType = editType;
    }

    public String getEditValue() {
        return editValue;
    }

    public void setEditValue(String editValue) {
        this.editValue = editValue;
    }

    public String getEditDesc() {
        return editDesc;
    }

    public void setEditDesc(String editDesc) {
        this.editDesc = editDesc;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public List<EcRewardLog> getRewardLogs() {
        return rewardLogs;
    }

    public void setRewardLogs(List<EcRewardLog> rewardLogs) {
        this.rewardLogs = rewardLogs;
    }
   
}
