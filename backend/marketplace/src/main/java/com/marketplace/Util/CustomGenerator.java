package com.marketplace.Util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import de.huxhorn.sulky.ulid.ULID;

public class CustomGenerator implements IdentifierGenerator {

    @Override
    public String generate(SharedSessionContractImplementor session, Object object) {
        ULID generateId = new ULID();
        return generateId.nextULID();
    }
    
}
