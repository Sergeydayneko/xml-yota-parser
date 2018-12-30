import java.io.Reader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JAXB Chat Transcript Parser
 */
public class ChatTranscriptXmlParser {
    private static final Logger logger = LoggerFactory.getLogger(ChatTranscriptXmlParser.class);

    private final JAXBContext jaxbContext;

    /**
     * Constructor
     * Initialize JAXBContext with proper classes for further transcription
     * Classes are in ChatTranscript
     * @throws JAXBException
     */
    public ChatTranscriptXmlParser() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(ChatTranscript.class,
            NewParty.class, Message.class, Notice.class, PartyLeft.class);

        logger.trace("Created JAXBContext: {}", jaxbContext);
    }

    /**
     * Parse XML to Java object
     * @param xmlReader is FileReader of corresponding XML file
     * @return Java Object representation of received XML file
     * @throws JAXBException
     */
    public ChatTranscript parseChatTranscript(Reader xmlReader) throws JAXBException {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        ChatTranscript transcript = (ChatTranscript) unmarshaller.unmarshal(xmlReader);

        logger.debug("Parsed chat transcript with sessionId: {}", transcript.sessionId);
        logger.debug("{}", transcript);

        return transcript;
    }
}