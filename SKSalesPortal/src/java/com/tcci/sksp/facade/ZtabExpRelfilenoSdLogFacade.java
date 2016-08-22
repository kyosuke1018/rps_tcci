/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.worklist.entity.ZtabExpRelfilenoSdLog;
import com.tcci.worklist.vo.ZtabExpRelfilenoSdVO;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author Neo.Fu
 */
@Stateless
public class ZtabExpRelfilenoSdLogFacade extends AbstractFacade<ZtabExpRelfilenoSdLog> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ZtabExpRelfilenoSdLogFacade() {
        super(ZtabExpRelfilenoSdLog.class);
    }

    public void createLog(String mandt, List<ZtabExpRelfilenoSdVO> VOs, String usermode, String signer) throws Exception {
        for (ZtabExpRelfilenoSdVO vo : VOs) {
            ZtabExpRelfilenoSdLog log = new ZtabExpRelfilenoSdLog();
            Date now = new Date();
            PropertyUtils.copyProperties(log, vo.getZtabExpRelfilenoSd());
            log.setId(null);
            log.setMandt(mandt);
            log.setCreateTimeStamp(now);
            log.setUsermode(usermode);
            log.setSigner(signer);
            this.create(log);
        }
    }
}
