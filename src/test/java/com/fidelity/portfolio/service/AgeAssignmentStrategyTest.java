package com.fidelity.portfolio.service;

import com.fidelity.portfolio.model.Customer;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Calendar;

import static junit.framework.TestCase.assertTrue;

public class AgeAssignmentStrategyTest {

    private LocalDate reference = LocalDate.of(2017, 10, 29);

    @Test
    public void testAggressiveGrowthAssignment() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, Calendar.OCTOBER, 29);
        Customer customer = new Customer();
        customer.setFirstName("Peter");
        customer.setLastName("Parker");
        customer.setDateOfBirth(calendar.getTime());
        AssignmentStrategy strategy = new AgeAssignmentStrategy();
        strategy.assign(reference, customer);
        assertTrue(PortfolioModel.AGGRESSIVE_GROWTH.name().equals(customer.getPortfolioName()));

        calendar.set(2017, Calendar.NOVEMBER, 29);
        customer.setDateOfBirth(calendar.getTime());
        strategy = new AgeAssignmentStrategy();
        strategy.assign(reference, customer);
        assertTrue(PortfolioModel.AGGRESSIVE_GROWTH.name().equals(customer.getPortfolioName()));

        calendar.set(2018, Calendar.NOVEMBER, 31);
        customer.setDateOfBirth(calendar.getTime());
        strategy = new AgeAssignmentStrategy();
        strategy.assign(reference, customer);
        assertTrue(PortfolioModel.AGGRESSIVE_GROWTH.name().equals(customer.getPortfolioName()));
    }

    @Test
    public void testGrowthAssignment() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1976, Calendar.OCTOBER, 29);
        Customer customer = new Customer();
        customer.setFirstName("Peter");
        customer.setLastName("Parker");
        customer.setDateOfBirth(calendar.getTime());
        AssignmentStrategy strategy = new AgeAssignmentStrategy();
        strategy.assign(reference, customer);
        assertTrue(PortfolioModel.GROWTH.name().equals(customer.getPortfolioName()));

        calendar.set(1962, Calendar.OCTOBER, 29);
        customer.setDateOfBirth(calendar.getTime());
        strategy = new AgeAssignmentStrategy();
        strategy.assign(reference, customer);
        assertTrue(PortfolioModel.GROWTH.name().equals(customer.getPortfolioName()));
    }

    @Test
    public void testIncomeAssignment() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1961, Calendar.OCTOBER, 29);
        Customer customer = new Customer();
        customer.setFirstName("Peter");
        customer.setLastName("Parker");
        customer.setDateOfBirth(calendar.getTime());
        AssignmentStrategy strategy = new AgeAssignmentStrategy();
        strategy.assign(reference, customer);
        assertTrue(PortfolioModel.INCOME.name().equals(customer.getPortfolioName()));

        calendar.set(1952, Calendar.OCTOBER, 29);
        customer.setDateOfBirth(calendar.getTime());
        strategy = new AgeAssignmentStrategy();
        strategy.assign(reference, customer);
        assertTrue(PortfolioModel.INCOME.name().equals(customer.getPortfolioName()));
    }

    @Test
    public void testRetirementAssignment() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1951, Calendar.OCTOBER, 29);
        Customer customer = new Customer();
        customer.setFirstName("Peter");
        customer.setLastName("Parker");
        customer.setDateOfBirth(calendar.getTime());
        AssignmentStrategy strategy = new AgeAssignmentStrategy();
        strategy.assign(reference, customer);
        assertTrue(PortfolioModel.RETIREMENT.name().equals(customer.getPortfolioName()));

        calendar.set(100, Calendar.OCTOBER, 29);
        customer.setDateOfBirth(calendar.getTime());
        strategy = new AgeAssignmentStrategy();
        strategy.assign(reference, customer);
        assertTrue(PortfolioModel.RETIREMENT.name().equals(customer.getPortfolioName()));
    }
}