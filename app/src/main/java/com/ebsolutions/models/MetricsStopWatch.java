package com.ebsolutions.models;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetricsStopWatch {
    /**
     * Stores the start time (in milliseconds) when an object of the StopWatch class is initialized.
     */
    private long startTime;

    /**
     * Custom constructor which initializes the {@link #startTime} parameter.
     */
    public MetricsStopWatch(boolean startWatch) {
        if (startWatch) {
            startTime = System.currentTimeMillis();
        }
    }

    public MetricsStopWatch() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Resets the {@link #startTime} parameter back to the current system time in milliseconds.
     */
    public void resetStopWatchClock() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Logs the elapsed time since the time the object of StopWatch was initialized.
     *
     * @param callingCodeBlock
     */
    public void logElapsedTime(String callingCodeBlock) {
        log.info("ELAPSED TIME: {} :: {}", System.currentTimeMillis() - startTime, callingCodeBlock);
    }
}
