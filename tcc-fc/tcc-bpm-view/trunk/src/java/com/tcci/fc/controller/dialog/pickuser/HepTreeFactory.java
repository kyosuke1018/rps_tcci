/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.dialog.pickuser;

//import com.tcci.hep.entity.org.HepCompany;
//import com.tcci.hep.entity.org.HepDepartment;
//import com.tcci.hep.entity.org.HepEmployee;
//import com.tcci.hep.facade.org.HepCompanyFacade;
//import com.tcci.hep.facade.org.HepDepartmentFacade;
//import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Greg.Chou
 */

@ManagedBean(name="hepTreeFactory")
@ApplicationScoped
public class HepTreeFactory implements TreeFactory{
//    @EJB HepCompanyFacade companyFacade;
//    @EJB HepDepartmentFacade depFacade;
    
    private TreeNode root = new TreeNode();

//    @PostConstruct
//    private void init() {
//        for(HepCompany company: companyFacade.findAll()) {
//            TreeNode compNode = new TreeNode();
//            compNode.setCode(company.getCode());
//            compNode.setName(company.getName());
//            
//            root.addChild(compNode);
//            
//            for (HepDepartment dep: depFacade.findFirstLevelDepartmentsByCompany(company)) {
//                createChild(compNode, dep);
//            }
//        }
//    }
//
//    private void createChild(TreeNode rootNode, HepDepartment dep) {
//        TreeNode depNode = new TreeNode();
//        depNode.setCode(dep.getCode());
//        depNode.setName(dep.getName());
//        
//        rootNode.addChild(depNode);
//        
//        for (HepDepartment subDep: dep.getHepDepartmentCollection()) {
//            createChild(depNode, subDep);
//        }
//        
//        for (HepEmployee emp: dep.getHepEmployeeCollection()) {
//            TreeLeaf empNode = new TreeLeaf();
//            empNode.setCode(emp.getCode());
//            empNode.setName(emp.getName());
//            empNode.setEmail(emp.getEmail());
//            
//            depNode.addChild(empNode);
//        }
//    }
    
    @Override
    public TreeNode getRoot() {
        return this.root;
    }
}
