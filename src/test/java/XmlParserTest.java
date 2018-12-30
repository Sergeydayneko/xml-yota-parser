import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.GregorianCalendar;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import model.CommonChatItem;
import model.FileChatItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Created by s.dayneko 09.08.2018
 */
public class XmlParserTest {
    private ChatTranscriptXmlParser parser;
    private File fileMessage;
    private File fileNoticeSys;
    private FileReader reader;
    private ChatTranscriptJsonConverter converter;
    private StringWriter strWriter;
    private JsonWriter writer;
    private String fileChatItem;
    private String commonChatItem;


    @Before
    public void initializeFileVariables() throws
        JAXBException,
        TransformerException,
        ParserConfigurationException
    {
        parser            = new ChatTranscriptXmlParser();
        converter         = new ChatTranscriptJsonConverter();
        strWriter         = new StringWriter();
        writer            = new JsonWriter(strWriter);
        fileChatItem      =  "\"fileName\":\"DB_PIC.jpg\"," +
                             "\"fileDocumentId\":\"00039aDP3BQY0064\"," +
                             "\"fileId\":\"00D35B6318F80223\"," +
                             "\"fileSize\":\"191855\"," +
                             "\"fileIsDeleted\":\"true\"," +
                             "\"userNick\":\"Vasya Pupkin\"," +
                             "\"userType\":\"CLIENT\"," +
                             "\"notice\":\"Notice.FileUploaded\"," +
                             "\"date\":\"2018-08-02T14:45:13Z\"";

        commonChatItem    = "\"text\":\"has joined the session\"," +
                            "\"userNick\":\"Vasya Pupkin\"," +
                            "\"userType\":\"CLIENT\"," +
                            "\"notice\":\"Notice.Joined\"," +
                            "\"date\":\"2018-08-02T14:44:10Z\"";



        /**
         * Initialize factories for XML files
         */
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document fileSysDoc = factory.newDocumentBuilder().newDocument();
        Document messageDoc = factory.newDocumentBuilder().newDocument();

        /**
         * Creating fileSys XML file
         */
        Element chatTranscript = fileSysDoc.createElement("chatTranscript");
        chatTranscript.setAttribute("startAt", "2018-08-02T14:44:10Z");
        chatTranscript.setAttribute("sessionId", "00039aDP3BQY005W");
        fileSysDoc.appendChild(chatTranscript);
        Element newParty = fileSysDoc.createElement("newParty");
        newParty.setAttribute("userId", "00D35B6318BA0220");
        newParty.setAttribute("timeShift", "0");
        newParty.setAttribute("visibility", "ALL");
        newParty.setAttribute("eventId", "1");
        chatTranscript.appendChild(newParty);
        Element userInfo = fileSysDoc.createElement("userInfo");
        userInfo.setAttribute("userNick", "Vasya Pupkin");
        userInfo.setAttribute("userType", "CLIENT");
        userInfo.setAttribute("protocolType", "FLEX");
        userInfo.setAttribute("timeZoneOffset", "180");
        userInfo.setAttribute("clientVersion", "106");
        newParty.appendChild(userInfo);
        Element notice = fileSysDoc.createElement("notice");
        notice.setAttribute("userId", "00D35B6318BA0220");
        notice.setAttribute("timeShift", "63");
        notice.setAttribute("visibility", "ALL");
        notice.setAttribute("eventId", "7");
        chatTranscript.appendChild(notice);
        Element userInfoNotice = fileSysDoc.createElement("userInfo");
        userInfoNotice.setAttribute("userNick", "Vasya Pupkin");
        userInfoNotice.setAttribute("userType", "CLIENT");
        userInfoNotice.setAttribute("protocolType", "FLEX");
        userInfoNotice.setAttribute("timeZoneOffset", "180");
        userInfoNotice.setAttribute("clientVersion", "106");
        notice.appendChild(userInfoNotice);
        Element noticeText = fileSysDoc.createElement("noticeText");
        noticeText.setAttribute("noticeType", "SYS_COMMAND");
        notice.appendChild(noticeText);
        Text textNode = fileSysDoc.createTextNode("file-uploaded");
        noticeText.appendChild(textNode);
        Element userData = fileSysDoc.createElement("userData");
        notice.appendChild(userData);
        Element item1 = fileSysDoc.createElement("item");
        item1.setAttribute("key", "file-deleted");
        Text text1 = fileSysDoc.createTextNode("true");
        item1.appendChild(text1);
        Element item2 = fileSysDoc.createElement("item");
        item2.setAttribute("key", "file-document-id");
        Text text2 = fileSysDoc.createTextNode("00039aDP3BQY0064");
        item2.appendChild(text2);
        Element item3 = fileSysDoc.createElement("item");
        item3.setAttribute("key", "file-name");
        Text text3 = fileSysDoc.createTextNode("DB_PIC.jpg");
        item3.appendChild(text3);
        Element item4 = fileSysDoc.createElement("item");
        item4.setAttribute("key", "file-size");
        Text text4 = fileSysDoc.createTextNode("191855");
        item4.appendChild(text4);
        Element item5 = fileSysDoc.createElement("item");
        item5.setAttribute("key", "file-id");
        Text text5 = fileSysDoc.createTextNode("00D35B6318F80223");
        item5.appendChild(text5);
        userData.appendChild(item1);
        userData.appendChild(item2);
        userData.appendChild(item3);
        userData.appendChild(item4);
        userData.appendChild(item5);

        /**
         * Creating fileMessage XML file
         */
        Element chatTranscript1 = messageDoc.createElement("chatTranscript");
        chatTranscript.setAttribute("startAt", "2018-08-02T14:44:10Z");
        chatTranscript.setAttribute("sessionId", "00039aDP3BQY005W");
        messageDoc.appendChild(chatTranscript1);
        Element newParty1 = messageDoc.createElement("newParty");
        newParty1.setAttribute("userId", "00D35B6318BA0220");
        newParty1.setAttribute("timeShift", "0");
        newParty1.setAttribute("visibility", "ALL");
        newParty1.setAttribute("eventId", "1");
        chatTranscript1.appendChild(newParty1);
        Element userInfo1 = messageDoc.createElement("userInfo");
        userInfo1.setAttribute("userNick", "Vasya Pupkin");
        userInfo1.setAttribute("userType", "CLIENT");
        userInfo1.setAttribute("protocolType", "FLEX");
        userInfo1.setAttribute("timeZoneOffset", "180");
        userInfo1.setAttribute("clientVersion", "106");
        newParty1.appendChild(userInfo1);
        Element message = messageDoc.createElement("message");
        message.setAttribute("userId", "00D35B6318BA0220");
        message.setAttribute("timeShift", "5");
        message.setAttribute("visibility", "ALL");
        message.setAttribute("eventId", "2");
        chatTranscript1.appendChild(message);
        Element msgText = messageDoc.createElement("msgText");
        message.appendChild(msgText);
        Text text = messageDoc.createTextNode("any text for testing");
        msgText.appendChild(text);

        fileNoticeSys           = new File("src/main/resources/noticeSys.xml");
        fileMessage             = new File("src/main/resources/messageNormal.xml");
        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(fileSysDoc), new StreamResult(fileNoticeSys));
        transformer.transform(new DOMSource(messageDoc), new StreamResult(fileMessage));
    }

    @Test
    public void testMakeModelFromXmlForMessage() throws FileNotFoundException, JAXBException {
        reader = new FileReader(fileMessage);
        ChatTranscript chatTranscript = parser.parseChatTranscript(reader);

        assert(chatTranscript.elements.get(0) instanceof NewParty);
        assertEquals(chatTranscript.elements.get(0).eventId, new Integer("1"));
        assert(chatTranscript.elements.get(1) instanceof Message);
        assertEquals(chatTranscript.elements.get(1).eventId, new Integer("2"));
    }

    @Test
    public void testMakeModelFromXmlForFileSys() throws JAXBException, FileNotFoundException, ClassCastException {
        reader = new FileReader(fileNoticeSys);
        ChatTranscript chatTranscript = parser.parseChatTranscript(reader);

        assert(chatTranscript.elements.get(1) instanceof Notice);
        Notice notice = (Notice)chatTranscript.elements.get(1);
        assertEquals(notice.noticeText.noticeType, "SYS_COMMAND");
        assertEquals(notice.noticeText.text, "file-uploaded");
    }

    @Test
    public void testWriteTranscriptAsJsonForSystemFile() throws JAXBException, FileNotFoundException {
        reader = new FileReader(fileNoticeSys);
        ChatTranscript chatTranscript = parser.parseChatTranscript(reader);
        converter.writeTranscriptAsJson(chatTranscript, writer);
        System.out.println(strWriter);

        assert(strWriter.toString().indexOf("fileName") > 0);
        assert(strWriter.toString().indexOf("fileDocumentId") > 0);
    }

    @Test
    public void testPrepareChatItemForConversionForSystemFile()throws JAXBException, FileNotFoundException, ClassCastException  {
        reader = new FileReader(fileNoticeSys);
        ChatTranscript chatTranscript = parser.parseChatTranscript(reader);

        converter.prepareChatItemForConversion(chatTranscript.elements.get(0), null);
        assertEquals(converter.prepareChatItemForConversion(chatTranscript.elements.get(1), null).getClass(), FileChatItem.class);
        assert(converter.prepareChatItemForConversion(chatTranscript.elements.get(1), null) instanceof FileChatItem);
        FileChatItem fileChatItem = (FileChatItem)converter.prepareChatItemForConversion(chatTranscript.elements.get(1), null);

        assertEquals(fileChatItem.fileIsDeleted, "true");
        assertEquals(fileChatItem.fileName, "DB_PIC.jpg");
        assertEquals(fileChatItem.fileDocumentId, "00039aDP3BQY0064");
    }

    @Test
    public void testPrepareChatItemForConversionForMessage()throws JAXBException, FileNotFoundException {
        reader = new FileReader(fileMessage);
        ChatTranscript chatTranscript = parser.parseChatTranscript(reader);

        converter.prepareChatItemForConversion(chatTranscript.elements.get(0), null);
        assertEquals(converter.prepareChatItemForConversion(chatTranscript.elements.get(1), null).getClass(), CommonChatItem.class);
        assert(converter.prepareChatItemForConversion(chatTranscript.elements.get(1), null) instanceof CommonChatItem);
        CommonChatItem messageChatItem = (CommonChatItem) converter.prepareChatItemForConversion(chatTranscript.elements.get(1), null);

        assertEquals(messageChatItem.notice, "Message.Text");
        assertEquals(messageChatItem.text, "any text for testing");
    }

    @Test
    public void testWriteTranscriptAsJsonForAnyFileWhenMustBeNullReturn() throws JAXBException, FileNotFoundException {
        reader = new FileReader(fileNoticeSys);
        ChatTranscript chatTranscript = parser.parseChatTranscript(reader);

        assertNull(converter.prepareChatItemForConversion(chatTranscript.elements.get(0), new GregorianCalendar()));
    }

    @Test
    public void testConvertTranscriptForFileChatItem() throws FileNotFoundException, JAXBException {
        reader = new FileReader(fileNoticeSys);
        ChatTranscript chatTranscript = parser.parseChatTranscript(reader);

        String fullJsonString     = converter.convertTranscript(chatTranscript);
        System.out.println(fullJsonString);
        String fileCommonItemJson = fullJsonString.split("},\\{")[0];
        String fileChatItemJson   = fullJsonString.split("},\\{")[1];

        String commonChatItemModel = fileCommonItemJson.substring(2, fileCommonItemJson.length());
        String fileChatItemModel   = fileChatItemJson.substring(0, fileChatItemJson.length() - 2);

        assertEquals(fileChatItem, fileChatItemModel);
        assertEquals(commonChatItem, commonChatItemModel);
    }

    @After
    public void closeReader() throws IOException {
        reader.close();
    }
}
