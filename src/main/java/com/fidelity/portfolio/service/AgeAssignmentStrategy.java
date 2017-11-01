package com.fidelity.portfolio.service;

import com.fidelity.portfolio.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;

public class AgeAssignmentStrategy implements AssignmentStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(AgeAssignmentStrategy.class);

    /**
     * Assigns a portfolio to a customer based on the client's age
     *
     * @param now Current date used to calculate the client's age
     * @param customer the customer whose age will be derived from the date of birth.
     *                 This method will set the portfolio name on the customer object
     */
    @Override
    public void assign(LocalDate now, Customer customer) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(customer.getDateOfBirth());
        LocalDate dob = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        Period period = Period.between(dob, now);

        final int age = period.getYears();

        if (age <= PortfolioModel.AGGRESSIVE_GROWTH.getAgeUpperBound()) {
            customer.setPortfolioName(PortfolioModel.AGGRESSIVE_GROWTH.name());
        } else if (age >= PortfolioModel.GROWTH.getAgeLowerBound() &&
                age <= PortfolioModel.GROWTH.getAgeUpperBound()) {
            customer.setPortfolioName(PortfolioModel.GROWTH.name());
        } else if (age >= PortfolioModel.INCOME.getAgeLowerBound() &&
                age <= PortfolioModel.INCOME.getAgeUpperBound()) {
            customer.setPortfolioName(PortfolioModel.INCOME.name());
        } else {
            customer.setPortfolioName(PortfolioModel.RETIREMENT.name());
        }

        LOG.info(customer.getFirstName() + " " + customer.getLastName() + " " +
                period.getYears() + " years -> " + customer.getPortfolioName());
    }
}
