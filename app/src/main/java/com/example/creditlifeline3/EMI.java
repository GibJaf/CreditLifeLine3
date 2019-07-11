package com.example.creditlifeline3;

public class EMI {

    String id;
    long principal;
    double rate;
    long emi;
    long interest;
    long amount;
    String startDate;
    String endDate;
    long tenure; // in months

    public EMI(){
    }

    public EMI (String id , long principal, double rate, String startDate, String endDate) {
        this.id = id;
        this.principal = principal;
        this.rate = rate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public long getPrincipal() {
        return principal;
    }

    public double getRate() {
        return rate;
    }

    public long getEmi() {
        return emi;
    }

    public long getInterest() {
        return interest;
    }

    public long getAmount() {
        return amount;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public long getTenure() {
        return tenure;
    }
}
