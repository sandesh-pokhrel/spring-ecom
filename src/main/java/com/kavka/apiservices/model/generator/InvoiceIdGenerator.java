package com.kavka.apiservices.model.generator;

import com.kavka.apiservices.util.GeneralUtil;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class InvoiceIdGenerator implements IdentifierGenerator {

    String prefix = "INV-";
    private final GeneralUtil generalUtil;

    @Autowired
    public InvoiceIdGenerator(GeneralUtil generalUtil) {
        this.generalUtil = generalUtil;
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o)
            throws HibernateException {
        return prefix + generalUtil.getSerialNumber(2);
    }
}
