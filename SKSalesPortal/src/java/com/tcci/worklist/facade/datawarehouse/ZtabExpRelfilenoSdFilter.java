package com.tcci.worklist.facade.datawarehouse;

import com.tcci.worklist.enums.ReviewOptionEnum;
import java.util.Date;

/**
 *
 * @author nEO.Fu
 */
public class ZtabExpRelfilenoSdFilter {

    private Date audatBegin;
    private Date audatEnd;
    private String bersl;
    private String vbeln;
    private String vkorg;
    private String vtweg;
    private String bname;

    public Date getAudatBegin() {
        return audatBegin;
    }

    public void setAudatBegin(Date audatBegin) {
        this.audatBegin = audatBegin;
    }

    public Date getAudatEnd() {
        return audatEnd;
    }

    public void setAudatEnd(Date audatEnd) {
        this.audatEnd = audatEnd;
    }

    public String getVbeln() {
        return vbeln;
    }

    public void setVbeln(String vbeln) {
        this.vbeln = vbeln;
    }

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    public String getVtweg() {
        return vtweg;
    }

    public void setVtweg(String vtweg) {
        this.vtweg = vtweg;
    }

    public String getBersl() {
        return bersl;
    }

    public void setBersl(String bersl) {
        this.bersl = bersl;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }
}
