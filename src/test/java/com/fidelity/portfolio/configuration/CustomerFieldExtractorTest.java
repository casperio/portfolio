package com.fidelity.portfolio.configuration;

import com.fidelity.portfolio.model.Customer;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerFieldExtractorTest {

    @Test
    public void testExtract() throws Exception {
        Customer customer = mock(Customer.class);
        CustomerFieldExtractor extractor = new CustomerFieldExtractor();
        when(customer.getFirstName()).thenReturn("Toto");
        when(customer.getLastName()).thenReturn("Schillaci");
        when(customer.getPortfolioName()).thenReturn("Retirement");

        Object[] values = extractor.extract(customer);
        assertTrue(values instanceof String[]);
        assertTrue(values != null);
        assertTrue(values.length == 3);
        assertTrue("Toto".equals(values[0]));
        assertTrue("Schillaci".equals(values[1]));
        assertTrue("Retirement".equals(values[2]));
    }
}