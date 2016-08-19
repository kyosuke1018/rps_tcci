package com.tcci.fc.facade.bpm;

import javax.naming.NamingException;

/**
 *
 * @author shawn_yang
 */
public abstract class AbstractExpression {

    public Object getEJB(String beanName) throws NamingException {
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        Object ejbObject = ctx.lookup("java:comp/env/" + beanName);
        return ejbObject;
    }

    public abstract Object doAction(Object[] objects);
}





