/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.access;

import com.tcci.fc.entity.access.ActorEnum;
import com.tcci.fc.entity.access.FolderedAccessControlled;
import com.tcci.fc.entity.access.TcFolderedAclEntry;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.filter.access.FolderedAclEntryFilter;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcPrincipal;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.org.TcUsergroup;
import com.tcci.fc.entity.repository.Foldered;
import com.tcci.fc.entity.repository.TcFolder;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.fc.facade.repository.RepositoryFacade;
import com.tcci.fc.facade.repository.TcDocumentFacade;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.ManagedType;
import javax.transaction.UserTransaction;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Named
public class TcFolderedAclEntryFacade extends AbstractFacade<TcFolderedAclEntry> {

    private static final Logger logger = LoggerFactory.getLogger(TcFolderedAclEntryFacade.class);
    @EJB
    RepositoryFacade repositoryFacade;
    @EJB
    TcDocumentFacade documentFacade;
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public TcFolderedAclEntryFacade() {
        super(TcFolderedAclEntry.class);
    }

    @Override
    public void create(TcFolderedAclEntry folderedAclEntry) {
        try {
            Class clazz = Class.forName(folderedAclEntry.getAclTargetClassName());
            Persistable aclTarget = (Persistable) em.find(clazz, folderedAclEntry.getAclTargetId());
            clazz = Class.forName(folderedAclEntry.getAclPrincipalClassName());
            TcPrincipal principal = (TcPrincipal) em.find(clazz, folderedAclEntry.getAclPrincipalId());
            if (aclTarget instanceof TcFolder) {
                TcFolder folder = (TcFolder) aclTarget;
                List<TcFolder> subFolders = repositoryFacade.findAllSubFolders(folder, false);
                for (TcFolder subFolder : subFolders) {
                    AclEntrySummary summary = PermissionUtils.newAclEntrySummary((FolderedAccessControlled) subFolder, principal);
                    TcFolderedAclEntry existsFolderedAclEntry = findFolderedAclEntryByFolderedPrincipal(subFolder, principal);
                    if (existsFolderedAclEntry != null) {
                        PermissionUtils.mergePermission(summary, existsFolderedAclEntry);
                        PermissionUtils.mergePermission(summary, folderedAclEntry);
                        existsFolderedAclEntry.setPermissionmask(summary.getPermissionmask());
                        existsFolderedAclEntry.setInheritancemask(folderedAclEntry.getPermissionmask()); //記錄哪些權限是由parent folder繼承而來.
                    } else {
                        existsFolderedAclEntry = new TcFolderedAclEntry();
                        PropertyUtils.copyProperties(existsFolderedAclEntry, folderedAclEntry);
                        existsFolderedAclEntry.setInheritancemask(folderedAclEntry.getPermissionmask()); //記錄哪些權限是由parent folder繼承而來.
                        existsFolderedAclEntry.setAclTargetClassName(subFolder.getClass().getCanonicalName());
                        existsFolderedAclEntry.setAclTargetId(subFolder.getId());
                    }
                    em.merge(existsFolderedAclEntry);
                }
            }
            em.persist(folderedAclEntry);
        } catch (Exception e) {
            logger.error("e={}", e);
        }
    }

    @Override
    public void remove(TcFolderedAclEntry folderedAclEntry) {
        try {
            Class clazz = Class.forName(folderedAclEntry.getAclTargetClassName());
            Persistable aclTarget = (Persistable) em.find(clazz, folderedAclEntry.getAclTargetId());
            clazz = Class.forName(folderedAclEntry.getAclPrincipalClassName());
            TcPrincipal principal = (TcPrincipal) em.find(clazz, folderedAclEntry.getAclPrincipalId());
            if (aclTarget instanceof TcFolder) {
                TcFolder folder = (TcFolder) aclTarget;
                List<TcFolder> subFolders = repositoryFacade.findAllSubFolders(folder, false);
                for (TcFolder subFolder : subFolders) {
                    AclEntrySummary summary = PermissionUtils.newAclEntrySummary((FolderedAccessControlled) subFolder, principal);
                    TcFolderedAclEntry existsFolderedAclEntry = findFolderedAclEntryByFolderedPrincipal(subFolder, principal);
                    if (existsFolderedAclEntry != null) {
                        PermissionUtils.mergePermission(summary, existsFolderedAclEntry);
                        PermissionUtils.minusPermission(summary, folderedAclEntry);
                        existsFolderedAclEntry.setPermissionmask(summary.getPermissionmask());
                        existsFolderedAclEntry.setInheritancemask(summary.getInheritancemask());
                    }
                    if (existsFolderedAclEntry.getPermissionmask().equals(PermissionUtils.ACCESS_POLICY_DEFAULT)) {
                        em.remove(existsFolderedAclEntry);
                    } else {
                        em.merge(existsFolderedAclEntry);
                    }
                }
            }
            em.remove(em.merge(folderedAclEntry));
        } catch (Exception e) {
            logger.error("e={}", e);
        }
    }

