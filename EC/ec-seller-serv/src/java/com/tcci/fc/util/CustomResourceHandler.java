/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;

/**
 *
 * @author Jimmy.Lee
 */
public class CustomResourceHandler extends ResourceHandlerWrapper {

    private ResourceHandler wrapped;

    public CustomResourceHandler(ResourceHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ResourceHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public Resource createResource(String resourceName, String libraryName) {
        Resource resource = super.createResource(resourceName, libraryName);
        // here a check of library name could be necessary, etc.
        if (libraryName != null && 
                (libraryName.equalsIgnoreCase("css") || libraryName.equalsIgnoreCase("js")))  {
            return new CustomResource(resource);
        } else {
            return resource;
        }

    }
}
