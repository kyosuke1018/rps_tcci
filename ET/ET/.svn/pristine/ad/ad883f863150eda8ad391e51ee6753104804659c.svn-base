/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs;

import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.enums.rs.ResStatusEnum;
import com.tcci.et.model.TenderVO;
import com.tcci.et.model.rs.ResponseVO;
import com.tcci.et.model.rs.TenderRsVO;
import com.tcci.et.enums.LanguageEnum;
import javax.ejb.Stateless;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST 專用資料轉換程式
 * @author Kyle.Cheng
 */
@Stateless
public class RestDataFacade {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    
    /**
     * 轉換至輸出格式
     * @param vo
     * @param lang
     * @param widthRes
     * @return 
     */
    public TenderRsVO toTenderRsVO(TenderVO vo, LanguageEnum lang, boolean widthRes){
        TenderRsVO rsVO = new TenderRsVO();
        if( vo==null || vo.getId()==null ){
            return rsVO;
        }
        ExtBeanUtils.copyProperties(rsVO, vo);
        
        if( widthRes ){
            ResponseVO resVO = new ResponseVO();
            resVO.setStatus(ResStatusEnum.SUCCESS.getCode());
            resVO.setTotalRows(1);
            rsVO.setRes(resVO);
        }
        
        return rsVO;
    }
}
