package org.example.model;

import java.time.Instant;
import java.util.Map;

public class Event {
    private long timestamp;
    private String source;
    private String host;
    private Map<String, Object> fields;

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp.getEpochSecond(); }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public Map<String, Object> getFields() { return fields; }
    public void setFields(Map<String, Object> fields) { this.fields = fields; }
}
