package app.project.microblogging.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MensajeMuroTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MensajeMuro getMensajeMuroSample1() {
        return new MensajeMuro().id(1L).mensaje("mensaje1").tags("tags1");
    }

    public static MensajeMuro getMensajeMuroSample2() {
        return new MensajeMuro().id(2L).mensaje("mensaje2").tags("tags2");
    }

    public static MensajeMuro getMensajeMuroRandomSampleGenerator() {
        return new MensajeMuro().id(longCount.incrementAndGet()).mensaje(UUID.randomUUID().toString()).tags(UUID.randomUUID().toString());
    }
}
