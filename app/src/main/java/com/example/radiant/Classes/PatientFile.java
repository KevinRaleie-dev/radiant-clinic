package com.example.radiant.Classes;

public class PatientFile {
    private String idNumber;
    private String address;
    private String occupation;
    private String employer;
    private String rName;
    private String rSurname;
    private String rNumber;

    public PatientFile() {
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOccupaton() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getrSurname() {
        return rSurname;
    }

    public void setrSurname(String rSurname) {
        this.rSurname = rSurname;
    }

    public String getrNumber() {
        return rNumber;
    }

    public void setrNumber(String rNumber) {
        this.rNumber = rNumber;
    }
}
