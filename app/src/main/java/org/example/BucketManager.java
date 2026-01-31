package org.example;
import org.example.model.Event;
import org.example.model.EventBucket;
import org.example.storage.DFSWriter;
import org.example.indexing.LuceneBucketIndexer;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BucketManager {

    private static final long MAX_BUCKET_SIZE_BYTES = 10 * 1024; // 10 KB

    private EventBucket activeBucket;
    private final DFSWriter dfsWriter;
    private final LuceneBucketIndexer indexer;

    public BucketManager(DFSWriter dfsWriter, LuceneBucketIndexer indexer) {
        this.dfsWriter = dfsWriter;
        this.indexer = indexer;
        this.activeBucket = new EventBucket(newBucketId());
    }

    public synchronized void ingest(Event event) throws Exception {
        activeBucket.addEvent(event);

        if (activeBucket.shouldRoll(MAX_BUCKET_SIZE_BYTES)) {
            rollBucket();
        }
    }

    private void rollBucket() throws Exception {
        EventBucket bucketToRoll = activeBucket;

        // 1. Persist bucket to DFS
        dfsWriter.writeBucket(bucketToRoll);

        // 2. Index bucket as ONE Lucene document
        indexer.indexBucket(bucketToRoll);

        // 3. Create new active bucket
        activeBucket = new EventBucket(newBucketId());
    }

    private String newBucketId() {
        return "bucket-" + UUID.randomUUID();
    }
}
