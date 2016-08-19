/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcservice.controller.util;

import com.tcci.fc.vo.AttachmentVO;

/**
 *
 * @author Jimmy.Lee
 */
public interface AttachmentEventListener {
    public void downloadAction(AttachmentVO vo);
}
