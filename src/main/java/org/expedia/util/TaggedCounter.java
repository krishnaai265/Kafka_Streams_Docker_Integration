package org.expedia.util;

//import io.micrometer.core.instrument.Counter;
//import io.micrometer.core.instrument.MeterRegistry;

import java.util.HashMap;
import java.util.Map;

public class TaggedCounter {
/*
    private MeterRegistry meterRegistry;
    private String counterName;
    private String tagName;
    private Map<String, Counter> counters = new HashMap<>();

    public TaggedCounter(MeterRegistry meterRegistry, String counterName, String tagName) {
        this.meterRegistry = meterRegistry;
        this.counterName = counterName;
        this.tagName = tagName;
    }

    public void increment(String tagValue) {
        Counter counter = counters.get(tagValue);
        if (counter == null) {
            counter = Counter.builder(counterName)
                .tag(tagName, tagValue)
                .register(meterRegistry);

            counters.put(tagValue, counter);
        }
        counter.increment();
    }

 */
}
