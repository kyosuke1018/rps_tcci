/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

import javax.faces.application.Resource;
import javax.faces.application.ResourceWrapper;
import javax.faces.context.FacesContext;

/**
 *
 * @author Jimmy.Lee
 */
public class CustomResource extends ResourceWrapper {
    // get revision from web.xml RESOURCE_VERSION
    private static String revision = null;

    static {
        FacesContext fc = FacesContext.getCurrentInstance();
        revision = fc.getExternalContext().getInitParameter("RESOURCE_VERSION");
    }

    private Resource resource;

    public CustomResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public Resource getWrapped() {
        return this.resource;
    }

    @Override
    public String getRequestPath() {
        String requestPath = resource.getRequestPath();
        if (requestPath.contains("?")) {
            requestPath = requestPath + "&rv=" + revision;
        } else {
            requestPath = requestPath + "?rv=" + revision;
        }

        return requestPath;
    }

    @Override
    public String getContentType() {
        return getWrapped().getContentType();
    }
}
