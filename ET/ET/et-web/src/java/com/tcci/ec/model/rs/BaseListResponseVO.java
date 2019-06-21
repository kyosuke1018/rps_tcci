/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.rs;

import com.tcci.et.model.rs.BaseResponseVO;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@XmlRootElement
public class BaseListResponseVO extends BaseResponseVO {
    private List list;
    private Integer offset;
    private Integer rowNum;
    private Integer totalRows;

    public BaseListResponseVO(){}
    
    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }
    
    
}
