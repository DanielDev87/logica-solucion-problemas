package com.logsoluprobl.appbank.service.strategies;

import com.logsoluprobl.appbank.service.InterestStrategy;

public class SimpleRateStrategy implements InterestStrategy {

    private double rate;

    public SimpleRateStrategy(double rate) {
        this.rate = rate;
    }

    @Override
    public double calculateInterest(double balance) {
        return balance * rate;
    }

    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }
}

