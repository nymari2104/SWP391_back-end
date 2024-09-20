package com.SWP391.KoiManagement.configuration;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.Random;

public class IdGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object o) {
        Random random = new Random();

        int id = 100000 + random.nextInt(899999);

        return id;
    }
}
