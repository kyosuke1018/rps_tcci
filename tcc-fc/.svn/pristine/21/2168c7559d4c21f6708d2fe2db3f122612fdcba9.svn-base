/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcservice.facade;

import com.tcci.fcservice.controller.test.FileuploadTest;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Louisz.Cheng
 */
@Stateless
public class FileuploadTestFacade extends AbstractFacade<FileuploadTest> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public FileuploadTestFacade() {
        super(FileuploadTest.class);
    }
    
}
