package com.dayneko;

import com.dayneko.model.ChatItem;
import com.dayneko.model.CommonChatItem;
import com.dayneko.model.FileChatItem;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Calendar;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class contains methods for converting file from POJO to JSON
 * with additional information depending on content
 * Created by s.dayneko 08.08.2018
 */
public class ChatTranscriptJsonConverter {
    private static final Logger logger = LoggerFactory.getLogger(ChatTranscriptJsonConverter.class);

    /**
     * Manually configurable variables
     */
    private static final String NOTICE_JOINED   = "Notice.Joined";
    private static final String NOTICE_LEFT     = "Notice.Left";
    private static final String MESSAGE_TEXT    = "Message.Text";
    private static final String NOTICE_PUSH_URL = "Notice.PushUrl";
    private static final String NOTICE_UPLOADED = "Notice.FileUploaded";

    private static final String USER_PUSHED_URL = "USER_PUSHED_URL";
    private static final String SYS_COMMAND     = "SYS_COMMAND";

    private static final String VISIBILITY_ALL = "ALL";

    private HashMap<String, NickTypePair> userIdNickMap = new HashMap<>();
    private Gson gson                                   = new Gson();

    private GregorianCalendar chatStartedAt;

    /**
     * Converts POJO to json string
     * @param transcript XML file represented by POJO
     */
    public String convertTranscript(ChatTranscript transcript) {
        logger.debug("Converting chat transcript with sessionId={}", transcript.sessionId);

        chatStartedAt                         = transcript.startAt;
        List<ChatItem> preparedChatTranscript = new ArrayList<>();

        for (ChatTranscriptItem item : transcript.elements) {
            ChatItem preparedItem = prepareChatItemForConversion(item);
            if (preparedItem != null) {
                preparedChatTranscript.add(preparedItem);
            }
        }

        String result = gson.toJson(preparedChatTranscript);

        logger.debug("Chat transcript converted: {}", result);

        return result;
    }

    /**
     * Converts POJO to json
     * @param transcript XML file represented by POJO
     * @param jsonWriter JsonWriter object of json library
     */
    public void writeTranscriptAsJson(ChatTranscript transcript, JsonWriter jsonWriter) {
        chatStartedAt = transcript.startAt;

        for (ChatTranscriptItem item : transcript.elements) {
            ChatItem preparedItem = prepareChatItemForConversion(item);
            if (preparedItem != null) {
                if (preparedItem instanceof CommonChatItem) {
                    gson.toJson(preparedItem, CommonChatItem.class, jsonWriter);
                } else if (preparedItem instanceof FileChatItem) {
                    gson.toJson(preparedItem, FileChatItem.class, jsonWriter);
                }
            }
        }
    }

    /**
     * Write transcript to json (only part of the transcript after fromDateCal)
     * @param transcript XML file represented by POJO
     * @param jsonWriter JsonWriter object of json library
     * @param fromDateCal date of checking(not necessary)
     */
    public void writeTranscriptAsJson(
        ChatTranscript transcript,
        JsonWriter jsonWriter,
        Calendar fromDateCal
    ) {
        chatStartedAt = transcript.startAt;

        for (ChatTranscriptItem item : transcript.elements) {
            ChatItem preparedItem = prepareChatItemForConversion(item,
                fromDateCal);
            if (preparedItem != null) {
                if (preparedItem instanceof CommonChatItem) {
                    gson.toJson(preparedItem, CommonChatItem.class, jsonWriter);
                } else if (preparedItem instanceof FileChatItem) {
                    gson.toJson(preparedItem, FileChatItem.class, jsonWriter);
                }
            }
        }
    }

    /**
     * Prepare single chat item for conversion without any date checks
     * @param item XML file represented by POJO
     * @return String array of content
     */
    private ChatItem prepareChatItemForConversion(ChatTranscriptItem item) {
        return prepareChatItemForConversion(item, null);
    }

