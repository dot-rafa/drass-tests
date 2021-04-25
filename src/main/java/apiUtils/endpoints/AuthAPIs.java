package apiUtils.endpoints;

import apiUtils.Utils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import models.Buyer;

import static io.restassured.RestAssured.given;

public class AuthAPIs {

    public static Response sendSMSToken(Buyer buyer) {

        JSONObject jsonObject = Utils.getSendTokenRequest(buyer);
        Response response =
                given().urlEncodingEnabled(true)
                        .contentType(ContentType.JSON)
                        .header("X-Bread-APP-ID", "5bb72e47-dd72-4301-843a-9c3f0db9e03d")
                        .body(jsonObject)
                        .post("/auth/send-token");

        if (response.getStatusCode() == 200) {
            buyer.setRefID(response.path("refID").toString());
        }

        return response;
    }

    public static Response Authenticate(Buyer buyer) {

        JSONObject jsonObject = Utils.getAuthenticateRequest(buyer);
        Response response =
                given().urlEncodingEnabled(true)
                        .contentType(ContentType.JSON)
                        .header("X-Bread-APP-ID", "5bb72e47-dd72-4301-843a-9c3f0db9e03d")
                        .body(jsonObject)
                        .post("/auth/authenticate");

        if (response.getStatusCode() == 200) {
            buyer.setAnonymousJWT(response.getHeaders().getValue("authorization"));
        }

        return response;
    }
}
