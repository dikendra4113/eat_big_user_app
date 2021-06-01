package com.example.waiterlessfood.model;

public class cartModel {

    private String catagary;
    private String description;
    private String date;
    private String pname;
    private String price;
    private String quantity;
    private String time;
    private String table;
    public String pid;
    public String seatNo;
    public String user_contact;
    private static String total;



    public cartModel(){

    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getPid() {
        return pid;
    }

    public static String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public cartModel(String time) {
        this.time = time;
    }

//    public cartModel(String catagary, String description, String date, String pname, String price, String quantity) {
//        this.catagary = catagary;
//        this.description = description;
//        this.date = date;
//        this.pname = pname;
//        this.price = price;
//        this.quantity = quantity;
//    }

    public cartModel(String catagary, String description, String date, String pname, String price, String quantity, String time, String table, String pid, String seatNo, String user_contact) {
        this.catagary = catagary;
        this.description = description;
        this.date = date;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.time = time;
        this.table = table;
        this.pid = pid;
        this.seatNo = seatNo;
        this.user_contact = user_contact;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getUser_contact() {
        return user_contact;
    }

    public void setUser_contact(String user_contact) {
        this.user_contact = user_contact;
    }

    public String getCatagary() {
        return catagary;
    }

    public void setCatagary(String catagary) {
        this.catagary = catagary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
