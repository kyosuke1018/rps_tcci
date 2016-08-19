package com.tcci.solr.client.enums;

/**
 * Solr Query Type
 * 
 * The extended dismax parser was based on the original Solr dismax parser. (defType)
 * Supports full lucene query syntax in the absence of syntax errors
 * supports "and"/"or" to mean "AND"/"OR" in lucene syntax mode
 * When there are syntax errors, improved smart partial escaping of special characters is done to prevent them... in this mode, fielded queries, +/-, and phrase queries are still supported.
 * Improved proximity boosting via word bigrams... this prevents the problem of needing 100% of the words in the document to get any boost, as well as having all of the words in a single field.
 * advanced stopword handling... stopwords are not required in the mandatory part of the query but are still used (if indexed) in the proximity boosting part. If a query consists of all stopwords (e.g. to be or not to be) then all will be required.
 * Supports the "boost" parameter.. like the dismax bf param, but multiplies the function query instead of adding it in
 * Supports pure negative nested queries... so a query like +foo (-foo) will match all documents
 * 
 * @author Peter
 */
public enum QueryDefTypeEnum {
    STANDARD(""),
    DISMAX("dismax"),
    EDISMAX("edismax");
    
    private String code;
    
    QueryDefTypeEnum(String code){
        this.code = code;
    }
    
    public static QueryDefTypeEnum getFromCode(String code){
        for (QueryDefTypeEnum enum1 : QueryDefTypeEnum.values()) {
            if (code.trim().equals(enum1.getCode())) {
                return enum1;
            }
        }
        return null; // default
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    //</editor-fold>
}
