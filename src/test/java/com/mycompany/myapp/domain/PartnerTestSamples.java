package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PartnerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Partner getPartnerSample1() {
        return new Partner().id(1L).codep("codep1").type("type1").name("name1").contact("contact1");
    }

    public static Partner getPartnerSample2() {
        return new Partner().id(2L).codep("codep2").type("type2").name("name2").contact("contact2");
    }

    public static Partner getPartnerRandomSampleGenerator() {
        return new Partner()
            .id(longCount.incrementAndGet())
            .codep(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .contact(UUID.randomUUID().toString());
    }
}
