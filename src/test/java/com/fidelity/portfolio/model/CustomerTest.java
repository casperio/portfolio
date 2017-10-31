package com.fidelity.portfolio.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class CustomerTest {

    @Test
    public void testFirstName() {
        Customer slim = new Customer();
        slim.setFirstName("Warren");
        assertTrue("Warren".equals(slim.getFirstName()));
    }

    @Test
    public void testLastName() {
        Customer slim = new Customer();
        slim.setLastName("Buffett");
        assertTrue("Buffett".equals(slim.getLastName()));
    }

    @Test
    public void testPortfolioName() {
        Customer slim = new Customer();
        slim.setPortfolioName("Retirement");
        assertTrue("Retirement".equals(slim.getPortfolioName()));
    }

    @Test
    public void testDateOfBirth() {
        Date dob = new GregorianCalendar(1930, 7, 30).getTime();
        Customer slim = new Customer();
        slim.setDateOfBirth(dob);
        assertTrue(dob.equals(slim.getDateOfBirth()));
    }

    @Test
    public void testAssets() {
        Customer slim = new Customer();
        BigDecimal assets = new BigDecimal(80.6 * 1_000_000_000);
        slim.setAssets(assets);
        assertTrue(assets.equals(slim.getAssets()));
    }

    @Test
    public void testToString() {
        Customer slim = new Customer();
        slim.setAssets(new BigDecimal(80.6 * 1_000_000_000));
        slim.setDateOfBirth(new GregorianCalendar(1930, 7, 30).getTime());
        slim.setPortfolioName("Growth");
        slim.setLastName("Buffett");
        slim.setFirstName("Warren");
        slim.toString();
    }
}