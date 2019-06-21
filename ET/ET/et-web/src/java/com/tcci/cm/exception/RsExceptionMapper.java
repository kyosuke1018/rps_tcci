/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.exception;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.et.enums.RsErrEnum;
import com.tcci.ec.enums.rs.ResStatusEnum;
import com.tcci.et.model.rs.BaseResponseVO;
import com.tcci.et.model.rs.ResponseVO;
import java.time.DateTimeException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
@Provider
public class RsExceptionMapper implements ExceptionMapper<Exception> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public Response toResponse(Exception e) {
        Throwable cause = ExceptionUtils.getRootCause(e);
        if(e instanceof ProcessingException){
            
            if(cause instanceof DateTimeException) {
                logger.error("toResponse DateTimeException ...");
                return genFailRepsone(RsErrEnum.DATETIME.getDisplayName(GlobalConstant.DEF_LOCALE.getLocale()));//"輸入日期時間格式錯誤，或此日期時間不存在!");
            }else{
                logger.error("toResponse cause ...", cause);
                return genFailRepsone(RsErrEnum.INPUT.getDisplayName(GlobalConstant.DEF_LOCALE.getLocale()));//"輸入資料格式錯誤!");
            }
        }
        logger.error("toResponse Exception = ", e.toString());
        logger.error("toResponse RootCause = \n", cause);
        
        return Response.serverError().build();
    }
    
    private Response genFailRepsone(String msg){
        return Response.ok(genBaseFailRepsone(ResStatusEnum.FAIL, msg), MediaType.APPLICATION_JSON).build();
    }
    
    private BaseResponseVO genBaseFailRepsone(ResStatusEnum status, String msg){
        BaseResponseVO vo = new BaseResponseVO();
        ResponseVO res = new ResponseVO();
        res.setStatus(status.getCode());
        res.setMsg(msg!=null?msg:status.getDisplayName(GlobalConstant.DEF_LOCALE.getLocale()));
        vo.setRes(res);

        return vo;
    }
}
