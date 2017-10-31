package com.fidelity.portfolio.configuration;

import com.fidelity.portfolio.model.Customer;
import com.fidelity.portfolio.service.AssignmentStrategy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;


public class CustomerItemProcessor implements ItemProcessor<Customer, Customer> {

    AssignmentStrategy assignmentStrategy;

    @Autowired
    public CustomerItemProcessor(AssignmentStrategy assignmentStrategy) {
        this.assignmentStrategy = assignmentStrategy;
    }

    @Override
    public Customer process(Customer customer) throws Exception {
        assignmentStrategy.assign(LocalDate.now(), customer);
        return customer;
    }
}
