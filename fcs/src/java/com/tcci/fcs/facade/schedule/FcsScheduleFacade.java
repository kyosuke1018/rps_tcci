/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.facade.schedule;

import com.tcci.fcs.entity.FcSapUploadRecord;
import com.tcci.fcs.enums.FcConfigKeyEnum;
import com.tcci.fcs.facade.FcConfigFacade;
import com.tcci.fcs.facade.FcSapUploadRecordFacade;
import com.tcci.irs.facade.IrsTranUploadRecordFacade;
import com.tcci.rpt.facade.RptDataUploadRecordFacade;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class FcsScheduleFacade {
    private static Logger logger = LoggerFactory.getLogger(FcsScheduleFacade.class);
//    @Inject
//    private TcUserFacade userFacade;
//    @Inject
//    private ExchangeRateFacade exchangeRateFacade;
    
    @Inject
    private FcSapUploadRecordFacade fcSapUploadRecordFacade;
    @Inject
    private IrsTranUploadRecordFacade irsTranUploadRecordFacade;
    @Inject
    private RptDataUploadRecordFacade rptDataUploadRecordFacade;
    @EJB
    private FcConfigFacade configFacade;
    
    
    /*更新月結匯率 每月一號排程更新
    20151029 停用 不提供每月參考值*/
//    @Schedule(dayOfMonth="1", hour = "9", minute = "0", second = "0", persistent = false)
//    public void updateMonthlyRate(){
//        logger.debug("updateMonthlyRate start!");
////        exchangeRateFacade.update(userFacade.find(new Long("1")));//admin 執行更新
//        TcUser admin = userFacade.findUserByLoginAccount("administrator");
//        String results = exchangeRateFacade.update(admin);
//        logger.info("FcsScheduleFacade updateMonthlyRate results:"+results);
//    }
    
    /*每15分鐘排程更新
    20160328 新增排程 每15分鐘以SAP上傳紀錄 更新(關係人對帳;合併報表)共用上傳紀錄檔*/
//    @Schedule(hour = "*", minute = "0,15,30,45", second = "0", persistent = false)
    @Schedule(hour = "*", minute = "*/15", second = "0", persistent = false)
    public void updateUploadRecordBatch(){
        logger.debug("updateUploadRecordBatch start!");
        try {
            this.updateUploadRecord(true);
            //20160704 prod排程未執行 設定寫入執行時間
            SimpleDateFormat sdfTime = new SimpleDateFormat("yyyyMMddHHmmss");
            configFacade.saveValue(FcConfigKeyEnum.BATCH_UPLOAD_RECORD, sdfTime.format(new Date()));
        } catch (Exception ex) {
//            ex.printStackTrace();
            logger.error("updateUploadRecordBatch execute fail:"+ex);
        }
    }
    //UI batch共用
    public void updateUploadRecord(boolean thisSeason) throws Exception{
        logger.debug("updateUploadRecord start!");
        List<FcSapUploadRecord> results = fcSapUploadRecordFacade.findLastUpdateList(thisSeason);
        if (CollectionUtils.isNotEmpty(results)) {
            logger.info("FcsScheduleFacade updateUploadRecord results:"+results.size());
            for(FcSapUploadRecord record : results){
                if(record.getUploadTimestamp()!=null){
                    if("A".equals(record.getFunc())){//IRS
                        irsTranUploadRecordFacade.sapUpdate(record.getYearmonth(), record.getCompanyCode(), record.getUploadTimestamp(), record.getUserName());
                    }else if("B".equals(record.getFunc())){//RPT
                        rptDataUploadRecordFacade.sapUpdate(record.getYearmonth(), record.getCompanyCode(), record.getUploadTimestamp(), record.getUserName());
                    }
                }
            }
        }
    }
    
    //20160511合併報表供財務使用者triger, 只更新指定月份; 只更新合併報表
    public void updateUploadRecord(String uiFunc, String yearmonth) throws Exception{
        logger.debug("updateUploadRecord start!");
        List<FcSapUploadRecord> results = fcSapUploadRecordFacade.findLastUpdateList(yearmonth);
        if (CollectionUtils.isNotEmpty(results)) {
            logger.info("FcsScheduleFacade updateUploadRecord results:"+results.size());
            for(FcSapUploadRecord record : results){
                if(record.getUploadTimestamp()!=null){
                    if(!uiFunc.equals(record.getFunc())){//是否為UI指定func
                        continue;
                    }
                    if("A".equals(record.getFunc())){//IRS
                        irsTranUploadRecordFacade.sapUpdate(record.getYearmonth(), record.getCompanyCode(), record.getUploadTimestamp(), record.getUserName());
                    }else if("B".equals(record.getFunc())){//RPT
                        rptDataUploadRecordFacade.sapUpdate(record.getYearmonth(), record.getCompanyCode(), record.getUploadTimestamp(), record.getUserName());
                    }
                }
            }
        }
    }
}
