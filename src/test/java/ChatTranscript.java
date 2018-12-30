import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * Classes corresponding to proper objects
 * ChatTranscript - main class
 */
@XmlRootElement(name = "chatTranscript")
public class ChatTranscript {
    @XmlAttribute
    public String sessionId;

    @XmlAttribute
    GregorianCalendar startAt;

    @XmlElementRef
    public List<ChatTranscriptItem> elements;

    public GregorianCalendar getStartAt() {
        return startAt;
    }

    @Override
    public String toString() {
        return "ChatTranscript [sessionId=" + sessionId + ", startAt="
            + startAt + ", elements=" + elements + "]";
    }
}

/**
 * First main subclass
 */
abstract class ChatTranscriptItem {
    @XmlAttribute
    public Integer eventId;

    @XmlAttribute
    public String userId;

    @XmlAttribute
    public Integer timeShift;

    @XmlAttribute
    String visibility;
}

/**
 * Subclasses for other fields
 */
@XmlRootElement(name = "newParty")
class NewParty extends ChatTranscriptItem {
    @XmlElement
    UserInfo userInfo;

    @XmlElement
    UserData userData;

    @Override
    public String toString() {
        return "NewParty [userInfo=" + userInfo + ", userData=" + userData
            + ", eventId=" + eventId + ", userId=" + userId
            + ", timeShift=" + timeShift + ", visibility=" + visibility
            + "]";
    }
}

@XmlRootElement(name = "message")
class Message extends ChatTranscriptItem {
    @XmlElement
    MsgText msgText;

    @Override
    public String toString() {
        return "Message [msgText=" + msgText + ", eventId=" + eventId
            + ", userId=" + userId + ", timeShift=" + timeShift
            + ", visibility=" + visibility + "]";
    }
}

@XmlRootElement(name = "notice")
class Notice extends ChatTranscriptItem {
    @XmlElement
    NoticeText noticeText;

    @XmlElement
    UserData userData;

    @Override
    public String toString() {
        return "Notice [noticeText=" + noticeText + ", eventId=" + eventId
            + ", userId=" + userId + ", timeShift=" + timeShift
            + ", visibility=" + visibility + "]";
    }
}

@XmlRootElement(name = "partyLeft")
class PartyLeft extends ChatTranscriptItem {
    @XmlAttribute
    String askerId;

    @Override
    public String toString() {
        return "PartyLeft [askerId=" + askerId + ", eventId=" + eventId
            + ", userId=" + userId + ", timeShift=" + timeShift
            + ", visibility=" + visibility + "]";
    }
}

@XmlRootElement(name = "userInfo")
class UserInfo {
    @XmlAttribute
    String personId;

    @XmlAttribute
    String userNick;

    @XmlAttribute
    String userType;

    @XmlAttribute
    String protocolType;

    @XmlAttribute
    Integer timeZoneOffset;

    @Override
    public String toString() {
        return "UserInfo [personId=" + personId + ", userNick=" + userNick
            + ", userType=" + userType + ", protocolType=" + protocolType
            + ", timeZoneOffset=" + timeZoneOffset + "]";
    }
}

@XmlRootElement(name = "userData")
class UserData {
    @XmlElementRef
    List<UserDataItem> items;

    @Override
    public String toString() {
        return "UserData [items=" + items + "]";
    }
}

@XmlRootElement(name = "item")
class UserDataItem {
    @XmlAttribute
    String key;

    @XmlValue
    String value;

    @Override
    public String toString() {
        return "UserDataItem [key=" + key + ", value=" + value + "]";
    }
}

@XmlRootElement(name = "msgText")
class MsgText {
    @XmlAttribute
    String msgType;

    @XmlAttribute
    String treatAs;

    @XmlValue
    String text;

    @Override
    public String toString() {
        return "MsgText [msgType=" + msgType + ", treatAs=" + treatAs
            + ", text=" + text + "]";
    }
}

@XmlRootElement(name = "noticeText")
class NoticeText {
    @XmlAttribute
    String noticeType;

    @XmlValue
    String text;

    @Override
    public String toString() {
        return "NoticeText [noticeType=" + noticeType + ", text=" + text + "]";
    }
}