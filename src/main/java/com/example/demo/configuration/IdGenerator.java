package com.example.demo.configuration;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.Random;

public class IdGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object o) {
        Random random = new Random();

        return random.nextInt(100000,999999);
    }
}
