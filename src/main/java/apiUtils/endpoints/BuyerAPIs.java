package apiUtils.endpoints;

import apiUtils.Utils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import models.Buyer;

import static io.restassured.RestAssured.given;

public class BuyerAPIs {

    public static Response createBuyer(Buyer buyer) {

        JSONObject jsonObject = Utils.getCreateBuyerRequest(buyer);
        Response response =
                given().urlEncodingEnabled(true)
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + buyer.getAnonymousJWT())
                        .body(jsonObject)
                        .post("/buyer");

        if (response.getStatusCode() == 201) {
            buyer.setBuyerJWT(response.getHeaders().getValue("authorization"));
            buyer.setBuyerID(response.path("id").toString());
            buyer.setPrimaryContactID(response.path("primaryContactID").toString());
        }

        return response;
    }

    public static Response addContact(Buyer buyer) {

        JSONObject jsonObject = Utils.getAddContactRequest(buyer);
        Response response =
                given().urlEncodingEnabled(true)
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + buyer.getBuyerJWT())
                        .body(jsonObject)
                        .pathParam("buyerID", buyer.getBuyerID())
                        .post("/buyer/{buyerID}/contact");

        if (response.getStatusCode() == 201) {
            buyer.setShippingContactID(response.path("id").toString());
        }

        return response;
    }

    public static Response updateContact(Buyer buyer) {

        JSONObject jsonObject = Utils.getUpdateContactRequest(buyer);
        Response response =
                given().urlEncodingEnabled(true)
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + buyer.getBuyerJWT())
                        .body(jsonObject)
                        .pathParam("buyerID", buyer.getBuyerID())
                        .pathParam("contactID", buyer.getPrimaryContactID())
                        .put("/buyer/{buyerID}/contact/{contactID}");

        return response;
    }

    public static Response updateBuyer(Buyer buyer) {

        JSONObject jsonObject = Utils.getUpdateBuyerRequest(buyer);
        Response response =
                given().urlEncodingEnabled(true)
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + buyer.getBuyerJWT())
                        .body(jsonObject)
                        .pathParam("buyerID", buyer.getBuyerID())
                        .put("/buyer/{buyerID}");

        return response;
    }
}
