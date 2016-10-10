import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class SequeceBolt extends BaseBasicBolt {

    public void execute(Tuple input, BasicOutputCollector collector) {
        String word = (String) input.getValue(0);
        String out = "??????????output:" + word;
        System.out.println(out);

        //写文件
        try {
            DataOutputStream out_file = new DataOutputStream(new FileOutputStream("kafkastorm.out"));
            out_file.writeUTF(out);
            out_file.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        collector.emit(new Values(out));
    }

    public void declareOutputFields(OutputFieldsDeclarer arg0) {
        arg0.declare(new Fields("message"));
    }

}