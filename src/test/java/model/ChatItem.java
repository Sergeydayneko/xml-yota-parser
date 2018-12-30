package model;


public class ChatItem {
    public String userNick;
    public String userType;
    public String notice;
    public String date;

    public ChatItem(
        String userNick,
        String userType,
        String notice,
        String date
    ) {
        this.userNick = userNick;
        this.userType = userType;
        this.notice   = notice;
        this.date     = date;
    }
}

