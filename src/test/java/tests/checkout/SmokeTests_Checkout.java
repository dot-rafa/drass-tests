package tests.checkout;

import apiUtils.endpoints.ApplicationAPIs;
import apiUtils.endpoints.AuthAPIs;
import apiUtils.endpoints.BuyerAPIs;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.TestBase;
import models.Buyer;

public class SmokeTests_Checkout extends TestBase {

    @Test
    public void newBuyerCheckout() {

        Buyer buyer = new Buyer();
        buyer.generateRandomBuyer();

        // Send SMS token
        response = AuthAPIs.sendSMSToken(buyer);
        Assert.assertEquals(response.getStatusCode(), 200, "Status Check Failed!");

        // Authenticate
        response = AuthAPIs.Authenticate(buyer);
        Assert.assertEquals(response.getStatusCode(), 200, "Status Check Failed!");

        // Create New Buyer
        response = BuyerAPIs.createBuyer(buyer);
        Assert.assertEquals(response.getStatusCode(), 201, "Status Check Failed!");

        // Update Buyer's Occupation Info
        response = BuyerAPIs.updateBuyer(buyer);
        Assert.assertEquals(response.getStatusCode(), 200, "Status Check Failed!");

        // Update Buyer's Address (Update primary contact)
        response = BuyerAPIs.updateContact(buyer);
        Assert.assertEquals(response.getStatusCode(), 200, "Status Check Failed!");

        // Add Shipping Address (Create new contact)
        buyer.generateRandomContact();
        response = BuyerAPIs.addContact(buyer);
        Assert.assertEquals(response.getStatusCode(), 201, "Status Check Failed!");

        // Create Application
        response = ApplicationAPIs.createApplication(buyer);
        Assert.assertEquals(response.getStatusCode(), 201, "Status Check Failed!");

        // Assert Application Terms
        Assert.assertEquals(response.path("paymentAgreements[0].terms.interval").toString(), "MONTHLY");
        Assert.assertEquals(response.path("paymentAgreements[0].terms.length").toString(), "24");
        Assert.assertEquals(response.path("paymentAgreements[0].terms.rate").toString(), "0");

        // Get Checksum from Application
        response = ApplicationAPIs.getApplication(buyer);
        Assert.assertEquals(response.getStatusCode(), 200, "Status Check Failed!");

        // Checkout
        response = ApplicationAPIs.checkout(buyer);
        Assert.assertEquals(response.getStatusCode(), 200, "Status Check Failed!");

    }
}
