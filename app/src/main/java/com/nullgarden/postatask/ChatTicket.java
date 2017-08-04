package com.nullgarden.postatask;

/**
 * Created by sora_wu on 04/08/2017.
 */

public class ChatTicket {

    String Name, Time, Msg, Head;

    public ChatTicket(){

    }

    public ChatTicket(String name, String time, String msg, String head) {
        Name = name;
        Time = time;
        Msg = msg;
        Head = head;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getHead() {
        return Head;
    }

    public void setHead(String head) {
        Head = head;
    }
}
