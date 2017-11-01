package com.fidelity.portfolio.service;

public enum PortfolioModel {

    AGGRESSIVE_GROWTH(0.8f, 0.1f, 0.1f, 0, 40),
    GROWTH(0.7f, 0.2f, 0.1f, 41, 55),
    INCOME(0.5f, 0.3f, 0.2f, 56, 65),
    RETIREMENT(0.2f, 0.3f, 0.5f, 66, Integer.MAX_VALUE);

    private float equityFundsPct;
    private float bondFundsPct;
    private float cashFundsPct;
    private int ageLowerBound;
    private int ageUpperBound;

    PortfolioModel(float equityFundsPct, float bondFundsPct, float cashFundsPct, int ageLowerBound, int ageUpperBound) {
        this.equityFundsPct = equityFundsPct;
        this.bondFundsPct = bondFundsPct;
        this.cashFundsPct = cashFundsPct;
        this.ageLowerBound = ageLowerBound;
        this.ageUpperBound = ageUpperBound;
    }

    public int getAgeLowerBound() {
        return ageLowerBound;
    }

    public int getAgeUpperBound() {
        return ageUpperBound;
    }

    @Override
    public String toString() {
        return "PortfolioModel{" + "equityFundsPct=" + equityFundsPct +
                ", bondFundsPct=" + bondFundsPct +
                ", cashFundsPct=" + cashFundsPct +
                ", ageLowerBound=" + ageLowerBound +
                ", ageUpperBound=" + ageUpperBound +
                '}';
    }
}
