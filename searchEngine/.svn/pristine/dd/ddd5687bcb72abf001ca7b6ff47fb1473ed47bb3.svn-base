/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.solr.server.queue;

import com.tcci.solr.client.model.TcSolrSource;
import com.tcci.solr.server.conf.ConfigManager;
import java.util.Enumeration;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Glassfish Open Message Queue
 * 
 * @author Peter.pan
 */
public class OpenMessageQueue implements QueueContainer<TcSolrSource> {
    private final static Logger logger = LoggerFactory.getLogger(OpenMessageQueue.class);
    
    private QueueConnectionFactory connectionFactory;
    private Queue queue;
    private QueueConnection myConn = null;
    private Session mySession = null;
    
    /**
     * Construtor
     */
    public OpenMessageQueue(){
        initQueue(ConfigManager.QUEUE_NAME);
    }
    public OpenMessageQueue(String queueName){
        initQueue(queueName);
    }
    
    /**
     * 初始設定
     */
    private void initQueue(String queueName){
        logger.info("initQueue ...");
        try{
            InitialContext ctx = new InitialContext();
            connectionFactory = (QueueConnectionFactory)ctx.lookup(ConfigManager.QUEUE_FACTORY);
            queue = (Queue)ctx.lookup(queueName);
            
            logger.info("connectionFactory ="+connectionFactory);
            logger.info("queue = "+queue);
        }catch(NamingException e){
            logger.error("checkQueueStatus exception :\n", e);
        }
    }

    /**
     * 取出 entry
     * @return 
     */
    @Override
    public TcSolrSource dequque(String queueName) {
        if( !checkQueueName(queueName) ){
            return null;
        }
        
        try {
            // get connect & session
            beforeSession();

            if( mySession!=null ){
                //Create a message consumer.
                MessageConsumer myMsgConsumer = mySession.createConsumer(queue);

                //Start the Connection created in step 3.
                myConn.start();

                //Receive a message from the queue.
                Message msg = myMsgConsumer.receive();

                //Retreive the contents of the message.
                return convertMessage(msg);
            }
        } catch (JMSException ex) {
            logger.error("dequque JMSException : \n", ex);
        } finally {
            afterSession();
        }
        
        return null;
    }

    /**
     * 新增 entry
     * @param entry
     * @return 
     */
    @Override
    public boolean enqueue(String queueName, TcSolrSource entry) {
        if( !checkQueueName(queueName) ){
            return false;
        }
        
        try {
            logger.info("enqueue before beforeSession...");
            // get connect & session
            beforeSession();
            logger.info("enqueue after beforeSession...mySession = "+mySession);
            
            if( mySession!=null ){
                logger.info("enqueue createProducer...");
                //Create a message producer.
                MessageProducer myMsgProducer = mySession.createProducer(queue);

                //Create a message to the queue.
                ObjectMessage myObjMsg = mySession.createObjectMessage(entry);

                //Send a message to the queue.
                myMsgProducer.send(myObjMsg);

                return true;
            }
        } catch (JMSException ex) {
            logger.error("enqueue JMSException : \n", ex);
        } finally {
            afterSession();
        }
        
        return false;
    }

    @Override
    public int getSize(String queueName) {
        if( !checkQueueName(queueName) ){
            return 0;
        }
        try {
            // get connect & session
            beforeSession();
            
            if( mySession!=null ){
                QueueBrowser qbrowser = mySession.createBrowser(queue);

                Enumeration enu = qbrowser.getEnumeration();
                int count = 0;
                while (enu.hasMoreElements()) {
                    enu.nextElement();
                    count++;
                }

                return count;
            }
        } catch (JMSException ex) {
            logger.error("getSize JMSException : \n", ex);
        } finally {
            afterSession();
        }
        
        return 0;
    }
    
    @Override
    public int restore(String queueName, TcSolrSource tcSolrSource) {
        if( !checkQueueName(queueName) ){
            return 0;
        }
        
        try {
            int count=0;
            if( enqueue(queueName, tcSolrSource) ){
                count++;
            }
            
            return count;
        } catch (Exception ex) {
            logger.error("restore Exception :\n", ex);
        }
        
        return 0;
    }
    
    /**
     * beforeSession : 對 Queue 作業前置動作
     * @param myConn
     * @param mySession
     * @throws JMSException 
     */
    private void beforeSession() throws JMSException{       
        // get Connection from pool
        myConn = connectionFactory.createQueueConnection();
        //Create a session within the connection.
        mySession = myConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }
    
    /**
     * afterSession : 對 Queue 作業後置動作
     * @param myConn
     * @param mySession 
     */
    private void afterSession(){
        try{
            // Close Session
            if( mySession !=null ){ 
                // mySession.commit(); // for transacted Session
                mySession.close(); 
            }
            // Close Connection
            if( myConn !=null ){ myConn.close(); }
        }catch(JMSException e){
            logger.error("afterConnect Closing Exception : \n", e);
        }
    }

    /**
     * 轉換 MQ message to TcSolrSource
     * @param msg
     * @return
     * @throws JMSException 
     */
    public TcSolrSource convertMessage(Message msg) throws JMSException{
        if (msg !=null && msg instanceof ObjectMessage) {
            ObjectMessage obj = (ObjectMessage)msg;
            TcSolrSource tcSolrSource = (TcSolrSource) obj.getObject();
            logger.debug("dequque get tcSolrSource = " + tcSolrSource.toString());

            return tcSolrSource;
        }
        
        return null;
    }

    /**
     * 檢查Queue Name
     * @return 
     */
    private boolean checkQueueName(String queueName){
        return ConfigManager.QUEUE_NAME.equals(queueName);
    }
}
