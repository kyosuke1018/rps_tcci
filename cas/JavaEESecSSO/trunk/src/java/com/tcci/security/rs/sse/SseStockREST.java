package com.tcci.security.rs.sse;

import com.tcci.security.rs.AbstractREST;
import com.tcci.security.test.ResVO;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import org.apache.log4j.Logger;

/**
 *
 * @author peter.pan
 */
@ApplicationScoped
@Path("sse/stock")
public class SseStockREST extends AbstractREST {
    private static final Logger LOG = Logger.getLogger(SseStockREST.class.getName());

    @Inject
    private StockFacade stockFacade;

    private Sse sse;
    private SseBroadcaster sseBroadcaster;
    private OutboundSseEvent.Builder eventBuilder;
    private boolean startBroadcast = false;

    @Context
    public void setSse(Sse sse) {
        this.sse = sse;
        this.eventBuilder = sse.newEventBuilder();
        this.sseBroadcaster = sse.newBroadcaster();
    }
    
    /**
     * /resources/sse/stock/prices
     * 提供 Client 獨立內容
     * 
     * @param sseEventSink
     * @param lastReceivedId 
     */
    @GET
    @Path("prices")
    @Produces(MediaType.SERVER_SENT_EVENTS) // "text/event-stream"
    public void getStockPrices(@Context SseEventSink sseEventSink,
                             @Context HttpServletRequest request,
                             @HeaderParam(HttpHeaders.LAST_EVENT_ID_HEADER) @DefaultValue("-1") int lastReceivedId) {
        // 確認 Session 機制仍可運作
        ResVO resVO = checkSession(request);
        LOG.debug("getStockPrices resVO.getLoginAccount()="+resVO.getLoginAccount());
        LOG.debug("getStockPrices resVO.getCaller()="+resVO.getCaller());
        
        int lastEventId = 1;
        if (lastReceivedId != -1) {
            lastEventId = ++lastReceivedId;
        }
        
        boolean running = true;
        while (running) {
            Stock stock = stockFacade.getNextTransaction(lastEventId);
            if (stock != null) {
                OutboundSseEvent sseEvent = genStockOutboundSseEvent(lastEventId, stock);
                sseEventSink.send(sseEvent);// PUSH
                lastEventId++;
            }
            //Simulate connection close
            if (lastEventId % 5 == 0) {
                sseEventSink.close();
                LOG.debug("getStockPrices simulate connection close ...");
                break;
            }
            try {
                //Wait 5 seconds
                Thread.currentThread().sleep(5 * 1000);
            } catch (InterruptedException ex) {
                LOG.debug("getStockPrices InterruptedException ex = "+ex.getMessage());
            }
            //Simulatae a while boucle break
            running = lastEventId <= 2000;
        }
        sseEventSink.close();
    }

    /**
     * 訂閱(註冊)
     * /resources/sse/stock/subscribe
     * @param sseEventSink 
     */
    @GET
    @Path("subscribe")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void listen(@Context HttpServletRequest request,
            @Context SseEventSink sseEventSink) {
        // 確認 Session 機制仍可運作
        ResVO resVO = checkSession(request);
        LOG.debug("listen resVO.getLoginAccount()="+resVO.getLoginAccount());
        LOG.debug("listen resVO.getCaller()="+resVO.getCaller());

        sseEventSink.send(sse.newEvent("Welcome !"));
        this.sseBroadcaster.register(sseEventSink);
        sseEventSink.send(sse.newEvent("You are registred !"));
    }

    /**
     * 啟動 broadcast
     * /resources/sse/stock/publish
     */
    @GET
    @Path("publish")
    public void broadcast(@Context HttpServletRequest request) {
        // 確認 Session 機制仍可運作
        ResVO resVO = checkSession(request);
        LOG.debug("broadcast resVO.getLoginAccount()="+resVO.getLoginAccount());
        LOG.debug("broadcast resVO.getCaller()="+resVO.getCaller());

        if( startBroadcast ){
            LOG.info("Broadcast already start...");
            return;
        }else{
            startBroadcast = true;
            LOG.info("Start Broadcast ...");
        }
        int lastEventId = 1;
        
        boolean running = true;
        while (running) {
            Stock stock = stockFacade.getNextTransaction(lastEventId);
            if (stock != null) {
                OutboundSseEvent sseEvent = genStockOutboundSseEvent(lastEventId, stock);
                sseBroadcaster.broadcast(sseEvent);// PUSH
                lastEventId++;
            }

            try {
                //Wait 5 seconds
                Thread.currentThread().sleep(5 * 1000);
            } catch (InterruptedException ex) {
                LOG.debug("getStockPrices InterruptedException ex = "+ex.getMessage());
            }
            //Simulatae a while boucle break
            running = lastEventId <= 2000;
        }
    }
    
    // 模擬報價
    private OutboundSseEvent genStockOutboundSseEvent(int lastEventId, Stock stock){
        return eventBuilder
                .name("stock")
                .id(String.valueOf(lastEventId))
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(Stock.class, stock)
                .reconnectDelay(3000)
                .comment("price change")
                .build();
    }
}
