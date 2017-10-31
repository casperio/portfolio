package com.fidelity.portfolio.configuration;

import com.fidelity.portfolio.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.transform.FieldExtractor;

public class CustomerFieldExtractor implements FieldExtractor<Customer> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerFieldExtractor.class);

    @Override
    public Object[] extract(Customer customer) {
        LOG.info(customer.toString());
        String[] values = new String[3];
        values[0] = customer.getFirstName();
        values[1] = customer.getLastName();
        values[2] = customer.getPortfolioName();
        return values;
    }
}
