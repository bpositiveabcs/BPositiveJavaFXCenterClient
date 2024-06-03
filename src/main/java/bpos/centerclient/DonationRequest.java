package bpos.centerclient;

import bpos.common.model.Donation;
import bpos.common.model.Person;

public class DonationRequest {
    private Donation donation;
    private Person person;

    // Getters and setters
    public Donation getDonation() {
        return donation;
    }

    public void setDonation(Donation donation) {
        this.donation = donation;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
