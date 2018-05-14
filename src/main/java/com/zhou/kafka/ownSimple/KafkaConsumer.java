package com.zhou.kafka.ownSimple;

import kafka.consumer.*;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author eli
 * @date 2018/5/10 12:04
 */
public class KafkaConsumer {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.put("zookeeper.connect", "192.168.8.101:3181");
        properties.put("group.id", "bigdata");
        properties.put("auto.offset.reset", "smallest");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("partition.assignment.strategy", "roundrobin");
        ConsumerConfig consumerConfig = new ConsumerConfig(properties);
        //得到一个consumer连接
        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);

        kafka_consume_02(consumerConnector);

    }



    public static void kafka_consume_01(ConsumerConnector consumerConnector ) throws Exception{

        //topic过滤器,白名单
        Whitelist whitelist = new Whitelist("own_kafka");
        List<KafkaStream<byte[],byte[]>> partitions = consumerConnector.createMessageStreamsByFilter(whitelist);
        if(CollectionUtils.isEmpty(partitions)){
            System.out.println("empty...");
            TimeUnit.SECONDS.sleep(1);
        }
        for (KafkaStream kafkaStream : partitions){
            ConsumerIterator<byte[],byte[]> iterator =  kafkaStream.iterator();
            while(iterator.hasNext()){
                MessageAndMetadata<byte[],byte[]> next = iterator.next();
                System.out.println("partiton:" + next.partition());
                System.out.println("offset:" + next.offset());
                System.out.println("message:" + new String(next.message(), "utf-8"));
            }
        }
    }
    public static void kafka_consume_02(ConsumerConnector consumerConnector ) throws Exception {


        String topicstr="gbktopic";
        //定义一个map
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topicstr, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> topicStreamsMap = consumerConnector.createMessageStreams(topicCountMap);
        //取出 `kafkaTest` 对应的 streams
        List<KafkaStream<byte[], byte[]>> streams = topicStreamsMap.get(topicstr);

        System.out.println("streams.size() = " + streams.size());
        for (int i = 0; i < streams.size(); i++) {
            ConsumerIterator<byte[], byte[]> it = streams.get(i).iterator();
            /**
             * 不停地从stream读取新到来的消息，在等待新的消息时，hasNext()会阻塞
             * 如果调用 `ConsumerConnector#shutdown`，那么`hasNext`会返回false
             * */
            while (it.hasNext()) {
                MessageAndMetadata<byte[], byte[]> data = it.next();
                String topic = data.topic();
                int partition = data.partition();
                long offset = data.offset();
                String msg = new String(data.message());
                System.out.println("msg = " + msg + " offset="+offset);
//                System.out.println(String.format(
//                        "Consumer:   Topic: [%s],  PartitionId: [%d], Offset: [%d], msg: [%s]",
//                         topic, partition, offset, msg));
            }
        }
    }

}
