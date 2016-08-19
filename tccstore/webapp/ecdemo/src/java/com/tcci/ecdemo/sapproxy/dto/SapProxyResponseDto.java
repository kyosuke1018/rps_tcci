package com.tcci.ecdemo.sapproxy.dto;

import com.tcci.ecdemo.sapproxy.enums.SapProxyResponseEnum;
import com.tcci.ecdemo.sapproxy.enums.SapSystemEnum;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *  SD回傳資料。
 * @author Neo.Fu
 */
public class SapProxyResponseDto {

    /**
     * 系統代碼。
     */
    private SapSystemEnum systemCode;
    /*
     * 回傳代碼。
     */
    private SapProxyResponseEnum returnCode;
    /**
     * 原因代碼。
     * <ul>
     * <li>E01: 員工編號不存在。</li>
     * <li>E99: 資料Lock中，無法進行資料更新。</li>
     * <li>999: 系統錯誤訊息。</li>
     * </ul>
     */
    private String reasonCode;
    /**
     * 說明。
     */
    private String description;
    /**
     * 回傳資料。
     */
    private Object result;

    public SapProxyResponseDto() {
    }

    public SapProxyResponseDto(
            SapSystemEnum systemCode,
            SapProxyResponseEnum returnCode,
            Object result) {
        this.systemCode = systemCode;
        this.returnCode = returnCode;
        this.result = result;
    }

    public SapProxyResponseDto(
            SapSystemEnum systemCode,
            SapProxyResponseEnum returnCode,
            String reasonCode,
            String description,
            Object result) {
        this.systemCode = systemCode;
        this.returnCode = returnCode;
        this.reasonCode = reasonCode;
        this.description = description;
        this.result = result;
    }

    public static SapProxyResponseDto createSuccessResponse(
            SapSystemEnum systemCode) {
        SapProxyResponseDto resp = new SapProxyResponseDto();
        resp.setSystemCode(systemCode);
        resp.setReturnCode(SapProxyResponseEnum.SUCCESS);
        return resp;
    }

    public static SapProxyResponseDto createSuccessResponse(
            SapSystemEnum systemCode,
            Object result) {
        SapProxyResponseDto resp = new SapProxyResponseDto();
        resp.setSystemCode(systemCode);
        resp.setReturnCode(SapProxyResponseEnum.SUCCESS);
        resp.setResult(result);
        return resp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public SapProxyResponseEnum getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(SapProxyResponseEnum returnCode) {
        this.returnCode = returnCode;
    }

    public SapSystemEnum getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(SapSystemEnum systemCode) {
        this.systemCode = systemCode;
    }

    /**
     * 是否執行成功。
     * @return 
     */
    public boolean isSUCCESS() {
        return SapProxyResponseEnum.SUCCESS.equals(this.getReturnCode());
    }

    /**
     * 取得回傳結果，並Casting為BigDecimal。
     */
    public BigDecimal getResultAsBigDecimal() {
        return (BigDecimal) this.getResult();
    }

    /**
     * 取得回傳結果，並Casting為BigDecimalArray。
     */
    public BigDecimal[] getResultAsBigDecimalArray() {
        return (BigDecimal[]) this.getResult();
    }

    /**
     * 取得回傳結果，並Casting為String。
     * @return 
     */
    public String getResultAsString() {
        return (String) this.getResult();
    }

    /**
     * 取得回傳結果，並Casting為String[]。
     * @return 
     */
    public String[] getResultAsStringArray() {
        return (String[]) this.getResult();
    }

    /**
     * 取得回傳結果，並Casting為Object[]。
     * @return 
     */
    public Object[] getResultAsObjectArray() {
        return (Object[]) this.getResult();
    }

    /**
     *  取得回傳結果，並Casting為Map<String, Object>。
     * @return 有Key/Value Map
     */
    public Map<String, Object> getResultAsMap() {
        return (Map<String, Object>) this.getResult();
    }

    /**
     *  取得回傳結果，並Casting為List<Map<String, Object>>。
     * @return 有Key/Value的List
     */
    public List<Map<String, Object>> getResultAsMapList() {
        return (List<Map<String, Object>>) this.getResult();
    }

    /**
     *  取得回傳結果，並Casting為List<SapTableDto>。
     * @return 有Key/Value的List
     */
    public List<SapTableDto> getResultAsSapTableDtoList() {
        return (List<SapTableDto>) this.getResult();
    }

    /**
     *  取得回傳結果，並Casting為SapTableDto。
     * @return 有Key/Value的List
     */
    public SapTableDto getResultAsSapTableDto() {
        return (SapTableDto) this.getResult();
    }
}
