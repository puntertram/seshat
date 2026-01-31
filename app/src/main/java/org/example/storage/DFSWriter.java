package org.example.storage;

import org.example.model.Event;
import org.example.model.EventBucket;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;


@Component
public class DFSWriter {

    // Assume this exists
    // private final DFSClient dfsClient;

    private final ObjectMapper mapper = new ObjectMapper();

    public void write(Event event) {
        // Example:
        // byte[] raw = serialize(event);
        // dfsClient.append("/logs/raw", raw);

        // For now
        System.out.println("Writing event to DFS: " + event);
    }

    public void writeBucket(EventBucket bucket) throws Exception {
        Path path = Path.of("data/buckets/" + bucket.getBucketId() + ".json");
        Files.createDirectories(path.getParent());

        byte[] data = mapper.writeValueAsBytes(bucket.getEvents());
        Files.write(path, data);
    }
}
