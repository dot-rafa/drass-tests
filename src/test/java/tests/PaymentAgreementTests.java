package tests;

import io.restassured.RestAssured;
import org.json.simple.JSONObject;
import org.testng.annotations.*;
import apiUtils.Utils;

import java.io.IOException;
import java.lang.reflect.Method;

import static io.restassured.RestAssured.given;

import testUtils.ExtentReports.ExtentTestManager;
//import org.json.simple.JSONObject;


public class PaymentAgreementTests extends TestBase {


    public String PC01_GenerateRefID() throws IOException{
        JSONObject requestBody = Utils.readJsonFile("data/auth/send_token");
        response = RestAssured.given()
                .spec(Utils.buildRequestSpec(Utils.getUATUri(), requestBody))
                .post("/auth/send-token");

        jp = Utils.getJsonPath(response);
        //System.out.println("send token response: " + res.asString());
        helper.checkStatusCode(response, 200);
        String refID = helper.getRefID(jp);
        System.out.println("RefID is: " + refID);
        return refID;
    }

    //this should be called before starting any test
    public String PC02_GenerateBuyerToken() throws IOException{
        String refID = PC01_GenerateRefID();
        JSONObject requestBody = Utils.getLoginUpdatedJsonFile("data/login_request", refID);
        response = RestAssured.given()
                .spec(Utils.buildRequestSpec(Utils.getUATUri(), requestBody))
                .post("/auth/login");

        jp = Utils.getJsonPath(response);
        //System.out.println("login response: " + res.asString());
        helper.checkStatusCode(response, 200);
        String buyerJWT = helper.getBuyerToken(jp);
        System.out.println("Buyer token is: " + buyerJWT);
        return buyerJWT;
    }

    public String PC03_ReturnPaymentAgreement(String buyerJWT, String hours) throws IOException{
        JSONObject requestBody = Utils.readJsonFile("data/payment_agreement_request2");
        response = RestAssured.given()
                .headers(Utils.defaultHeaders("Bearer " + buyerJWT, hours))
                .body(requestBody)
                .post("/payment-agreement");

        jp = Utils.getJsonPath(response);
        helper.checkStatusCode(response, 200);
        return(helper.getAgreementID(jp));
    }

    public String PC04_PerformAccept(String buyerJWT, String hours) throws IOException{
        JSONObject requestBody = Utils.getAcceptUpdatedJsonFile("data/accept_request", buyerJWT);
        String agreementID = PC03_ReturnPaymentAgreement(buyerJWT, hours);
        response = RestAssured.given()
                .headers(Utils.defaultHeaders("Bearer " + buyerJWT, hours))
                .body(requestBody)
                .post("/payment-agreement/" + agreementID + "/accept");

        jp = Utils.getJsonPath(response);
        //System.out.println("Accept payment agreement response: " + res.asString());
        helper.checkStatusCode(response, 200);
        return agreementID;
    }

    @Test
    public void TC01_CreatePaymentAgreement(Method method) throws IOException{
        ExtentTestManager.startTest(method.getName(), "Create a payment agreement for a buyer");
        String hours = "0h";
        JSONObject requestBody = Utils.readJsonFile("data/payment_agreement_request2");
        String buyerJWT = PC02_GenerateBuyerToken();
        response = RestAssured.given()
                .headers(Utils.defaultHeaders("Bearer " + buyerJWT, hours))
                .body(requestBody)
                .post("/payment-agreement");

        jp = Utils.getJsonPath(response);
        //System.out.println("payment agreement response: " + res.asString());
        helper.checkStatusCode(response, 200);
        System.out.println("Agreement ID is: " + helper.getAgreementID(jp));

    }

    @Test
    public void TC02_AcceptPaymentAgreement(Method method) throws IOException{
        ExtentTestManager.startTest(method.getName(), "Accept a payment agreement for a buyer");
        String hours = "0h";
        String buyerJWT = PC02_GenerateBuyerToken();
        JSONObject requestBody = Utils.getAcceptUpdatedJsonFile("data/accept_request", buyerJWT);
        String agreementID = PC03_ReturnPaymentAgreement(buyerJWT, hours);
        response = RestAssured.given()
                .headers(Utils.defaultHeaders("Bearer " + buyerJWT, hours))
                .body(requestBody)
                .post("/payment-agreement/" + agreementID + "/accept");

        jp = Utils.getJsonPath(response);
        System.out.println("Accept payment agreement response: " + response.asString());
        helper.checkStatusCode(response, 200);

    }

    @Test
    public void TC03_SettlePaymentAgreement(Method method) throws IOException{
        ExtentTestManager.startTest(method.getName(), "Settle a payment agreement for a buyer");
        String hours = "0h";
        String buyerJWT = PC02_GenerateBuyerToken();
        JSONObject requestBody = Utils.readJsonFile("data/settle_request");
        String agreementID = PC04_PerformAccept(buyerJWT, hours);
        response = RestAssured.given()
                .headers(Utils.defaultHeaders("Bearer " + buyerJWT, hours))
                .body(requestBody)
                .post("/payment-agreement/" + agreementID + "/settle");

        jp = Utils.getJsonPath(response);
        System.out.println("Settle payment agreement response: " + response.asString());
        helper.checkStatusCode(response, 200);

    }

    @Test
    public void TC04_SettlePaymentAgreement_FutureDate(Method method) throws IOException{
        ExtentTestManager.startTest(method.getName(), "Settle a payment agreement for a buyer");
        String hours = "240h"; //sets the loan origination 10 days ahead
        String buyerJWT = PC02_GenerateBuyerToken();
        JSONObject requestBody = Utils.readJsonFile("data/settle_request");
        String agreementID = PC04_PerformAccept(buyerJWT, hours);
        response = RestAssured.given()
                .headers(Utils.defaultHeaders("Bearer " + buyerJWT, hours))
                .body(requestBody)
                .post("/payment-agreement/" + agreementID + "/settle");

        jp = Utils.getJsonPath(response);
        System.out.println("Settle payment agreement response: " + response.asString());
        helper.checkStatusCode(response, 200);

    }

    public void TC02_CreatePaymentAgreement2(Method method) throws IOException{
        ExtentTestManager.startTest(method.getName(), "Create a payment agreement for a buyer using stored credentials");
        JSONObject requestBody = Utils.readJsonFile("data/payment_agreement_request");
        response = RestAssured.given()
                .headers(Utils.defaultHeaders(Utils.getJsonFileAsString("data/jwt_token"), "0h"))
                .body(requestBody)
                .post("/payment-agreement");

        jp = Utils.getJsonPath(response);
        //System.out.println("payment agreement response: " + res.asString());
        helper.checkStatusCode(response, 200);
        System.out.println("Agreement ID 2 is: " + helper.getAgreementID(jp));

    }


}
