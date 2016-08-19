/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.bpm.admin;

import com.tcci.fc.entity.bpm.TcActivityroutetemplate;
import com.tcci.fc.entity.bpm.TcActivitytemplate;
import com.tcci.fc.entity.bpm.TcProcesstemplate;
import com.tcci.fc.facade.bpm.BPMFacade;
import com.tcci.fc.facade.bpm.BPMTemplateFacade;
import com.tcci.fc.jaxb.ActivityroutetemplateConverter;
import com.tcci.fc.jaxb.ActivitytemplateConverter;
import com.tcci.fc.jaxb.ProcesstemplateConverter;
import com.tcci.fc.util.BPMException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Jason.Yu
 */
@ManagedBean
@ViewScoped
public class ProcessTemplateController implements Serializable {

    @EJB
    BPMTemplateFacade bpmTemplateFacade;
    @EJB
    BPMFacade bpmFacade;
    private UploadedFile templateFile;
    private TcProcesstemplate selectedProcesstemplate;

    public TcProcesstemplate getSelectedProcesstemplate() {
        return selectedProcesstemplate;
    }

    public void setSelectedProcesstemplate(TcProcesstemplate selectedProcesstemplate) {
        this.selectedProcesstemplate = selectedProcesstemplate;
    }

    public UploadedFile getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(UploadedFile file) {
        this.templateFile = file;
    }

