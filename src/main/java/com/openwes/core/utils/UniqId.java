package com.openwes.core.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class UniqId {

    private final static UniqId INSTANCE = new UniqId();
    public final static Long CUSTOM_EPOCH = getCustomEpoch();

    public final static long getCustomEpoch() {
        String setting = System.getProperty("zeus.uniqid.epoch", null);
        if (Validate.isEmpty(setting)) {
            return 1546300800000L;
        } else {
            return Long.valueOf(setting);
        }
    }

    private UniqId() {
    }

    public final static UniqId instance() {
        return INSTANCE;
    }

    private final byte[] ip = MachineUtils.getCurrentIp();
    private final int machineId = MachineUtils.getMachineId();
    private final int processId = MachineUtils.getProcessId();
    private final static AtomicInteger INTERNAL_COUNTER = new AtomicInteger(new SecureRandom().nextInt());

    private SnowFlakeGenerator snowFlakeGenerator;

    public final UniqId initSnowFlakeIdGenerator(int nodeId) {
        snowFlakeGenerator = new SnowFlakeGenerator(nodeId);
        return this;
    }

    private SnowFlakeGenerator snowflake() {
        return snowFlakeGenerator;
    }

    /**
     * Return unique id in one process
     *
     * @return string
     */
    public final static String procUniqId() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8)
                .order(ByteOrder.BIG_ENDIAN)
                .putInt(instance().timestampInSeconds())
                .putInt(INTERNAL_COUNTER.getAndIncrement());
        return Hex.encodeHexString(byteBuffer.array());
    }

    /**
     * {@code
     * <pre>
     * Return an unique id 8 bytes following structure:
     *
     * | 0 bits unused (sign) | 43 bits timestamp in seconds | 8 bits machine Id | 12 bits counter
     *
     * Max id in hex format is  7F  FF  FF  FF  FF  FF  FF  FF
     *
     * <pre>
     * }
     *
     * @return string
     */
    public final static String snowflakeIdStr() {
        return String.valueOf(instance().snowflake().nextId());
    }

    /**
     * {@code
     * <pre>
     * Return an unique id 8 bytes following structure:
     *
     * | 0 bits unused (sign) | 43 bits timestamp in seconds | 8 bits machine Id | 12 bits counter
     *
     * Max id in hex format is  7F  FF  FF  FF  FF  FF  FF  FF
     * <pre>
     * }
     *
     * @return long
     */
    public final static long snowflakeId() {
        return instance().snowflake().nextId();
    }

    /**
     * {@code
     * <pre>
     * Return an unique id 16 bytes following structure:
     *
     * 4 bytes timestamp in seconds | 4 bytes IP | 4 bytes process-id | 4 bytes counter
     * <pre>
     * }
     *
     * @return string
     */
    public final static String uniqId16Bytes() {
        return Hex.encodeHexString(instance().generate16BytesId());
    }

    /**
     * {@code
     * <pre>
     * Return an unique id 12 bytes following structure:
     *
     * 4 bytes timestamp in seconds | 3 bytes machine-id | 2 bytes process-id | 3 bytes counter
     * <pre>
     * }
     *
     * @return string
     */
    public final static String uniqId12Bytes() {
        return Hex.encodeHexString(instance().generate12BytesId());
    }

    private int timestampInSeconds() {
        return (int) ((ClockService.nowMS() - CUSTOM_EPOCH) / 1000);
    }

    private byte[] makeInt4(int i) {
        return new byte[]{
            (byte) ((i >> 24) & 0xFF),
            (byte) ((i >> 16) & 0xFF),
            (byte) ((i >> 8) & 0xFF),
            (byte) ((i) & 0xFF)
        };
    }

    private byte[] makeInt3(int i) {
        return new byte[]{
            (byte) ((i >> 16) & 0xFF),
            (byte) ((i >> 8) & 0xFF),
            (byte) ((i) & 0xFF)
        };
    }

    private byte[] makeInt2(int i) {
        return new byte[]{
            (byte) ((i >> 8) & 0xFF),
            (byte) ((i) & 0xFF)
        };
    }

    private byte[] generate16BytesId() {
        byte[] arr = new byte[16];
        ByteBuffer byteBuffer = ByteBuffer.wrap(arr)
                .order(ByteOrder.BIG_ENDIAN);
        byteBuffer.putInt(timestampInSeconds())
                .put(ip)
                .putInt(processId)
                .putInt(INTERNAL_COUNTER.getAndIncrement());
        return arr;
    }

    private byte[] generate12BytesId() {
        byte[] arr = new byte[12];
        ByteBuffer byteBuffer = ByteBuffer.wrap(arr);
        byteBuffer.put(makeInt4(timestampInSeconds()))
                .put(makeInt3(machineId))
                .put(makeInt2(processId))
                .put(makeInt3(INTERNAL_COUNTER.getAndIncrement()));
        return arr;
    }
}
