package com.duongnd.sipdrinkadmin.model;

public class Notification {
    private String idNotification,notificationTitle, orderId,date;

    public Notification(String idNotification, String notificationTitle, String orderId, String date) {
        this.idNotification = idNotification;
        this.notificationTitle = notificationTitle;
        this.orderId = orderId;
        this.date = date;
    }

    public Notification() {
    }

    public String getIdNotification() {
        return idNotification;
    }

    public Notification setIdNotification(String idNotification) {
        this.idNotification = idNotification;
        return this;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public Notification setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public Notification setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Notification setDate(String date) {
        this.date = date;
        return this;
    }
}