    public String uploadProcessTemplate() {
        if (templateFile != null) {
            try {
                System.out.println("selectedProcesstemplate=" +  selectedProcesstemplate );
                unmarshallerThenSave(templateFile.getInputstream(), selectedProcesstemplate);
                String message = "Done to upload process!";
                FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, message, message);
                FacesContext.getCurrentInstance().addMessage( null , facesMsg );
            } catch (Exception ex) {
                Logger.getLogger(ProcessTemplateController.class.getName()).log(Level.SEVERE, null, ex);
                FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage());
                FacesContext.getCurrentInstance().addMessage( null , facesMsg );
                ex.printStackTrace();
            }
        } else {
            String message = "Error: you have to selected a file!";
            Logger.getLogger(this.getClass().getCanonicalName()).log(Level.SEVERE, message);
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
            FacesContext.getCurrentInstance().addMessage( null , facesMsg );
        }
        return null;
    }
    /*
    public void handleNewProcessTemplateUpload(FileUploadEvent event) {
    System.out.println("handleNewProcessTemplateUpload!");
    FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");  
    FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
     */

    private ProcesstemplateConverter unmarshallerThenSave(InputStream is, TcProcesstemplate selectedProcesstemplate) throws JAXBException, BPMException, Exception {
        ProcesstemplateConverter processTemplate = null;
        JAXBContext context = JAXBContext.newInstance(ProcesstemplateConverter.class);
        Unmarshaller um = context.createUnmarshaller();
        //InputStream is = this.getClass().getResourceAsStream("process.xml");
        if (is != null) {
            Object obj = um.unmarshal(is);
            if (obj == null) {
                throw new BPMException("obj is null");
            }
            processTemplate = (ProcesstemplateConverter) obj;
            if (processTemplate.getProcessname() == null) {
                throw new BPMException("processTemplate name is null");
            }
            bpmTemplateFacade.saveProcessTemplate(processTemplate, selectedProcesstemplate);
        } else {
            System.out.println("Inputstream is null!");
        }
        return processTemplate;
    }

    public void outputProcessTemplateByTest() {
        try {
            TcProcesstemplate processTemplate = bpmFacade.getProcesstemplateByName("Processname1");
            System.out.println("processTemplate=" + processTemplate);
            JAXBContext context = JAXBContext.newInstance(ProcesstemplateConverter.class);
            if (processTemplate != null) {
                ProcesstemplateConverter pt = wrapperProcesstemplate(processTemplate);
                Marshaller m = context.createMarshaller();
                m.marshal(pt, System.out);
            }
        } catch (Exception ex) {
            Logger.getLogger(ProcessTemplateController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ProcesstemplateConverter wrapperProcesstemplate(TcProcesstemplate processtemplate) {
        ProcesstemplateConverter pt = new ProcesstemplateConverter();
        pt.setProcessname(processtemplate.getProcessname());
        //pt.setProcessversion(processtemplate.getProcessversion());
        if (processtemplate.getTcActivitytemplateCollection() != null) {
            List<ActivitytemplateConverter> list = new ArrayList<ActivitytemplateConverter>();
            List<ActivityroutetemplateConverter> routeList = new ArrayList<ActivityroutetemplateConverter>();
            for (TcActivitytemplate activitytemplate : processtemplate.getTcActivitytemplateCollection()) {
                ActivitytemplateConverter at = new ActivitytemplateConverter();
                at.setActivityname(activitytemplate.getActivityname());
                at.setActivitytype(activitytemplate.getActivitytype());
                if (activitytemplate.getDuration() != null) {
                    at.setDuration(activitytemplate.getDuration().intValue());
                }
                at.setExpression(activitytemplate.getExpression());
                at.setRolename(activitytemplate.getRolename());
                list.add(at);
                if (activitytemplate.getTcActivityroutetemplateCollection() != null) {
                    for (TcActivityroutetemplate routetemplate : activitytemplate.getTcActivityroutetemplateCollection()) {
                        ActivityroutetemplateConverter art = new ActivityroutetemplateConverter();
                        art.setRoutename(routetemplate.getRoutename());
                        art.setFromactivity(routetemplate.getFromactivity().getActivityname());
                        art.setToactivity(routetemplate.getToactivity().getActivityname());
                        routeList.add(art);
                    }
                }
            }
            pt.setActivitytemplate(list);
            pt.setActivityroutetemplate(routeList);
        }
        return pt;
    }

    public List<TcProcesstemplate> getProcessTemplateList() {
        List<TcProcesstemplate> list = bpmTemplateFacade.getLatestProcessTemplates();
        return list;
    }

    public String exportProcessTemplate() {
        OutputStream outputStream = null;
        try {
            TcProcesstemplate processtemplate = bpmTemplateFacade.getLatestProcessTempalteByName(selectedProcesstemplate.getProcessname());
            ProcesstemplateConverter pt = wrapperProcesstemplate(processtemplate);
            JAXBContext context = JAXBContext.newInstance(ProcesstemplateConverter.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            HttpServletResponse response = (HttpServletResponse) (FacesContext.getCurrentInstance().getExternalContext().getResponse());
            response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Disposition", "attachment; filename=" + processtemplate.getProcessname());
            response.setContentType("application/xml");
            outputStream = response.getOutputStream();
            m.marshal(pt, outputStream);
            outputStream.flush();
            outputStream.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(ProcessTemplateController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(ProcessTemplateController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                outputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(ProcessTemplateController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    public String exportProcessTemplateTxt() {
        OutputStream outputStream = null;
        try {
            TcProcesstemplate processtemplate = bpmTemplateFacade.getLatestProcessTempalteByName(selectedProcesstemplate.getProcessname());
            ProcesstemplateConverter pt = wrapperProcesstemplate(processtemplate);
            initGraphicTypeData();
            String graphviz = converterToGraphviz(pt);
            
            HttpServletResponse response = (HttpServletResponse) (FacesContext.getCurrentInstance().getExternalContext().getResponse());
            response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Disposition", "attachment; filename=" + processtemplate.getProcessname() +".txt");
            response.setContentType("text/plain");
            outputStream = response.getOutputStream();
            outputStream.write(graphviz.getBytes());
            outputStream.flush();
            outputStream.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(ProcessTemplateController.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                outputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(ProcessTemplateController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
   private String converterToGraphviz(ProcesstemplateConverter pt){
        StringBuilder sb = new StringBuilder("");
        sb.append("digraph G {").append("\n");
        sb.append("node[fontname=\"DFKai-sb\"];").append("\n");
        for( ActivityroutetemplateConverter route :pt.getActivityroutetemplate()){
            sb.append(converterActivityname(route.getFromactivity()) ).append(" -> ").append( converterActivityname(route.getToactivity())).append(" [label=\"").append(route.getRoutename()).append("\"];").append("\n");
        }
        for( ActivitytemplateConverter a :pt.getActivitytemplate()){
            sb.append( converterActivityname(a.getActivityname())).append(" [shape=").append(getGraphicTypeName(a.getActivitytype().toString())).append(getGraphicLabel(a.getActivityname())).append("];").append("\n");
        }
        sb.append("\n");
        sb.append("}").append("\n");
        return sb.toString();
    }
    private Map<String,String> map = new HashMap<String,String>();
    private Map<String,String> initGraphicTypeData(){
        
        String[][] types ={ 
        {"START", "circle"},
        {"END", "circle"},
        {"TASK", "box"},
        {"AND_GATE", "diamond"},
        {"OR_GATE", "diamond"},
        {"OPTION", "diamond"},
        {"EXPRESSION_ROBOT", "parallelogram"},
        {"SETSTATE_ROBOT", "parallelogram"},
        {"GROUND", "circle"},
        {"CONDITION", "diamond"}
        };
        for( int i =0;i<types.length;i++){
            map.put(types[i][0], types[i][1]);
        }
        return map;
    }

    private String getGraphicTypeName(String type){
       return map.get(type);
    }
    
    private String getGraphicLabel(String activityname){
        String name=" ";
        if( activityname !=null && !activityname.trim().equals("") )
            name = " label=\"" + activityname +"\"";
        return name;
    }
    private String converterActivityname(String activityname){
        String name = "na";
        if( activityname != null){
            name = "\"" + activityname +"\"";
        }
        return name;
    }
}
