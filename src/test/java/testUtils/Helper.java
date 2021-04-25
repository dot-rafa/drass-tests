package testUtils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.*;

public class Helper {

    //Verify the http response status returned. Check Status Code is 200?
    public void checkStatusCode (Response res, int code) {
        Assert.assertEquals(res.getStatusCode(), code, "Status Check Failed!");
    }

    public void checkInvalidSessionState (JsonPath jp) {
        //An unexpected error happened. Try to restart the application.
        Assert.assertEquals(jp.get("code"), "invalid_session_state", "Message Check Failed!");
    }

    public void checkValidationCode (JsonPath jp, String code) {
        Assert.assertEquals(jp.get("code"), code, "Message Check Failed!");
    }

    public void checkValidationStep (JsonPath jp, String step) {
        Assert.assertTrue(jp.getList("steps").contains(step), "Step Check Failed!");
    }

    public void checkTooShortMessage (JsonPath jp) {
        String message = "Ride length too short";
        Assert.assertTrue(jp.get("fields").toString().contains(message), "Message Check Failed!");
    }

    public void checkTooLongMessage (JsonPath jp) {
        String message = "Ride length too long";
        Assert.assertTrue(jp.get("fields").toString().contains(message), "Message Check Failed!");
    }

    public void checkDoubleRideMessage (JsonPath jp) {
        String message = "You are already in a ride";
        Assert.assertTrue(jp.get("fields").toString().contains(message), "Message Check Failed!");
    }

    public String getAccessToken (JsonPath jp) {
        String accessToken = jp.get("access_token");
        return accessToken;
    }

    public String getAgreementID (JsonPath jp) {
        String agreementID = jp.get("payment_agreement.id");
        return agreementID;
    }

    public String getRefID (JsonPath jp) {
        String refID = jp.get("refID");
        return refID;
    }

    public ArrayList getSessionId (JsonPath jp) {
        ArrayList sessionId = jp.get("session_id");
        return sessionId;
    }

    public String getBuyerToken (JsonPath jp) {
        String buyerToken = jp.get("token");
        return buyerToken;
    }

}

