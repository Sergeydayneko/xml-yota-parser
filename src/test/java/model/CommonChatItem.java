package model;

public class CommonChatItem extends ChatItem{
    public String text;

    public CommonChatItem(
        String userNick,
        String userType,
        String notice,
        String date,
        String text
    ) {
        super(userNick, userType, notice, date);
        this.text     = text;
    }

    @Override
    public String toString() {
        return "CommonChatItem [notice=" + notice +
            ", user nickname=" + userNick +
            ", text/additional notice=" + text +
            ", date=" + date +
            ", user type=" + userType + "]";
    }
}