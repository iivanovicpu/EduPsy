package hr.iivanovic.psyedu.util;

import java.util.UUID;

/**
 * Our generator of UUIDs, just random.
 *
 * @author ftomassetti
 * @since Mar 2015
 */
public class RandomUuidGenerator implements UuidGenerator {
    
    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}