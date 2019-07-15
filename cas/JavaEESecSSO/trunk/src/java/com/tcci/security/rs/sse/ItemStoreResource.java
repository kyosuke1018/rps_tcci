/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.tcci.security.rs.sse;

import com.tcci.security.rs.AbstractREST;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;

/**
 * A resource for storing named items.
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
@Path("sse/items")
public class ItemStoreResource extends AbstractREST {
    private static final Logger LOG = Logger.getLogger(ItemStoreResource.class.getName());
    
    private static final Queue<String> ITEMS = new ConcurrentLinkedQueue<String>();
    private static final SseBroadcaster BROADCASTER = new SseBroadcaster();

    /**
     * /resources/sse/items
     * @return 
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String listItems() {
        LOG.info("ItemStoreResource listItems ...");
        return ITEMS.toString();
    }

    /**
     * /resources/sse/items/events
     * run while client open sse connection
     * @return 
     */
    @GET
    @Path("events")
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput itemEvents() {
        LOG.info("ItemStoreResource itemEvents ...");
        
        final EventOutput eventOutput = new EventOutput();
        BROADCASTER.add(eventOutput);// 加入廣播對象
        return eventOutput;
    }

    /**
     * /resources/sse/items/addItem
     * POST 接收資料，並廣播至 SSE Client
     * @param name 
     */
    @POST
    public void addItem(@FormParam("name") String name) {
        LOG.info("ItemStoreResource addItem ...");
        
        ITEMS.add(name);
        // Broadcasting an un-named event with the name of the newly added item in data
        BROADCASTER.broadcast(new OutboundEvent.Builder().data(String.class, name).build());
        // Broadcasting a named "add" event with the current size of the items collection in data
        BROADCASTER.broadcast(new OutboundEvent.Builder().name("size").data(Integer.class, ITEMS.size()).build());
    }
}
