package org.example.model;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class EventBucket {

    private final String bucketId;
    private final long startTime;
    private long endTime;

    private final List<Event> events = new ArrayList<>();
    private long approxSizeBytes = 0;

    public EventBucket(String bucketId) {
        this.bucketId = bucketId;
        this.startTime = Instant.now().getEpochSecond();
    }

    public void addEvent(Event event) {
        events.add(event);
        endTime = event.getTimestamp();

        // crude size estimate (good enough to start)
        approxSizeBytes += estimateSize(event);
    }

    public boolean shouldRoll(long maxSizeBytes) {
        return approxSizeBytes >= maxSizeBytes;
    }

    public List<Event> getEvents() {
        return events;
    }

    public String getBucketId() {
        return bucketId;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    private long estimateSize(Event event) {
        // Approximation — refine later with serialization size
        return event.toString().getBytes().length;
    }
}