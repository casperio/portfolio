package com.fidelity.portfolio.configuration;

import com.fidelity.portfolio.model.Customer;
import com.fidelity.portfolio.service.AssignmentStrategy;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


public class CustomerItemProcessorTest {

    @Test
    public void testProcess() throws Exception {
        AssignmentStrategy strategy = mock(AssignmentStrategy.class);
        Customer customer = new Customer();
        CustomerItemProcessor processor = new CustomerItemProcessor(strategy);
        assertTrue(processor.process(customer) == customer);
    }
}