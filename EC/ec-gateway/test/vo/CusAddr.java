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
public class CusAddr {
    private Long id;
    private String alias;//別名
    private String address;
    private String phone;
    private boolean primary;
    private String carNo;//車號 多筆以分號";"隔開
    private float patrolLatitude;//緯度
    private float patrolLongitude;//經度
    private String delivery;//送達地點 EC_SALESAREA.CODE.CODE

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
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

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }
}
