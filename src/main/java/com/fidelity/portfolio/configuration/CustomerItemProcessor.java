package com.fidelity.portfolio.configuration;

import com.fidelity.portfolio.model.Customer;
import com.fidelity.portfolio.service.AssignmentStrategy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;


/**
 * Implementation of the spring batch ItemProcessor
 */
public class CustomerItemProcessor implements ItemProcessor<Customer, Customer> {

    private AssignmentStrategy assignmentStrategy;

    @Autowired
    CustomerItemProcessor(AssignmentStrategy assignmentStrategy) {
        this.assignmentStrategy = assignmentStrategy;
    }

    /**
     * Implements the ItemProcessor stage of the spring batch step
     * @param customer input customer object
     * @return customer object with an assigned portfolio
     */
    @Override
    public Customer process(Customer customer) {
        assignmentStrategy.assign(LocalDate.now(), customer);
        return customer;
    }
}
