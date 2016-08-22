package com.tcci.tccweb.rs.util;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Greg.Chou
 */

@XmlRootElement
public class ResponseMessageDto {
    public static ResponseMessageDto OK = new ResponseMessageDto(MessageCode.SUCCESS, "Success");

    private MessageCode msgCode;
    private String description;

    public ResponseMessageDto() {}

    public ResponseMessageDto(MessageCode msgCode, String desc) {
        this.msgCode = msgCode;
        this.description = desc;
    }

    public MessageCode getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(MessageCode msgCode) {
        this.msgCode = msgCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
