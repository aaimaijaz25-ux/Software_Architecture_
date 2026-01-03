package com.healthcare.model;

/* Referral Model class */
public class Referral {
    private String referralID;
    private String patientID;
    private String referringClinicianID;
    private String receivingClinicianID;
    private String referringFacilityID;
    private String receivingFacilityID;
    private String referralDate;
    private String urgencyLevel;
    private String referralReason;
    private String clinicalSummary;
    private String requestedInvestigations;
    private String appointmentID;
    private String notes;
    private String status;
    private String createdDate;
    private String lastUpdated;

    public Referral() {
    }

    public Referral(String referralID, String patientID, String referringClinicianID,
                    String receivingClinicianID, String referringFacilityID, String receivingFacilityID,
                    String referralDate, String urgencyLevel, String clinicalSummary,String requestedInvestigation, String status) {
        this(referralID, patientID, referringClinicianID, receivingClinicianID, referringFacilityID, receivingFacilityID,
             referralDate, urgencyLevel, "", clinicalSummary, requestedInvestigation, "", "", status, "", "");
    }

    public Referral(String referralID, String patientID, String referringClinicianID,
                    String receivingClinicianID, String referringFacilityID, String receivingFacilityID,
                    String referralDate, String urgencyLevel, String referralReason, String clinicalSummary,
                    String requestedInvestigations, String appointmentID, String notes, String status,
                    String createdDate, String lastUpdated) {
        this.referralID = referralID;
        this.patientID = patientID;
        this.referringClinicianID = referringClinicianID;
        this.receivingClinicianID = receivingClinicianID;
        this.referringFacilityID = referringFacilityID;
        this.receivingFacilityID = receivingFacilityID;
        this.referralDate = referralDate;
        this.urgencyLevel = urgencyLevel;
        this.referralReason = referralReason;
        this.clinicalSummary = clinicalSummary;
        this.requestedInvestigations = requestedInvestigations;
        this.appointmentID = appointmentID;
        this.notes = notes;
        this.status = status;
        this.createdDate = createdDate;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public String getReferralID() {
        return referralID;
    }

    public void setReferralID(String referralID) {
        this.referralID = referralID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getReferringClinicianID() {
        return referringClinicianID;
    }

    public void setReferringClinicianID(String referringClinicianID) {
        this.referringClinicianID = referringClinicianID;
    }

    public String getReceivingClinicianID() {
        return receivingClinicianID;
    }

    public void setReceivingClinicianID(String receivingClinicianID) {
        this.receivingClinicianID = receivingClinicianID;
    }

    public String getReferringFacilityID() {
        return referringFacilityID;
    }

    public void setReferringFacilityID(String referringFacilityID) {
        this.referringFacilityID = referringFacilityID;
    }

    public String getReceivingFacilityID() {
        return receivingFacilityID;
    }

    public void setReceivingFacilityID(String receivingFacilityID) {
        this.receivingFacilityID = receivingFacilityID;
    }

    public String getreferralDate() {
        return referralDate;
    }

    public void setreferralDate(String referraldate) {
        this.referralDate = referraldate;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public String getClinicalSummary() {
        return clinicalSummary;
    }

    public void setClinicalSummary(String clinicalSummary) {
        this.clinicalSummary = clinicalSummary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReferralReason() {
        return referralReason;
    }

    public void setReferralReason(String referralReason) {
        this.referralReason = referralReason;
    }

    public String getRequestedInvestigations() {
        return requestedInvestigations;
    }

    public void setRequestedInvestigations(String requestedInvestigations) {
        this.requestedInvestigations = requestedInvestigations;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return referralID + " - " + referralDate + " (" + urgencyLevel + ")";
    }
}



