package com.fidelity.portfolio.service;

import java.time.LocalDate;
import com.fidelity.portfolio.model.Customer;

public interface AssignmentStrategy {

    void assign(LocalDate now, Customer customer);
}
