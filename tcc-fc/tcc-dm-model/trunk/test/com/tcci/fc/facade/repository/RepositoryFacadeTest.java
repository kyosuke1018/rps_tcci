/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.repository;

import com.tcci.fc.entity.repository.TcFolder;
import java.util.List;
import java.io.File;
import javax.naming.Context;
import javax.ejb.embeddable.EJBContainer;
import java.util.Map;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Wayne.Hu
 */
public class RepositoryFacadeTest {
    public static EJBContainer container;
    public static Context context;
    public static RepositoryFacade facade;
    
    
    public RepositoryFacadeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        //Map properties = new HashMap();
        //properties.put(EJBContainer.MODULES, new File("target/classes"));
        //properties.put("org.glassfish.ejb.embedded.glassfish.installation.root", "D:/glassfish3.1/glassfish");
        //container = EJBContainer.createEJBContainer(properties);
        container = EJBContainer.createEJBContainer();
        context = container.getContext();
        facade = (RepositoryFacade) context.lookup("java:global/classes/RepositoryFacade!com.tcci.fc.facade.repository.RepositoryFacade");        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void hello() {
         List<TcFolder> folders = facade.findFirstLevelFolders();
         for (TcFolder folder: folders) {
             System.out.println("folder="+folder.getName());
         }
     }
}
