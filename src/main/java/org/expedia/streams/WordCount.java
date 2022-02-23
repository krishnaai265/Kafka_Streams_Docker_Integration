package org.expedia.streams;

import com.timgroup.statsd.NonBlockingStatsDClientBuilder;
import com.timgroup.statsd.StatsDClient;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
//import io.micrometer.core.instrument.MeterRegistry;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.example.CustomerV1;
import org.expedia.util.TaggedCounter;

import java.util.Properties;

public class WordCount {

 //   private TaggedCounter eventLagCounter;
 //   private MeterRegistry meterRegistry;

    public static void main(String []args) throws Exception {

        StatsDClient statsd = new NonBlockingStatsDClientBuilder()
                .prefix("statsd")
                .hostname("localhost")
                .port(8125)
                .build();


        try {
            final Properties props = new Properties();
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, "myApplicationName_3");
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class.getName());
            props.setProperty("schema.registry.url", "http://localhost:8081");
            props.put("key.subject.name.strategy", "io.confluent.kafka.serializers.subject.TopicNameStrategy");
            props.put("value.subject.name.strategy", "io.confluent.kafka.serializers.subject.TopicRecordNameStrategy");

            props.put(ConsumerConfig.GROUP_ID_CONFIG, "customer-consumer-group-v2");

            StreamsBuilder builder = new StreamsBuilder();

            KStream<String, CustomerV1> stream = builder.stream("customer-avro_1");
            stream.foreach((key, value) -> {
                statsd.incrementCounter("example_metric.increment", new String[]{"environment:dev"});
                statsd.count("example_metric.count", 2, new String[]{"environment:dev"});
                System.out.println("================");
                System.out.println(key);
                System.out.println(value);
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            KafkaStreams streams = new KafkaStreams(builder.build(), props);
            streams.start();
            Thread.sleep(5000L);
            streams.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
