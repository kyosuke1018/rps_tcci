/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testjerseyclient;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.facade.customer.EcCustomerFacade;
import com.tcci.ec.facade.member.EcMemberFacade;
import java.util.List;
import javax.ejb.EJB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
public class TestModel {
    private static final Logger logger = LoggerFactory.getLogger(TestModel.class);
    
    @EJB
    private EcMemberFacade ecMemberFacade;
    @EJB
    protected EcCustomerFacade ecCustomerFacade;
    
    public static void main(String[] args) {
        logger.debug("TestModel!");
        
    }
}
