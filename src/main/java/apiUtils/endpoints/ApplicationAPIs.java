package apiUtils.endpoints;

import apiUtils.Utils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import models.Buyer;

import static io.restassured.RestAssured.given;

public class ApplicationAPIs {

    public static Response createApplication(Buyer buyer) {

        Response response =
                given().urlEncodingEnabled(true)
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + buyer.getBuyerJWT())
                        .body(Utils.getCreateApplicationRequest())
                        .post("/application");

        if (response.getStatusCode() == 201) {
            buyer.setApplicationID(response.path("id").toString());
            buyer.setAgreementID(response.path("paymentAgreements[0].id").toString());
        }

        return response;
    }

    public static Response getApplication(Buyer buyer) {

        Response response =
                given().urlEncodingEnabled(true)
                        .header("Authorization", "Bearer " + buyer.getBuyerJWT())
                        .pathParam("agreementID", buyer.getAgreementID())
                        .get("/disclosure/payment-agreement/{agreementID}/views/loan-agreement");

        if (response.getStatusCode() == 200) {
            buyer.setChecksum(response.getHeaders().getValue("x-bread-checksum"));
        }

        return response;
    }

    public static Response checkout(Buyer buyer) {

        JSONObject jsonObject = Utils.getCheckoutRequest(buyer);
        Response response =
                given().urlEncodingEnabled(true)
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + buyer.getBuyerJWT())
                        .body(jsonObject)
                        .pathParam("applicationID", buyer.getApplicationID())
                        .pathParam("agreementID", buyer.getAgreementID())
                        .post("/application/{applicationID}/agreement/{agreementID}/checkout");

        return response;
    }
}
