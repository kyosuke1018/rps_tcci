/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.model.admin;

import com.tcci.fc.entity.org.TcUser;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 線上使用者
 * 
 * @author Peter
 */
public class OnlineUsers {
    private final static Logger logger = LoggerFactory.getLogger(OnlineUsers.class);
    
    public final static int ACTION_GET = 0;
    public final static int ACTION_ADD = 1;
    public final static int ACTION_REMOVE = 2;
    public final static int ACTION_CLEAR = 3;
    public final static int ACTION_VIEW = 9;
    
    private static Map<String, TcUser> sessionUserMap = new LinkedHashMap<String, TcUser>();

    /**
     * 主處理程序
     * @param action
     * @param key
     * @param Content
     * @return 
     */
    public static synchronized List<TcUser> action(int action){
        return action(action, null, null);
    }
    public static synchronized List<TcUser> action(int action, String key){
        return action(action, key, null);
    }
    public static synchronized List<TcUser> action(int action, String key, TcUser user){
        try{
            logger.debug("action = "+action+"; key = "+key);
            switch(action){
                case ACTION_ADD:
                    sessionUserMap.put(key, user);
                    break;
                case ACTION_REMOVE:
                    sessionUserMap.remove(key);
                    break;
                case ACTION_CLEAR:
                    sessionUserMap.clear();
                    break;
                case ACTION_GET:
                    List<TcUser> list = new ArrayList<TcUser>();
                    list.add(sessionUserMap.get(key));
                    return list;
                default:
                    return toList();
            }
        }catch(Exception e){
            logger.error("action exception :\n", e);
        }
        
        return new ArrayList<TcUser>();
    }
    
    /**
     * 轉換成 List
     * @return 
     */
    private static List<TcUser> toList(){
        List<TcUser> resList = new ArrayList<TcUser>();
        for (Map.Entry<String, TcUser> entry : sessionUserMap.entrySet()) {
            resList.add(entry.getValue());
        }
        
        return resList;
    }

}
