/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Jimmy.Lee
 */
public class VelocityMailFormat {
    // white-space: pre-line; 在outlook裡無作用, 僅能用<br/>取代換行
    public String escapeHtml(String original) {
        if (original == null) {
            return "";
        }
        StringBuilder out = new StringBuilder(original.length() * 2);
        char[] chars = original.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            boolean found = true;
            switch (chars[i]) {
                case 60:
                    out.append("&lt;");
                    break; //<
                case 62:
                    out.append("&gt;");
                    break; //>
                case 34:
                    out.append("&quot;");
                    break; //"
                case '\n':
                    out.append("<br/>");
                    break; //newline
                case '\r':
                    break;
            default:
                    found = false;
                    break;
            }
            if (!found) {
                out.append(chars[i]);
            }
        }
        return out.toString();
    }

    public String dateFormat(String pattern, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat (pattern);
        return sdf.format(date);
    }
}