    @Override
    public void edit(TcFolderedAclEntry folderedAclEntry) {
        try {
            Class clazz = Class.forName(folderedAclEntry.getAclTargetClassName());
            Persistable aclTarget = (Persistable) em.find(clazz, folderedAclEntry.getAclTargetId());
            clazz = Class.forName(folderedAclEntry.getAclPrincipalClassName());
            TcPrincipal principal = (TcPrincipal) em.find(clazz, folderedAclEntry.getAclPrincipalId());
            if (aclTarget instanceof TcFolder) {
                TcFolderedAclEntry originalFolderedAclEntry = find(folderedAclEntry.getId());
                logger.debug("original permissionmask={}", originalFolderedAclEntry.getPermissionmask());
                logger.debug("new permissionmask={}", folderedAclEntry.getPermissionmask());
                AclEntrySummary updateSummary = PermissionUtils.diff(originalFolderedAclEntry, folderedAclEntry);
                logger.debug("diff={}", updateSummary.getPermissionmask());
                TcFolder folder = (TcFolder) aclTarget;
                List<TcFolder> subFolders = repositoryFacade.findAllSubFolders(folder, false);
                for (TcFolder subFolder : subFolders) {
                    AclEntrySummary summary = PermissionUtils.newAclEntrySummary((FolderedAccessControlled) subFolder, principal);
                    TcFolderedAclEntry existsFolderedAclEntry = findFolderedAclEntryByFolderedPrincipal(subFolder, principal);
                    if (existsFolderedAclEntry != null) {
                        PermissionUtils.mergePermission(summary, existsFolderedAclEntry);
                        PermissionUtils.updatePermission(summary, updateSummary);
                        existsFolderedAclEntry.setPermissionmask(summary.getPermissionmask());
                        existsFolderedAclEntry.setInheritancemask(summary.getPermissionmask());
                    } else {
                        existsFolderedAclEntry = new TcFolderedAclEntry();
                        PropertyUtils.copyProperties(existsFolderedAclEntry, folderedAclEntry);
                        existsFolderedAclEntry.setInheritancemask(existsFolderedAclEntry.getPermissionmask());
                        existsFolderedAclEntry.setAclTargetClassName(subFolder.getClass().getCanonicalName());
                        existsFolderedAclEntry.setAclTargetId(subFolder.getId());
                    }
                    if (existsFolderedAclEntry.getPermissionmask().equals(PermissionUtils.ACCESS_POLICY_DEFAULT)) {
                        em.remove(existsFolderedAclEntry);
                    } else {
                        em.merge(existsFolderedAclEntry);
                    }
                }
            }
            em.merge(folderedAclEntry);
        } catch (Exception e) {
            logger.error("e={}", e);
        }
    }

    /**
     *
     * @param folderedAclEntries TcFolderedAclEntry list.
     * @return unique target foldered list.
     */
    private <T extends Foldered> List<T> getTargetObjects(List<TcFolderedAclEntry> folderedAclEntries) {
        Set<T> uniqueFoldereds = new HashSet<T>();
        for (TcFolderedAclEntry fae : folderedAclEntries) {
            try {
                Class c = Class.forName(fae.getAclTargetClassName());

                T foldered = (T) em.find(c, fae.getAclTargetId());
                if (foldered != null) {
                    uniqueFoldereds.add(foldered);
                }
            } catch (Exception e) {
                logger.error("e={}", e);
            }
        }
        List<T> foldereds = new ArrayList<T>();
        foldereds.addAll(uniqueFoldereds);
        return foldereds;
    }

