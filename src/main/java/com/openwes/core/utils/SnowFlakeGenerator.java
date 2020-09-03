package com.openwes.core.utils;

import java.time.Instant;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public final class SnowFlakeGenerator {

    private static final int TOTAL_BITS = 64;
    private static final int EPOCH_BITS = 44;
    private static final int NODE_ID_BITS = 8;
    private static final int SEQUENCE_BITS = 12;

    private static final int MAXNODEID = (int) (Math.pow(2, NODE_ID_BITS) - 1);
    private static final int MAXSEQUENCE = (int) (Math.pow(2, SEQUENCE_BITS) - 1);

    // Custom Epoch (January 1, 2019 Midnight UTC = 2019-01-01T00:00:00Z)
    private static final long CUSTOM_EPOCH = UniqId.getCustomEpoch();

    private final int nodeId;

    private volatile long lastTimestamp = -1L;
    private volatile long sequence = 0L;

    // Let SnowFlakeGenerator generate a nodeId
    public SnowFlakeGenerator(int nodeId) {
        this.nodeId = nodeId & MAXNODEID;
    }

    public synchronized long nextId() {
        long currentTimestamp = timestamp();

        if (currentTimestamp < lastTimestamp) {
            throw new IllegalStateException("Invalid System Clock!");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAXSEQUENCE;
            if (sequence == 0) {
                // Sequence Exhausted, wait till next millisecond.
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            // reset sequence to start with zero for the next millisecond
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;

        long id = currentTimestamp << (TOTAL_BITS - EPOCH_BITS);
        id |= (nodeId << (TOTAL_BITS - EPOCH_BITS - NODE_ID_BITS));
        id |= sequence;
        return id;
    }

    // Get current timestamp in milliseconds, adjust for the custom epoch.
    private static long timestamp() {
        return Instant.now().toEpochMilli() - CUSTOM_EPOCH;
    }

    // Block and wait till next millisecond
    private long waitNextMillis(long currentTimestamp) {
        while (currentTimestamp == lastTimestamp) {
            currentTimestamp = timestamp();
        }
        return currentTimestamp;
    }

}
