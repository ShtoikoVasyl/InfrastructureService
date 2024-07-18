package edu.shtoiko.infrastructureservice.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class SixDigitIdGenerator implements IdentifierGenerator {

    private static final Random random = new Random();

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        long id;
        do {
            id = 100000 + random.nextInt(900000);
        } while (isIdExists(session, id));

        return id;
    }

    private boolean isIdExists(SharedSessionContractImplementor session, long id) {
        String sql = "SELECT COUNT(*) FROM terminals WHERE id = :id";
        Long count = ((Number) session.createNativeQuery(sql)
                .setParameter("id", id)
                .getSingleResult()).longValue();
        return count > 0;
    }
}