    /**
     * 查詢有權限的 Foldered (特定Class), 不包括繼承權限的項目.
     *
     * @param principal TcPrincipal
     * @param permission PermissionUtils.XXXX_PERMISSION, XXXX can replay by CREATE,READ,UPDATE,DELETE,MANAGEMENT.
     * @param clazz target object.
     * @return <b>principal</b> can <b>permission</b>'s <b>clazz</b> list.
     */
    public <T extends Foldered> List<T> getAccessibleFoldered(
            TcPrincipal principal,
            int permission,
            Class clazz) {
        FolderedAclEntryFilter filter = new FolderedAclEntryFilter();
        filter.setAclTargetClassName(clazz.getCanonicalName());
        if (principal instanceof TcUser) {
            Set principals = new HashSet();
            TcUser user = (TcUser) principal;
            principals.add(user.getClass().getCanonicalName() + ":" + user.getId());
            for (TcUsergroup usergroup : user.getTcUsergroupCollection()) {
                principals.add(usergroup.getGroupId().getClass().getCanonicalName() + ":" + usergroup.getGroupId().getId());
            }
            filter.setPrincipals(principals);
        } else {
            filter.setAclPrincipalClassName(principal.getClass().getCanonicalName());
            filter.setAclPrincipalId(principal.getId());
        }
        String permissionmask = PermissionUtils.setAclFlag(PermissionUtils.ACCESS_POLICY_DEFAULT, permission, true);
        filter.setPermissionmask(permissionmask);
        List<TcFolderedAclEntry> folderedAclEntries = findByCriteria(filter);
        List<T> foldereds = getTargetObjects(folderedAclEntries);
        return foldereds;
    }

    /**
     * 查詢有權限的 Foldered, 不包括繼承權限的項目.
     *
     * @param principal TcPrincipal
     * @param permission PermissionUtils.XXXX_PERMISSION, XXXX can replay by CREATE,READ,UPDATE,DELETE,MANAGEMENT.
     * @return <b>principal</b> can <b>permission</b>'s all foldered list.
     */
    public <T extends Foldered> List<T> getAccessibleFoldered(
            TcPrincipal principal,
            int permission) {
        FolderedAclEntryFilter filter = new FolderedAclEntryFilter();
        if (principal instanceof TcUser) {
            Set principals = new HashSet();
            TcUser user = (TcUser) principal;
            principals.add(user.getClass().getCanonicalName() + ":" + user.getId());
            for (TcUsergroup usergroup : user.getTcUsergroupCollection()) {
                principals.add(usergroup.getGroupId().getClass().getCanonicalName() + ":" + usergroup.getGroupId().getId());
            }
            filter.setPrincipals(principals);
        } else {
            filter.setAclPrincipalClassName(principal.getClass().getCanonicalName());
            filter.setAclPrincipalId(principal.getId());
        }
        String permissionmask = PermissionUtils.setAclFlag(PermissionUtils.ACCESS_POLICY_DEFAULT, permission, true);
        filter.setPermissionmask(permissionmask);
        List<TcFolderedAclEntry> folderedAclEntries = findByCriteria(filter);
        List<T> foldereds = getTargetObjects(folderedAclEntries);
        return foldereds;
    }

    /**
     * 查詢有權限的 Foldered, 包括繼承權限的項目.
     *
     * @param <T> Foldered
     * @param principal TcPrincipal
     * @param permission PermissionUtils.XXXX_PERMISSION, XXXX can replay by CREATE,READ,UPDATE,DELETE,MANAGEMENT.
     * @return <b>principal</b> can <b>permission</b>'s all foldered list.
     */
    public <T extends Foldered> List<T> getAccessibleFolderedInheritance(
            TcPrincipal principal,
            int permission) {
        List<Class> classes = new ArrayList<Class>();
        for (ManagedType<?> managedType : em.getMetamodel().getManagedTypes()) {
            logger.debug("javaType={}", managedType.getJavaType());
            try {
                Object object = managedType.getJavaType().newInstance();
                if (object instanceof Foldered) {
                    logger.debug("add {}", managedType.getJavaType());
                    classes.add(managedType.getJavaType());
                }
            } catch (Exception e) {
                logger.error("e={}", e);
            }
        }
        return getAccessibleFolderedInheritance(principal, permission, classes);
    }

