package com.nullgarden.postatask;

/**
 * Created by sora_wu on 01/08/2017.
 */

public class MainTicket {

    private String Head, Name, Price, Detail, Time, Date, Title;

    public MainTicket(){

    }

    public MainTicket(String head, String name, String price, String detail, String time, String date, String title) {
        Head = head;
        Name = name;
        Price = price;
        Detail = detail;
        Time = time;
        Date = date;
        Title = title;
    }

    public String getTitle(){
        return Title;
    }

    public void setTitle(String title){
        Title = title;
    }

    public String getHead() {
        return Head;
    }

    public void setHead(String head) {
        Head = head;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
