/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.comparator;

import com.tcci.cm.entity.admin.CmFactory;
import java.util.Comparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class CmFactoryComparator implements Comparator<CmFactory> {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public int compare(CmFactory s1, CmFactory s2) {
        if( s1==null ){
            return 0;
        }
        // logger.info("s1 = "+s1.getCode() + ", s2 = "+s2.getCode() + " => s1.compareTo(s2) = "+s1.compareTo(s2));
        return s1.compareTo(s2);
    }
}
