import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.kafka.bolt.KafkaBolt;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

import java.util.HashMap;
import java.util.Map;


public class StormKafkaTopo {
    public static void main(String[] args) {
        BrokerHosts brokerHosts = new ZkHosts("localhost:2181/kafka");

        SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, "topic231", "/kafka", "kafkaSpout");

        Config conf = new Config();
        Map<String, String> map = new HashMap<String, String>();
        map.put("metadata.broker.list", "localhost:9092");
        map.put("serializer.class", "kafka.serializer.StringEncoder");

        conf.put("kafka.broker.properties", map);
        conf.put("topic", "topic2");

        spoutConfig.scheme = new SchemeAsMultiScheme(new MessageScheme());

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafkaSpout", new KafkaSpout(spoutConfig));
        builder.setBolt("sequeceBolt", new SequeceBolt()).shuffleGrouping("kafkaSpout");
        builder.setBolt("kafkaBolt", new KafkaBolt<String,Object>()).shuffleGrouping("sequeceBolt");

        if(args != null && args.length > 0) {
            //提交到集群运行
            try {
                StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //本地模式运行
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("Topotest1121", conf, builder.createTopology());
            Utils.sleep(1000);
            cluster.killTopology("Topotest1121");
            cluster.shutdown();
        }
    }
}