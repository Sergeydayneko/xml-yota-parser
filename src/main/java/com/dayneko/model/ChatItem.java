package com.dayneko.model;

/**
 * Created by s.dayneko 09.08.2018
 */
public class ChatItem {
    String userNick;
    String userType;
    String notice;
    String date;

    ChatItem(String userNick, String userType, String notice, String date) {
        this.userNick = userNick;
        this.userType = userType;
        this.notice   = notice;
        this.date     = date;
    }
}
