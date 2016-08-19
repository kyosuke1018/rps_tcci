package com.tcci.fc.event.access;

import com.tcci.fc.entity.access.FolderedAccessControlled;
import com.tcci.fc.entity.access.TcFolderedAclEntry;
import com.tcci.fc.entity.filter.access.FolderedAclEntryFilter;
import com.tcci.fc.entity.repository.Foldered;
import com.tcci.fc.entity.repository.TcFolder;
import com.tcci.fc.event.repository.FolderedEvent;
import com.tcci.fc.facade.access.TcFolderedAclEntryFacade;
import javax.ejb.Asynchronous;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@Named
@Asynchronous
public class TcFolderedAclEntryEventHandler {

    @Inject
    TcFolderedAclEntryFacade folderedAclEntryFacade;
    Logger logger = LoggerFactory.getLogger(TcFolderedAclEntryEventHandler.class);

    public void documentReceiptSaveEvent(@Observes(during = TransactionPhase.AFTER_SUCCESS) FolderedEvent folderedEvent) {
        Foldered foldered = folderedEvent.getFoldered();
        switch (folderedEvent.getAction()) {
            case FolderedEvent.CREATE_EVENT:
                if (foldered instanceof FolderedAccessControlled) {
                    copyPermission((FolderedAccessControlled) foldered);
                }
                break;
            case FolderedEvent.EDIT_EVENT:
                //no action need when upload folder.
                break;
            case FolderedEvent.DESTROY_EVENT:
                destroyPermissions((FolderedAccessControlled) foldered);
                break;
        }
    }

    private void copyPermission(FolderedAccessControlled folderedAccessControlled) {
        logger.debug("createPermission");
        FolderedAclEntryFilter filter = new FolderedAclEntryFilter();
        TcFolder folder = folderedAccessControlled.getFolder();
        logger.debug("folder={}", folder);
        filter.setAclTargetClassName(folder.getClass().getCanonicalName());
        filter.setAclTargetId(folder.getId());
        try {
            for (TcFolderedAclEntry parentFolderedAclEntry : folderedAclEntryFacade.findByCriteria(filter)) {
                logger.debug("parentFolderedAclEntry={}", parentFolderedAclEntry);
                TcFolderedAclEntry folderedAclEntry = new TcFolderedAclEntry();
                PropertyUtils.copyProperties(folderedAclEntry, parentFolderedAclEntry);
                folderedAclEntry.setId(null);
                folderedAclEntry.setInheritancemask(folderedAclEntry.getPermissionmask());
                logger.debug("className={}", folderedAccessControlled.getClass().getCanonicalName());
                folderedAclEntry.setAclTargetClassName(folderedAccessControlled.getClass().getCanonicalName());
                folderedAclEntry.setAclTargetId(folderedAccessControlled.getId());
                folderedAclEntryFacade.create(folderedAclEntry);
            }
        } catch (Exception e) {
            logger.error("e={}", e);
        }
    }

    private void destroyPermissions(FolderedAccessControlled folderedAccessControlled) {
        FolderedAclEntryFilter filter = new FolderedAclEntryFilter();
        logger.debug("className={}", folderedAccessControlled.getClass().getCanonicalName());
        filter.setAclTargetClassName(folderedAccessControlled.getClass().getCanonicalName());
        filter.setAclTargetId(folderedAccessControlled.getId());
        for (TcFolderedAclEntry folderedAclEntry : folderedAclEntryFacade.findByCriteria(filter)) {
            folderedAclEntryFacade.remove(folderedAclEntry);
        }
    }
}
