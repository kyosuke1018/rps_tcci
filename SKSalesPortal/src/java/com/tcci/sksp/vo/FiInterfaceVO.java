package com.tcci.sksp.vo;

import com.tcci.sksp.entity.ar.SkFiDetailInterface;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 *
 * @author nEO.Fu
 */
public class FiInterfaceVO {

    private String masterReferenceclassname;
    private Long masterReferenceid;
    private Date createtimestamp;
    private String transactionNo;
    private SkFiDetailInterface detail;

    public String getMasterReferenceclassname() {
        return masterReferenceclassname;
    }

    public void setMasterReferenceclassname(String masterReferenceclassname) {
        this.masterReferenceclassname = masterReferenceclassname;
    }

    public Long getMasterReferenceid() {
        return masterReferenceid;
    }

    public void setMasterReferenceid(Long masterReferenceid) {
        this.masterReferenceid = masterReferenceid;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public SkFiDetailInterface getDetail() {
        return detail;
    }

    public void setDetail(SkFiDetailInterface detail) {
        this.detail = detail;
    }

    /** 排序基準:
     * 1.已上傳 (有建立時間):
     *   以建立時間+transaction item 排序.
     * 2.未上傳:
     *   以referenceclassname+id+transaction item 排序.
     */
    public static List<FiInterfaceVO> sortInterfaceVOList(List<FiInterfaceVO> interfaceVOList) {
        Collections.sort(interfaceVOList, new Comparator<FiInterfaceVO>() {

            @Override
            public int compare(FiInterfaceVO v1, FiInterfaceVO v2) {
                if (v1.getCreatetimestamp() != null && v2.getCreatetimestamp() != null) {
                    if (v1.getCreatetimestamp().equals(v2.getCreatetimestamp())) {
                        if (v1.getTransactionNo().equals(v2.getTransactionNo())) {
                            return v1.getDetail().getTransactionItem().compareTo(v2.getDetail().getTransactionItem());
                        } else {
                            return v1.getTransactionNo().compareTo(v2.getTransactionNo());
                        }
                    } else {
                        return v1.getCreatetimestamp().compareTo(v2.getCreatetimestamp());
                    }
                } else if (v1.getCreatetimestamp() != null && v2.getCreatetimestamp() == null) {
                    return 1;
                } else if (v1.getCreatetimestamp() == null && v2.getCreatetimestamp() != null) {
                    return 0;
                } else {
                    String oid1 = v1.getMasterReferenceclassname() + ":" + v1.getMasterReferenceid();
                    String oid2 = v2.getMasterReferenceclassname() + ":" + v2.getMasterReferenceid();
                    if (oid1.equals(oid2)) {
                        return v1.getDetail().getTransactionItem().compareTo(v2.getDetail().getTransactionItem());
                    } else {
                        return oid1.compareTo(oid2);
                    }
                }
            }
        });
        return interfaceVOList;
    }
}
