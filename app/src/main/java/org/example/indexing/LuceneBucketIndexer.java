package org.example.indexing;

import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.example.model.Event;
import org.example.model.EventBucket;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class LuceneBucketIndexer {

    private final IndexWriter writer;

    public LuceneBucketIndexer() throws Exception {
        Directory dir = new ByteBuffersDirectory();
        IndexWriterConfig cfg = new IndexWriterConfig(new StandardAnalyzer());
        cfg.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        this.writer = new IndexWriter(dir, cfg);
    }

    public void indexBucket(EventBucket bucket) throws Exception {
        Document doc = new Document();

        doc.add(new StringField("bucket_id", bucket.getBucketId(), Field.Store.YES));

        doc.add(new LongPoint("start_time", bucket.getStartTime()));
        doc.add(new LongPoint("end_time", bucket.getEndTime()));

        // Aggregate all events into one searchable text field
        StringBuilder aggregatedText = new StringBuilder();
        for (Event e : bucket.getEvents()) {
            aggregatedText.append(e.toString()).append("|-|");
        }

        doc.add(new TextField("content", aggregatedText.toString(), Field.Store.NO));

        writer.addDocument(doc);
        writer.commit();
    }
}
