package com.personalcapital;

public class Request {
    String planName;
    String sponsorName;
    String sponsorState;

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponserName) {
        this.sponsorName = sponserName;
    }

    public String getSponsorState() {
        return sponsorState;
    }

    public void setSponsorState(String sponsorState) {
        this.sponsorState = sponsorState;
    }

    public void Request(String planName, String sponsorName, String sponsorState) {
        this.planName = planName;
        this.sponsorName = sponsorName;
        this.sponsorName = sponsorState;
    }
}