    /**
     * 查詢有權限的 Foldered (特定Class), 包括繼承權限的項目.
     *
     * @param <T> Foldered
     * @param principal TcPrincipal
     * @param permission PermissionUtils.XXXX_PERMISSION, XXXX can replay by CREATE,READ,UPDATE,DELETE,MANAGEMENT.
     * @Param clazz Class
     * @return <b>principal</b> can <b>permission</b>'s all foldered list.
     */
    public <T extends Foldered> List<T> getAccessibleFolderedInheritance(
            TcPrincipal principal,
            int permission,
            Class clazz) {
        List<Class> classes = new ArrayList<Class>();
        logger.debug("object={}", clazz);
        classes.add(clazz);
        return getAccessibleFolderedInheritance(principal, permission, classes);
    }

    /**
     * 查詢有權限的 Foldered (特定Class), 包括繼承權限的項目.
     *
     * @param <T> Foldered
     * @param principal TcPrincipal
     * @param permission PermissionUtils.XXXX_PERMISSION, XXXX can replay by CREATE,READ,UPDATE,DELETE,MANAGEMENT.
     * @Param classes List<Class>
     * @return <b>principal</b> can <b>permission</b>'s all foldered list.
     */
    public <T extends Foldered> List<T> getAccessibleFolderedInheritance(TcPrincipal principal, int permission, List<Class> classes) {
        FolderedAclEntryFilter filter = new FolderedAclEntryFilter();
        if (principal instanceof TcUser) {
            Set principals = new HashSet();
            TcUser user = (TcUser) principal;
            principals.add(user.getClass().getCanonicalName() + ":" + user.getId());
            for (TcUsergroup usergroup : user.getTcUsergroupCollection()) {
                principals.add(usergroup.getGroupId().getClass().getCanonicalName() + ":" + usergroup.getGroupId().getId());
            }
            filter.setPrincipals(principals);
        } else {
            filter.setAclPrincipalClassName(principal.getClass().getCanonicalName());
            filter.setAclPrincipalId(principal.getId());
        }
        String permissionmask = PermissionUtils.setAclFlag(PermissionUtils.ACCESS_POLICY_DEFAULT, permission, true);
        filter.setPermissionmask(permissionmask);
        List<TcFolderedAclEntry> folderedAclEntries = findByCriteria(filter);
        List<T> foldereds = getTargetObjects(folderedAclEntries);
        Set<T> uniqueFoldered = new HashSet<T>();
        for (Foldered foldered : foldereds) {
            if (foldered instanceof TcFolder) {
                TcFolder folder = (TcFolder) foldered;
                for (Class clazz : classes) {
                    try {
                        List<T> allSubFoldereds = findAllFolderedByFolder(folder, clazz);
                        for (Foldered subFoldered : allSubFoldereds) {
                            uniqueFoldered.add((T) subFoldered);
                        }
                    } catch (Exception e) {
                        logger.error("e={}", e);
                    }
                }
            }
            for (Class clazz : classes) {
                if (foldered.getClass().equals(clazz)) {
                    uniqueFoldered.add((T) foldered);
                    break;
                }
            }
        }
        foldereds.clear();
        foldereds.addAll(uniqueFoldered);
        return foldereds;
    }

    private <T extends Foldered> List<T> findAllFolderedByFolder(TcFolder folder, Class clazz) {
//        StringBuilder cb = new StringBuilder();
//        cb.append("SELECT o FROM ");
//        cb.append(foldered.getClass().getSimpleName());
//        cb.append(" o WHERE o.folder = :folder");
//        logger.debug("sql={}", cb.toString());
//        Query q = em.createQuery(cb.toString());
//        q.setParameter("folder", folder);
//        return q.getResultList();
        logger.debug("clazz={}", clazz);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(clazz);
        Root root = cq.from(clazz);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p1 = cb.equal(root.get("folder").as(TcFolder.class), folder);
        predicateList.add(p1);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        return em.createQuery(cq).getResultList();
    }

