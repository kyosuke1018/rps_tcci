/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vo;

/**
 *
 * @author Kyle.Cheng
 */
public class UseLog {
    
    private String category;//LOGIN_APP, ORDER_CREATE,ORDER_PAY,ORDER_SHIP
    private String clientInfo;//json string  IMEI
    private float patrolLatitude;//緯度
    private float patrolLongitude;//經度

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public float getPatrolLatitude() {
        return patrolLatitude;
    }

    public void setPatrolLatitude(float patrolLatitude) {
        this.patrolLatitude = patrolLatitude;
    }

    public float getPatrolLongitude() {
        return patrolLongitude;
    }

    public void setPatrolLongitude(float patrolLongitude) {
        this.patrolLongitude = patrolLongitude;
    }
    
}
