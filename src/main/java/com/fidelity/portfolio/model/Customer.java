package com.fidelity.portfolio.model;

import java.math.BigDecimal;
import java.util.Date;

public class Customer {

    private String firstName;
    private String lastName;
    private String portfolioName;
    private Date dateOfBirth;
    private BigDecimal assets;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public BigDecimal getAssets() {
        return assets;
    }

    public void setAssets(BigDecimal assets) {
        this.assets = assets;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Customer{");
        sb.append("firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", portfolioName='").append(portfolioName).append('\'');
        sb.append(", dateOfBirth=").append(dateOfBirth);
        sb.append(", assets=").append(assets);
        sb.append('}');
        return sb.toString();
    }
}