    public TcFolderedAclEntry findFolderedAclEntryByFolderedPrincipal(Foldered foldered, TcPrincipal principal) {
        FolderedAclEntryFilter filter = new FolderedAclEntryFilter();
        filter.setAclTargetClassName(foldered.getClass().getCanonicalName());
        filter.setAclTargetId(foldered.getId());
        filter.setAclPrincipalClassName(principal.getClass().getCanonicalName());
        filter.setAclPrincipalId(principal.getId());
        List<TcFolderedAclEntry> folderedAclEntries = findByCriteria(filter);
        if (folderedAclEntries != null && !folderedAclEntries.isEmpty()) {
            return folderedAclEntries.get(0);
        } else {
            return null;
        }
    }

    public List<TcFolderedAclEntry> findByCriteria(FolderedAclEntryFilter filter) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TcFolderedAclEntry> cq = cb.createQuery(TcFolderedAclEntry.class);
        Root<TcFolderedAclEntry> root = cq.from(TcFolderedAclEntry.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();

        if (StringUtils.isNotEmpty(filter.getAclTargetClassName())) {
            logger.debug("aclTargetClassName={}", filter.getAclTargetClassName());
            Predicate p1 = cb.equal(root.get("aclTargetClassName"), filter.getAclTargetClassName());
            predicateList.add(p1);
        }

        if (filter.getAclTargetId() != null) {
            logger.debug("aclTargetId={}", filter.getAclTargetId());
            Predicate p2 = cb.equal(root.get("aclTargetId").as(Long.class), filter.getAclTargetId());
            predicateList.add(p2);
        }

        if (StringUtils.isNotEmpty(filter.getAclPrincipalClassName())) {
            logger.debug("aclPrincipalClassName={}", filter.getAclPrincipalClassName());
            Predicate p3 = cb.equal(root.get("aclPrincipalClassName"), filter.getAclPrincipalClassName());
            predicateList.add(p3);
        }

        if (filter.getAclPrincipalId() != null) {
            logger.debug("aclPrincipalId={}", filter.getAclPrincipalId());
            Predicate p4 = cb.equal(root.get("aclPrincipalId").as(Long.class), filter.getAclPrincipalId());
            predicateList.add(p4);
        }

        if (StringUtils.isNotEmpty(filter.getPermissionmask())) {
            String permissionmask = filter.getPermissionmask();
            permissionmask = permissionmask.replace("0", "_");
            logger.debug("permissionmask={}", permissionmask);
            Predicate p5 = cb.like(root.get("permissionmask").as(String.class), permissionmask);
            predicateList.add(p5);
        }

        if (filter.getPrincipals() != null && !filter.getPrincipals().isEmpty()) {
            logger.debug("principals.size()={}", filter.getPrincipals().size());
            Expression expression1 = root.get("aclPrincipalClassName");
            Expression expression2 = cb.concat(expression1, ":");
            Expression expression3 = root.get("aclPrincipalId");
            Expression expression = cb.concat(expression2, expression3);
            Predicate p6 = expression.in(filter.getPrincipals());
            predicateList.add(p6);
        }

        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        return getEntityManager().createQuery(cq).getResultList();
    }

    public boolean hasPermission(FolderedAccessControlled folderedAccessControlled, TcPrincipal principal, int accessRight) {
        AclEntrySummary summary = getAclEntry(folderedAccessControlled, principal);
        return ((summary == null) ? false : PermissionUtils.getAclFlag(summary.getPermissionmask(), accessRight));
    }

