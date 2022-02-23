package org.expedia.streams;

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
//import org.expedia.util.TaggedCounter;
//import org.expedia.util.TaggedTimer;

import java.util.Properties;

public class PreProcessor {
    private StatsDClient Statsd;
/*
    private TaggedTimer eventLagTimer;
    private TaggedCounter preProcessorExceptionCounter;
    private TaggedCounter eventLagCounter;

    public PreProcessor(MeterRegistry meterRegistry, String metricPrefix) {
        this.eventLagTimer = new TaggedTimer(meterRegistry, metricPrefix + EVENT_LAG_TIMER, DIVISION);
        this.eventLagCounter = new TaggedCounter(meterRegistry, metricPrefix + EVENT_LAG_COUNTER, DIVISION);
        this.preProcessorExceptionCounter = new TaggedCounter(meterRegistry, metricPrefix + PREPROCESSOR_EXCEPTION, DIVISION);
    }

 */

    public void process(StatsDClient statsd){
        this.Statsd = statsd;
        try {
            final Properties props = new Properties();
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, "myApplicationName_11");
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class.getName());
            props.setProperty("schema.registry.url", "http://localhost:8081");
            props.put("key.subject.name.strategy", "io.confluent.kafka.serializers.subject.TopicNameStrategy");
            props.put("value.subject.name.strategy", "io.confluent.kafka.serializers.subject.TopicRecordNameStrategy");

            props.put(ConsumerConfig.GROUP_ID_CONFIG, "customer-consumer-group-v2");
            // props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

//        props.put("auto.commit.enable", "false");
//        props.setProperty("specific.avro.reader", "true");

            StreamsBuilder builder = new StreamsBuilder();

            KStream<String, CustomerV1> stream = builder.stream("customer-avro_1");
            stream.foreach((key, value) -> {

                    Statsd.incrementCounter("example_metric.increment", new String[]{"environment:dev"});
                    Statsd.decrementCounter("example_metric.decrement", new String[]{"environment:dev"});
                    Statsd.count("example_metric.count", 2, new String[]{"environment:dev"});

                System.out.println("================");
                System.out.println(key);
                System.out.println(value);
            });

            KafkaStreams streams = new KafkaStreams(builder.build(), props);
            streams.start();

            // usually the stream application would be running forever,
            // in this example we just let it run for some time and stop since the input data is finite.

            streams.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
