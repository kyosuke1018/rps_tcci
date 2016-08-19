/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.NumberTool;

import com.tcci.mail.template.MailBean;
import java.util.Properties;

//import com.tcci.mail.template.ApprovalMailBean;
/**
 *
 * @author Jason.Yu
 */
public class VelocityTemplateUtils {

//    static{
//        ClassLoader classLoader = VelocityTemplateUtils.class.getClass().getClassLoader();
//       System.out.println(classLoader.getResourceAsStream("approval.html").toString());
//
//    }
    public static String getContent(Object bean, String templateName) throws Exception {
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        p.setProperty(Velocity.ENCODING_DEFAULT, "UTF8");
        p.setProperty(Velocity.INPUT_ENCODING, "UTF8");
        p.setProperty(Velocity.OUTPUT_ENCODING, "UTF8");

        //Velocity.setProperty(Velocity.RESOURCE_LOADER, "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        // Velocity.setProperty(Velocity., "org.apache.velocity.runtime.resource.loader.FileResourceLoader");

        Velocity.init(p);

        StringWriter writer = new StringWriter();

        NumberTool number = new NumberTool();
        VelocityContext context = new VelocityContext();

        context.put("bean", bean);//data bean
        context.put("number", number);//for number format
        Template template = null;
        //try {
        template = Velocity.getTemplate(templateName);
        template.setEncoding("UTF-8");

        //} catch (Exception ex) {
        //    throw new SPException(ex);
        //}
        try {
            if (template != null) {
                template.merge(context, writer);

            }

            return writer.toString();
            //} catch (IOException ex) {
            //    throw new SPException(ex);
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
//                logger.log(Level.SEVERE,ex.getMessage(), ex);
            }
        }
    }

    public static void main(String[] arg) {
        MailBean bean = new MailBean();
        bean.setActorId("鄭正略");
        bean.setAppSubject("加班申請-加班申請單");
        bean.setAppType("加班單");
        bean.setReason("加班");
        bean.setRequester("周世俊");
        bean.setSubject("表單簽核");
        try {
            System.out.println(VelocityTemplateUtils.getContent(bean, bean.getTemplateName()));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
