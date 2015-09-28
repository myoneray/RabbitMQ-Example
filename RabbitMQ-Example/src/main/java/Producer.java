import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;

public class Producer extends EndPoint {

    public Producer(String endPointName) throws IOException {
        super(endPointName);
    }

    public void sendMessage(Serializable object) throws IOException {
        channel.basicPublish("", endPointName, null, SerializationUtils.serialize(object));
    }
}