package main.model;

public class SponsorSponsoredAmount {
    private String sponsor;

    private int sponsorAmt;

    public SponsorSponsoredAmount(String sponsor, int sponsorAmt) {
        this.sponsor = sponsor;
        this.sponsorAmt = sponsorAmt;
    }

    public String getSponsor() {
        return this.sponsor;
    }

    public int getSponsorAmt() {
        return this.sponsorAmt;
    }

}