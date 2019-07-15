/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.enums.SapClientEnum;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.et.entity.EtSequence;
import com.tcci.et.enums.SeqTypeEnum;
import com.tcci.fc.entity.org.TcUser;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.http.impl.cookie.DateUtils;

/**
 * 
 * @author Peter.pan
 */
@Stateless
public class EtSequenceFacade extends AbstractFacade<EtSequence> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtSequenceFacade() {
        super(EtSequence.class);
    }
    
    /**
     * 取得下一編號
     * @param type
     * @param com
     * @param operator
     * @return 
     */
    public synchronized Long getNextSeq(SeqTypeEnum type, SapClientEnum com, TcUser operator){
        Map<String, Object> params = new HashMap<String, Object>();
        List<EtSequence> list = findByNamedQuery("EtSequence.findByType", params);
        
        Long sno = 1L;
        if( sys.isEmpty(list) ){
            EtSequence entity = new EtSequence();
            entity.setSapClientCode(com.getSapClientCode());
            entity.setType(type.getCode());
            entity.setSeqNum(sno);
            entity.setModifier(operator!=null?operator.getId():null);
            entity.setModifytime(new Date());
            
            this.create(entity, false);
        }else{
            EtSequence entity = list.get(0);
            sno = entity.getSeqNum()+1;
            entity.setSeqNum(sno);
            
            this.edit(entity, false);
        }
        
        return sno;
    }

    /**
     * 產生標案編號 12
     * ex. W2A190013329
     * @param com
     * @param operator
     * @return 
     */
    public String genTenderCode(SapClientEnum com, TcUser operator){
        return genSeqCode(SeqTypeEnum.TENDER, com, operator);
    }

    /**
     * 產生詢價單編號 12
     * ex. W2B190013329
     * @param com
     * @param operator
     * @return 
     */
    public String genRfqCode(SapClientEnum com, TcUser operator){
        return genSeqCode(SeqTypeEnum.RFQ, com, operator);
    }

    /**
     * 產生報價單編號 12
     * ex. W2C190013329
     * @param com
     * @param operator
     * @return 
     */
    public String genQuoteCode(SapClientEnum com, TcUser operator){
        return genSeqCode(SeqTypeEnum.QUOTE, com, operator);
    }

    /**
     * 產生得標單編號 12
     * ex. W2D190013329
     * @param com
     * @param operator
     * @return 
     */
    public String genAwardCode(SapClientEnum com, TcUser operator){
        return genSeqCode(SeqTypeEnum.AWARD, com, operator);
    }
    
    /**
     * 產生編號
     * @param type
     * @param com
     * @param operator
     * @return 
     */
    public String genSeqCode(SeqTypeEnum type, SapClientEnum com, TcUser operator){
        StringBuilder sb = new StringBuilder();

        String comCode = com.getTenderCodePrefix();// 目前空白
        String yy = DateUtils.formatDate(new Date(), GlobalConstant.FORMAT_YEAR).substring(2);
        String seqStr = Long.toString(getNextSeq(type, com, operator));
        String sno = "0000000" + seqStr;
        sno = sno.substring(seqStr.length());

        sb.append("W").append(comCode).append("2").append(type.getCode()).append(yy).append(sno);
        
        return sb.toString();
    }
        
}
