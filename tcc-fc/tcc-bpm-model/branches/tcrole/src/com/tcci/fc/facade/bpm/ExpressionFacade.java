package com.tcci.fc.facade.bpm;

import com.tcci.fc.entity.bpm.TcActivity;
import com.tcci.fc.entity.bpm.TcProcess;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.facade.essential.EssentialFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//@Stateless
public class ExpressionFacade  {

//    @PersistenceContext(unitName="Model")
//    private EntityManager em;
//
//   @EJB
//    private EssentialFacade essentialFacade;

    private static final String EXPR_METHOD_NAME = "doAction";

    private AbstractExpression generateExpressionClass(String expression) throws Exception {

        // get default Class Pool, the class loader is system class loader
        // so, we need to add class path into it.
        // It's a singleton pool.
        ClassPool cPool = ClassPool.getDefault();
        if (cPool.find(this.getClass().getName()) == null) {
            // if we can't find this class in class pool, that mean the class
            // path isn't correct.
            // so, we get the class loader of this class.
            // It suppose to be a EJBClassLoader.
            java.lang.ClassLoader cll = this.getClass().getClassLoader();
            // Then, we create a LoaderClassPath of the EJBClassLoader,
            // it will copy out the class path of the EJBClassLoader into the LoaderClassPath
            LoaderClassPath lcp = new LoaderClassPath(cll);
            // see what class path of this EJBClassLoader.
            System.out.println(lcp);
            // set the class path into our class pool
            cPool.insertClassPath(lcp);
        }

        // now, get the concrete Expression name, this is the class we want to modify.
        String oldClassName = ConcreteExpression.class.getName();
        // Because a JVM class loader cannot load a class twice, we should create a unique class name.
        // we will copy old ExpressionConcrete into a new ExpressionConcreteXXXXXXX later.
        String newClassName = oldClassName + UUID.randomUUID().toString().replace("-", "");
        CtClass ctClass = cPool.get(oldClassName);
        // to defrost the old ExpressionConcrete before we want to modify it.
        ctClass.defrost();
        //Now copy the ExpressionConcrete into a new ExpressionConcreteXXXXXXXXXXXXXXX
        ctClass.setName(newClassName);

        //Now, let's get the method
        CtMethod doMethod = ctClass.getDeclaredMethod(EXPR_METHOD_NAME);
        // Generate the method body.
        StringBuffer body = new StringBuffer();
        body.append("{");
        body.append("  Object result = null;");
        body.append("  Object self = $1[0];");
        body.append("  Object primaryObject = $1[1];");
        StringBuffer argsIn = new StringBuffer();
        StringBuffer argsOut = new StringBuffer();
        body.append(argsIn.toString());
        body.append("  try{ ");
        body.append(expression);
        body.append("  } catch (Exception e) { e.printStackTrace();throw e;}");
        body.append(argsOut.toString());
        body.append("  return result;");
        body.append("}");

        //See what we gen
        System.out.println("New Created Expression Method Body");
        System.out.println(body);

        //Inject the method body.
        doMethod.setBody(body.toString());
        // load the new class into this class loader.
        Class clazz = ctClass.toClass(this.getClass().getClassLoader(), this.getClass().getProtectionDomain());
        // detach the temp class for avoid out of memory.
        ctClass.detach();

        return (AbstractExpression) clazz.newInstance();

    }



    public Object invokeExpression(Persistable execObj, Persistable primaryObject, String expression) throws Exception {
        AbstractExpression expressionTemplate;
        Object result = null;
        //init();

        try {
            //======================================================================================
            // To fetch variable for the execution object which can be Bpmprocess and Bpmacitity
            //======================================================================================
            /*
            String pboOid = null;
            if (execObj instanceof TcProcess) {
                TcProcess process = (TcProcess) execObj;
                if (process.getPrimaryobjectclassname() != null && process.getPrimaryobjectid() != 0) {
                    pboOid = process.getPrimaryobjectclassname() + ":" + process.getPrimaryobjectid();
                }
            } else if (execObj instanceof TcActivity) {
                TcActivity activity = (TcActivity) execObj;
                if (activity.getProcessid().getPrimaryobjectclassname() != null && activity.getProcessid().getPrimaryobjectid() != 0) {
                    pboOid = activity.getProcessid().getPrimaryobjectclassname() + ":" + activity.getProcessid().getPrimaryobjectid();
                }
            }
            Object primaryBusinessObject = (pboOid != null) ? essentialFacade.getObject(pboOid) : null;
            */
            // =================================================================================================
            // To initialize input parameters. Index 0 is for self and Index 1 is for primaryBusinessObject
            // =================================================================================================
                        
            Object[] args = new Object[2];
            args[0] = execObj;
            args[1] = primaryObject;

            //======================================================================================
            // To generate .java file for compiling
            //======================================================================================
            //String classname = "Expr" + Calendar.getInstance().getTimeInMillis();
            expressionTemplate = generateExpressionClass(expression);


            //======================================================================================
            // To invoke invokeAction method in the new generated class
            //======================================================================================
            try {


                result = expressionTemplate.doAction(args);

 
            } catch (Exception e) {
                e.printStackTrace();

                throw new Exception("Exception: " + e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception: " + e);
        }

        return result;
    }
    
  public static void main(String arguments[]) {
        ExpressionFacade test1 = new ExpressionFacade();
        String expression ="result=com.tcci.hep.util.WorkflowUtil.hasCosign1(self, primaryObject);";
        Object[] args = new Object[2];
        args[0] = "Hello";
        args[1] = "World";
        
        try {
            AbstractExpression expressionTemplate = test1.generateExpressionClass(expression);
            Object result = expressionTemplate.doAction(args);
            System.out.println("result="+result);
        } catch (Exception ex) {
            Logger.getLogger(ExpressionFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
  

}
