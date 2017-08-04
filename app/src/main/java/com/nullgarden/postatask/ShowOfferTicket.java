package com.nullgarden.postatask;

/**
 * Created by sora_wu on 02/08/2017.
 */

public class ShowOfferTicket {

    String Content, Email, UID, Time, Name, Head;

    public ShowOfferTicket(){

    }

    public ShowOfferTicket(String content, String email, String UID, String time, String name, String head) {
        Content = content;
        Email = email;
        this.UID = UID;
        Time = time;
        Name = name;
        Head = head;
    }

    public String getName(){return Name;}

    public void setName(String name){Name = name;}

    public String getHead(){return Head;}

    public void setHead(String head){Head = head;}

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
