
import org.apache.storm.spout.Scheme;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by dujiayong on 16/10/9.
 */
public class MessageScheme implements Scheme {

    public List<Object> deserialize(ByteBuffer arg0) {
        try {
            String msg = new String(arg0.array(), "UTF-8");
            return new Values(msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Fields getOutputFields() {
        return new Fields("msg");
    }

}