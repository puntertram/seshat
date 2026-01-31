package org.example.api;

import org.example.BucketManager;
import org.example.model.Event;
import org.example.storage.DFSWriter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ingest")
public class IngestController {

    private final BucketManager bucketManager;
    private final DFSWriter dfsWriter;

    public IngestController(BucketManager bucketManager, DFSWriter dfsWriter) {
        this.bucketManager = bucketManager;
        this.dfsWriter = dfsWriter;
    }

    @PostMapping
    public ResponseEntity<?> ingestEvent(@RequestBody Event event) {
        try {
            bucketManager.ingest(event);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
