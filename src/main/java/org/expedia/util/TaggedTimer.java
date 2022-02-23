package org.expedia.util;

//import io.micrometer.core.instrument.MeterRegistry;
//import io.micrometer.core.instrument.Timer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TaggedTimer {
/*
    private MeterRegistry meterRegistry;
    private String timerName;
    private String tagName;
    private Map<String, Timer> timers = new HashMap<>();

    public TaggedTimer(MeterRegistry meterRegistry, String timerName, String tagName) {
        this.meterRegistry = meterRegistry;
        this.timerName = timerName;
        this.tagName = tagName;
    }

    public void record(String tagValue, long l, TimeUnit timeUnit) {
        Timer timer = timers.get(tagValue);
        if (timer == null) {
            timer = Timer.builder(timerName)
                .tag(tagName, tagValue)
                .publishPercentiles(0.5, 0.75, 0.90, 0.95, 0.999)
                .publishPercentileHistogram()
                .register(meterRegistry);

            timers.put(tagValue, timer);
        }
        timer.record(l, timeUnit);
    }

 */

}
