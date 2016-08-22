package com.tcci.sapproxy;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.worklist.facade.datawarehouse.ZtabExpRelfilenoSdFilter;
import com.tcci.sapproxy.dto.SapProxyResponseDto;
import com.tcci.sksp.entity.quotation.SkQuotationMaster;
import com.tcci.sksp.vo.FiInterfaceVO;
import com.tcci.worklist.vo.ZtabExpRelfilenoSdVO;
import java.util.List;
import java.util.Properties;

/**
 * sap upload
 *
 * @author Jackson.lee
 */
public interface PpProxy {

    /**
     * Proxy 初始化
     *
     * @param sapClientCode
     * @param sapServiceUrl
     * @param operator
     */
    public void init(String sapClientCode, String sapServiceUrl, String operator);

    public void setSapClientCode(String sapClientCode);

    public void setSapServiceUrl(String sapServiceUrl);

    public void setOperator(String operator);

    /**
     * Proxy 初始化
     *
     * @param props
     */
    public void init(Properties props);

    /**
     * 清除 Cache
     */
    public void clearRepository();

    /**
     * 設定通知人員資訊
     *
     * @param users
     */
    public void setNotifyUsers(List<TcUser> users);

        /**
     * 上傳傳票介面資料. (RFC_ZWIS_FI_CLAR_SKPC)
     *
     * @param
     * @return 回傳SAP Table:<br/>
     * SapProxyReponseDto.getResultAsSapTableDto():
     * @throws Exception
     */
    SapProxyResponseDto doUpload(List<FiInterfaceVO> interfaceVOList) throws Exception;

    /**
     * 建立報價單. (ZSAP_JAVA_IMP_QT_CREATE)
     *
     * @param quotationMaster 報價單主檔
     * @return 回傳SAP table: <br/>
     * SapProxyResponseDto.getResultAsSapTableDto():
     * @thorws Exception
     */
    SapProxyResponseDto createQuotation(SkQuotationMaster quotationMaster, String mandt) throws Exception;

    /**
     * 銷售文件核發(A)、核退 (R)或復原核發狀態 (O). (ZSAP_JAVA_IMP_SD_RELEASE_CODE)
     *
     * @param bname 使用者
     * @param bersl 權限碼
     * @param vbeln 銷售文件單號
     * @param posnr 項次
     * @param comment 備註
     * @param usrmode A: Approval, R: Return and O:Open
     * @return 回傳SAP Table:<br/> SapProxyReponseDto.getResultAsSapTableDto():
     * @throws Exception
     */
    SapProxyResponseDto doSdDocumentRelease(List<ZtabExpRelfilenoSdVO> VOs, String usermode) throws Exception;

    /**
     * SD 依傳入的 BNAME 及權限碼，取得所有帶出待核文件號碼。
     *
     * @param bname
     * @param bersl
     * @return 回傳一筆資料:<br/>
     * SapProxyReponseDto.getResultAsSapTableDto():
     * @throws Exception
     */
    SapProxyResponseDto findExpRelFileNoSDs(ZtabExpRelfilenoSdFilter filter, String userMode) throws Exception;

    /**
     * Proxy 關閉，包含連結關閉、資料清除等工作
     */
    public void dispose();
}
