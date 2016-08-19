package com.tcci.fc.facade.team;


import com.tcci.fc.entity.role.TcRole;
import com.tcci.fc.entity.role.TcRoleholderrolemap;
import com.tcci.fc.entity.role.TcRoleprincipallink;
import com.tcci.fc.entity.team.TcTeam;
import com.tcci.fc.entity.team.TeamManaged;
import com.tcci.fc.facade.essential.EssentialFacade;
import com.tcci.fc.facade.role.RoleFacade;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Stateless
@Named
public class TeamFacade  {

    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    @EJB
    private EssentialFacade essentialFacade;
    @EJB
    private RoleFacade roleFacade;

    public TcTeam copyTeam(TcTeam fromTeam) {
        TcTeam team = null;
        try {
            team = new TcTeam();
            team.setName(fromTeam.getName());
            team.setDescription(fromTeam.getDescription());
            em.persist(team);

            Collection<TcRole> fromRoles = roleFacade.getRoleHolderRoles(fromTeam);
            for (TcRole fromRole : fromRoles) {
                TcRole role = new TcRole();
                role.setName(fromRole.getName());
                TcRoleholderrolemap map = new TcRoleholderrolemap();
                map.setHolderclassname(team.getClass().getCanonicalName());
                map.setHolderid(team.getId());
                map.setRole(role);
                em.persist(map);
                //role.getRoleHolderRoleMapCollection().add(map);
                em.persist(role);
                Collection<TcRoleprincipallink> fromLinks = fromRole.getTcRoleprincipallinkCollection();
                for (TcRoleprincipallink fromLink : fromLinks) {
                    TcRoleprincipallink link = new TcRoleprincipallink();
                    link.setPrincipalclassname(fromLink.getPrincipalclassname());
                    link.setPrincipalid(fromLink.getPrincipalid());
                    link.setRole(role);
                    em.persist(link);

                    role.getTcRoleprincipallinkCollection().add(link);
                }
                em.flush();
            }

            em.refresh(team);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return team;

    }

    public TcTeam findTeam(String teamName) {
        TcTeam team = null;
        try {
            team = (TcTeam) em.createQuery("SELECT t FROM TcTeam t where t.name = :name").setParameter("name", teamName).getSingleResult();
        } catch (NoResultException nre) {
            team = null;
        }
        return team;
    }


    public List<TcTeam> getTeams() {
        return em.createQuery("SELECT t FROM TcTeam t").getResultList();

    }


    public TeamManaged setTeam(TeamManaged teamManaged, String teamName) {
        TcTeam team = findTeam(teamName);
        teamManaged.setTeam(team);
        return teamManaged;
    }

    public TcTeam createTeam(String teamName, String teamDescription) {
        TcTeam team = new TcTeam();
        team.setName(teamName);
        team.setDescription(teamDescription);
        return team;
    }



}
