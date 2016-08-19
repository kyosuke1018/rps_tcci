/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.role;

import com.tcci.fc.entity.role.RoleEnum;
import com.tcci.fc.entity.role.RoleHolder;
import com.tcci.fc.entity.role.TcRole;
import com.tcci.fc.entity.role.TcRoleholderrolemap;
import com.tcci.fc.entity.role.TcRoleprincipallink;
import com.tcci.fc.facade.essential.EssentialFacade;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Wayne.Hu
 */
@Stateless
@Named
public class RoleFacade {
        @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    @EJB
    private EssentialFacade essentialFacade;

        public List<TcRoleholderrolemap> getRoleHolderRoleMaps(RoleHolder holder) {
        List<TcRoleholderrolemap> maps = new ArrayList<TcRoleholderrolemap>();

        String sql = "SELECT r FROM TcRoleholderrolemap r " +
                " WHERE r.holderclassname = :holderClassName " +
                "   AND r.holderid = :holderId";

        if (holder != null) {
            try {
                Query query = em.createQuery(sql);
                query.setParameter("holderClassName", holder.getClass().getCanonicalName());
                query.setParameter("holderId", holder.getId());
                maps = query.getResultList();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return maps;
    }

    public Collection<TcRole> getRoleHolderRoles(RoleHolder holder) {
        Collection<TcRole> roles = new ArrayList<TcRole>();

        List<TcRoleholderrolemap> list = getRoleHolderRoleMaps(holder);
        for (TcRoleholderrolemap map : list) {
            roles.add(map.getRole());
        }
        return roles;
    }

    public TcRole getRoleHolderRole(RoleHolder holder, RoleEnum roleName) {
        TcRole role = null;

        List<TcRoleholderrolemap> list = getRoleHolderRoleMaps(holder);
        for (TcRoleholderrolemap map : list) {
            if (map.getRole().getName().equals(roleName)) {
                role = map.getRole();
                break;
            }
        }
        return role;
    }


    public RoleHolder saveRoleHolder(RoleHolder roleHolder, Collection<TcRole> newRoles) throws Exception {
        try {

            /**
             *            1                     1        1     
             * teamTemplate --> roleHolderRoleMap --> role --> rolePrincipalLink
             *                  *                     1        *
             */
            if (roleHolder.getId() == null) {
                em.persist(roleHolder);
                for (TcRole r : newRoles) {
                    em.persist(r);

                    TcRoleholderrolemap roleHolderRoleMap = new TcRoleholderrolemap();
                    roleHolderRoleMap.setHolderclassname(roleHolder.getClass().getCanonicalName());
                    roleHolderRoleMap.setHolderid(roleHolder.getId());
                    roleHolderRoleMap.setRole(r);

                    em.persist(roleHolderRoleMap);

                    for (TcRoleprincipallink link : r.getTcRoleprincipallinkCollection()) {
                        em.persist(link);
                    }
                }
            } else {
                // put new roles in a map
                Map<RoleEnum, TcRole> newRoleMap = new HashMap<RoleEnum, TcRole>();
                for (TcRole r : newRoles) {
                    newRoleMap.put(r.getName(), r);
                }

                roleHolder = em.merge(roleHolder);
                List<TcRoleholderrolemap> oldRoleHolderRoleMaps = getRoleHolderRoleMaps(roleHolder);
                // old roles loop
                for (Iterator<TcRoleholderrolemap> oldRoleIte = oldRoleHolderRoleMaps.iterator(); oldRoleIte.hasNext();) {
                    TcRoleholderrolemap roleHolderRoleMap = oldRoleIte.next();
                    TcRole oldRole = roleHolderRoleMap.getRole();

                    roleHolderRoleMap = em.merge(roleHolderRoleMap);
                    oldRole = em.merge(oldRole);

                    // if old role in new role list, merge its principal links
                    if (newRoleMap.containsKey(oldRole.getName())) {
                        TcRole newRole = newRoleMap.get(oldRole.getName());

                        // new role principal links to map
                        Map<String, TcRoleprincipallink> newLinkMap = new HashMap<String, TcRoleprincipallink>();
                        for (TcRoleprincipallink newLink : newRole.getTcRoleprincipallinkCollection()) {
                            String key = newLink.getRole().getName() + "_" + newLink.getPrincipalclassname() + ":" + newLink.getPrincipalid();
                            newLinkMap.put(key, newLink);
                        }

                        // old principal links
                        for (Iterator<TcRoleprincipallink> oldLinkIte = oldRole.getTcRoleprincipallinkCollection().iterator(); oldLinkIte.hasNext();) {
                            TcRoleprincipallink oldLink = oldLinkIte.next();
                            String key = oldLink.getRole().getName() + "_" + oldLink.getPrincipalclassname() + ":" + oldLink.getPrincipalid();

                            // if old principal link not in new principal list,
                            // then remove the principal link
                            if (!newLinkMap.containsKey(key)) {
                                em.remove(oldLink);
                                oldLinkIte.remove();
                            } else {
                                newLinkMap.remove(key);
                            }

                        }

                        // add the rest of new principal links to old role
                        for (TcRoleprincipallink newLink : newLinkMap.values()) {
                          oldRole.getTcRoleprincipallinkCollection().add(newLink);
                        }


                        em.merge(oldRole);
                        newRoleMap.remove(oldRole.getName());

                    } // if old not in new then delete old one
                    else {
                        em.remove(roleHolderRoleMap);
                        em.remove(oldRole);
                    }

                }

                for (TcRole r : newRoleMap.values()) {

                    TcRoleholderrolemap roleHolderRoleMap = new TcRoleholderrolemap();
                    roleHolderRoleMap.setHolderclassname(roleHolder.getClass().getCanonicalName());
                    roleHolderRoleMap.setHolderid(roleHolder.getId());
                    roleHolderRoleMap.setRole(r);

                    em.persist(roleHolderRoleMap);

                    // r.setId(null);
                    r.getTcRoleholderrolemapCollection().add(roleHolderRoleMap);

                    em.merge(roleHolderRoleMap);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();

            throw new Exception(e);
        }

        return roleHolder;
    }


}
