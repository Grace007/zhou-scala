package com.zhou.kafka.ownSimple;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * @author eli
 * @date 2018/5/10 12:04
 */
public class KafkaProducer {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("metadata.broker.list","192.168.8.101:9092");
        properties.put("request.required.acks", "1");
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        ProducerConfig producerConfig = new ProducerConfig(properties);
        //必须是kafka.javaapi.producer.Producer
        Producer<String,String> producer = new Producer(producerConfig);
        String topic = "own_kafka";
        kafka_produce_01(producer,topic);
    }
    public static void kafka_produce_01(Producer producer ,String topic){
        KeyedMessage<String,String> keyedMessage = new KeyedMessage<String, String>(topic,"test-message");
        producer.send(keyedMessage);
        producer.close();

    }


}
