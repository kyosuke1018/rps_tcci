/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.model.global;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter
 */
public class HitMessageVO {
    private String subject;
    private List<String> contents;

    public boolean hasContent(){
        if( contents!=null ){
            for(String content : contents){
                if( content!=null && !content.isEmpty() ){
                    return true;
                }
            }
        }
        return false;
    }
    
    public void addContent(String content){
        if( contents==null ){
            contents = new ArrayList<String>();
        }
        contents.add(content);
    }
    
    public void clear(){
        subject = "";
        contents = null;
    }
    
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }
}
