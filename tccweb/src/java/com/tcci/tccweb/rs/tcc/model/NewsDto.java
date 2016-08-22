
package com.tcci.tccweb.rs.tcc.model;

/**
 *
 * @author Jackson.Lee
 */
public class NewsDto {
    
    private Integer idnum;
    private String yy;
    private String subject;
    private String subjectCHS;
    private String subjecten;
    private String memo;
    private String memoCHS;
    private String memoen;

    public Integer getIdnum() {
        return idnum;
    }

    public void setIdnum(Integer idnum) {
        this.idnum = idnum;
    }

    public String getYy() {
        return yy;
    }

    public void setYy(String yy) {
        this.yy = yy;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectCHS() {
        return subjectCHS;
    }

    public void setSubjectCHS(String subjectCHS) {
        this.subjectCHS = subjectCHS;
    }

    public String getSubjecten() {
        return subjecten;
    }

    public void setSubjecten(String subjecten) {
        this.subjecten = subjecten;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMemoCHS() {
        return memoCHS;
    }

    public void setMemoCHS(String memoCHS) {
        this.memoCHS = memoCHS;
    }

    public String getMemoen() {
        return memoen;
    }

    public void setMemoen(String memoen) {
        this.memoen = memoen;
    }

    
}
