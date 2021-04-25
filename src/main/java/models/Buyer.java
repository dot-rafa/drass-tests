package models;

import org.apache.commons.lang3.RandomStringUtils;

public class Buyer {

    private String phoneNumber;

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String number) {
        this.phoneNumber = number;
    }

    private String email;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String givenName;

    public String getGivenName() {
        return this.givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    private String familyName;

    public String getFamilyName() {
        return this.familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    private String DOB;

    public String getBirthdate() {
        return this.DOB;
    }

    public void setBirthdate(String DOB) {
        this.DOB = DOB;
    }

    private String refID;

    public String getRefID() {
        return this.refID;
    }

    public void setRefID(String refID) {
        this.refID = refID;
    }

    private String anonymousJWT;

    public String getAnonymousJWT() {
        return this.anonymousJWT;
    }

    public void setAnonymousJWT(String anonymousJWT) {
        this.anonymousJWT = anonymousJWT;
    }

    private String buyerJWT;

    public String getBuyerJWT() {
        return this.buyerJWT;
    }

    public void setBuyerJWT(String BuyerJWT) {
        this.buyerJWT = BuyerJWT;
    }

    private String buyerID;

    public String getBuyerID() {
        return this.buyerID;
    }

    public void setBuyerID(String BuyerID) {
        this.buyerID = BuyerID;
    }

    private String primaryContactID;

    public String getPrimaryContactID() {
        return this.primaryContactID;
    }

    public void setPrimaryContactID(String contactID) {
        this.primaryContactID = contactID;
    }

    private String shippingContactID;

    public String getShippingContactID() {

        if (shippingContactID == null) {
            return primaryContactID;
        } else {
            return this.shippingContactID;
        }
    }

    public void setShippingContactID(String shippingContactID) {
        this.shippingContactID = shippingContactID;
    }

    private String shippingContactPhone;

    public String getShippingContactPhone() {
        return this.shippingContactPhone;
    }

    public void setShippingContactPhone(String shippingContactPhone) {
        this.shippingContactPhone = shippingContactPhone;
    }

    private String shippingContactEmail;

    public String getShippingContactEmail() {
        return this.shippingContactEmail;
    }

    public void setShippingContactEmail(String shippingContactEmail) {
        this.shippingContactEmail = shippingContactEmail;
    }

    private String shippingContactGivenName;

    public String getShippingContactGivenName() {
        return this.shippingContactGivenName;
    }

    public void setShippingContactGivenName(String shippingContactGivenName) {
        this.shippingContactGivenName = shippingContactGivenName;
    }

    private String shippingContactFamilyName;

    public String getShippingContactFamilyName() {
        return this.shippingContactFamilyName;
    }

    public void setShippingContactFamilyName(String shippingContactFamilyName) {
        this.shippingContactFamilyName = shippingContactFamilyName;
    }

    private String applicationID;

    public String getApplicationID() {
        return this.applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    private String agreementID;

    public String getAgreementID() {
        return this.agreementID;
    }

    public void setAgreementID(String agreementID) {
        this.agreementID = agreementID;
    }

    private String checksum;

    public String getChecksum() {
        return this.checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public void generateRandomBuyer() {

        this.phoneNumber = "+1" + RandomStringUtils.randomNumeric(10);
        this.email = "QA_User_" + phoneNumber + "@gmail.com";

        this.givenName = RandomStringUtils.randomAlphabetic(5);
        this.familyName = RandomStringUtils.randomAlphabetic(5);

        String year = Integer.toString((int) Math.round(Math.random() * (2000 - 1900)) + 1900);
        String month = "0" + ((int) Math.round(Math.random() * (9 - 1)) + 1);
        String day = Integer.toString((int) Math.round(Math.random() * (28 - 10)) + 10);

        this.DOB = year + "-" + month + "-" + day;
    }

    public void generateRandomContact() {

        this.shippingContactPhone = "+1" + RandomStringUtils.randomNumeric(10);
        this.shippingContactEmail = "QA_Contact_" + shippingContactPhone + "@gmail.com";

        this.shippingContactGivenName = RandomStringUtils.randomAlphabetic(5);
        this.shippingContactFamilyName = RandomStringUtils.randomAlphabetic(5);
    }
}
