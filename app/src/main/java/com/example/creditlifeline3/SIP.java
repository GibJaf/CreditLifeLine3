package com.example.creditlifeline3;

public class SIP {

    String id;
    Long principal , interest , maturity_value;
    Integer tenure;
    Double rate;

    public SIP(){

    }

    public SIP(String id, Long principal, int tenure, Double rate) {
        this.id = id;
        this.principal = principal;
        this.tenure = tenure;
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public Long getPrincipal() {
        return principal;
    }

    public Long getMaturity_value() {
        return maturity_value;
    }

    public int getTenure() {
        return tenure;
    }

    public Double getRate() {
        return rate;
    }

    public void calculateInterest(){
        this.interest = this.maturity_value - (this.principal * this.tenure);
    }

    public Long TotalPrincipal(){
        if((this.principal != null) && (this.tenure != null))
            return  this.principal * this.tenure;

        return Long.valueOf(-1);
    }
}