    public AclEntrySummary getAclEntry(
            FolderedAccessControlled folderedAccessControlled,
            TcPrincipal principal) {
        AclEntrySummary aclSummary = PermissionUtils.newAclEntrySummary(folderedAccessControlled, principal);

        // ==========================================================
        // If folderedAccessControlled or principal is null, return no access permission
        // ==========================================================
        if (folderedAccessControlled == null || principal == null) {
            return aclSummary;
        }

        // ==========================================================
        // To get belonging group(s)
        // ==========================================================
        Set principals = new HashSet();
        principals.add(principal.getClass().getCanonicalName() + principal.getId());
        //To get the user and all of his/her groups
        if (principal instanceof TcUser) {
            TcUser user = (TcUser) principal;
            Collection<TcUsergroup> usergroups = user.getTcUsergroupCollection();
            for (TcUsergroup usergroup : usergroups) {
                TcGroup department = (TcGroup) usergroup.getGroupId();
                principals.add(department.getClass().getCanonicalName() + principal.getId());
            }
        }
        logger.debug("getAclEntry(Object object, HTUser principal): " + principal);

        PermissionUtils.mergePermission(aclSummary, getAclEntry(folderedAccessControlled, principals));
        logger.debug("getAclEntry(FolderedAccessControlled folderedAccessControlled, HTUser principal): AccessControlled Object: " + aclSummary.getPermissionmask());
        return aclSummary;
    }

    public AclEntrySummary getAclEntry(FolderedAccessControlled folderedAccessControlled, Set principals) {
        FolderedAccessControlled parent = null;
        AclEntrySummary result = PermissionUtils.newAclEntrySummary(folderedAccessControlled, null);

        // ======================================
        // To get the target item
        // ======================================
        if (folderedAccessControlled instanceof Foldered) {
            Foldered item = (Foldered) folderedAccessControlled;
            result.setAclTargetClassName(item.getClass().getCanonicalName());
            result.setAclTargetId(item.getId());
            parent = (FolderedAccessControlled) item.getFolder();
        }

        // ===================================================================================================
        // Object level permission: To get the Access Control List by the target item and all principals
        // ===================================================================================================
        String where = "";
        for (Iterator keys = principals.iterator(); keys.hasNext();) {
            String key = (String) keys.next();
            if (!where.equals("")) {
                where += ", ";
            }
            where += "'" + key + "'";
        }
        String sql = "SELECT a FROM TcFolderedAclEntry a "
                + " WHERE a.aclTargetClassName = :aclTargetClassName"
                + " AND  a.aclTargetId = :aclTargetId"
                + " AND (a.aclPrincipalClassName='" + ActorEnum.ALL.name() + "' OR concat(a.aclPrincipalClassName,TRIM(a.aclPrincipalId)) IN (" + where + "))";

        Query query = em.createQuery(sql);
        query.setParameter("aclTargetClassName", result.getAclTargetClassName());
        query.setParameter("aclTargetId", result.getAclTargetId());

        // ======================================
        // To merge all Access Control List into one
        // ======================================
        List aclentries = query.getResultList();
        logger.debug("getAclEntry(FolderedAccessControlled folderedAccessControlled, Set principals, LifecycleState state): lifecycle result size=" + result.getAclCount());
        if (aclentries != null && aclentries.size() > 0) {
            // Update aclcount
            result.addAclCount(aclentries.size());

            // Merge permission mask                 
            logger.debug("getAclEntry(FolderedAccessControlled folderedAccessControlled, Set principals): Found: " + aclentries.size());
            for (int i = 0; i < aclentries.size(); i++) {
                TcFolderedAclEntry entry = (TcFolderedAclEntry) aclentries.get(i);

                logger.debug("getAclEntry(FolderedAccessControlled folderedAccessControlled, Set principals): lifecycle entry=" + entry + " (" + entry.getPermissionmask() + ")" + entry.getAclPrincipalClassName() + ":" + entry.getAclPrincipalId() + " on " + entry.getAclTargetClassName() + ":" + entry.getAclTargetId());
                PermissionUtils.mergePermission(result, entry);
            }
        }
        if (!(folderedAccessControlled instanceof TcFolder) && parent != null) {

            // ===============================================================
            // If target object not TcFolder
            // Go to check its parent's permission
            // ===============================================================
            PermissionUtils.mergePermission(result, getAclEntry(parent, principals));
        }
        logger.debug("getAclEntry(FolderedAccessControlled folderedAccessControlled, Set principals): entry=(" + result.getPermissionmask() + ")" + result.getAclPrincipalClassName() + ":" + result.getAclPrincipalId() + " on " + result.getAclTargetClassName() + ":" + result.getAclTargetId());
        return result;
    }
}