    /**
     * Prepare single chat item for conversion
     * @param item XML file represented by POJO
     * @param fromDateCal date of checking(not necessary)
     * @return String array of corresponding content
     */
    private ChatItem prepareChatItemForConversion(ChatTranscriptItem item, Calendar fromDateCal) {
        if (
            fromDateCal != null &&
            timeShiftToDate(item.timeShift).before(fromDateCal) &&
            VISIBILITY_ALL.equals(item.visibility)
            ) { return null; }

        if (item instanceof NewParty) {
            NewParty newParty = (NewParty) item;
            String userNick   = newParty.userInfo.userNick;
            String userType   = newParty.userInfo.userType;

            userIdNickMap.put(
                newParty.userId,
                new NickTypePair(userNick, userType)
            );

            return new CommonChatItem(
                userNick,
                userType,
                NOTICE_JOINED,
                timeShiftToDateString(newParty.timeShift),
                "has joined the session"
            );

        } else if (item instanceof Message) {
            Message message           = (Message) item;
            NickTypePair nickTypePair = userIdNickMap.get(message.userId);

            return new CommonChatItem(
                nickTypePair.userNick,
                nickTypePair.userType,
                MESSAGE_TEXT,
                timeShiftToDateString(message.timeShift),
                message.msgText.text
            );

        } else if (item instanceof Notice) {
            Notice notice = (Notice) item;

            if (USER_PUSHED_URL.equals(notice.noticeText.noticeType)) {
                NickTypePair nickTypePair = userIdNickMap.get(notice.userId);

                return new CommonChatItem(
                    nickTypePair.userNick,
                    nickTypePair.userType,
                    NOTICE_PUSH_URL,
                    timeShiftToDateString(notice.timeShift),
                    notice.noticeText.text
                );
            } else if(SYS_COMMAND.equals(notice.noticeText.noticeType)) {
                NickTypePair nickTypePair = userIdNickMap.get(notice.userId);
                Map<String, String> fileItems = getFileChatData(notice.userData.items);

                return new FileChatItem(
                    nickTypePair.userNick,
                    nickTypePair.userType,
                    NOTICE_UPLOADED,
                    timeShiftToDateString(notice.timeShift),
                    fileItems.get("file-name"),
                    fileItems.get("file-document-id"),
                    fileItems.get("file-id"),
                    fileItems.get("file-size"),
                    fileItems.get("file-deleted")
                );
            }

        } else if (item instanceof PartyLeft) {
            PartyLeft partyLeft       = (PartyLeft) item;
            NickTypePair nickTypePair = userIdNickMap.get(partyLeft.userId);

            return new CommonChatItem(
                nickTypePair.userNick,
                nickTypePair.userType,
                NOTICE_LEFT,
                timeShiftToDateString(partyLeft.timeShift),
                "has left the session"
                );
        }
        return null;
    }

    /**
     * Service function for converting timeShift into absolute time
     * @param timeShift given in object time shift
     * @return representation in Calendar object
     */
    private Calendar timeShiftToDate(Integer timeShift) {
        GregorianCalendar preciseDate = new GregorianCalendar();
        preciseDate.setTimeInMillis(chatStartedAt.getTimeInMillis());
        preciseDate.add(Calendar.SECOND, timeShift);

        return preciseDate;
    }

    /**
     * Service function for converting timeShift into absolute time
     * @param timeShift given in object time shift
     * @return representation in String
     */
    private String timeShiftToDateString(Integer timeShift) {
        GregorianCalendar preciseDate = new GregorianCalendar();
        preciseDate.setTimeInMillis(chatStartedAt.getTimeInMillis());
        preciseDate.add(Calendar.SECOND, timeShift);

        TimeZone tz   = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);

        return df.format(preciseDate.getTime());
    }

    /**
     * Service class
     */
    class NickTypePair {
        private final String userNick;
        private final String userType;

        NickTypePair(String userNick, String userType) {
            this.userNick = userNick;
            this.userType = userType;
        }

        @Override
        public String toString() {
            return "NickTypePair [userNick=" + userNick +
                   ", userType=" + userType + "]";
        }
    }

    /**
     * UserDataItem Map representation
     * @param items UserDataItem
     * @return Map representation
     */
    private Map<String, String> getFileChatData(List<UserDataItem> items) {
        return new HashMap<String, String>(){
            {
                for (UserDataItem item : items ) put(item.key, item.value);
            }
        };
    }
}
