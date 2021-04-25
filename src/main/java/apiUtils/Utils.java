package apiUtils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import models.Buyer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.*;

public class Utils {
    //Global Setup Variables
    public static String path = "";
    public static String jsonPathTerm;

    //Sets Base URI
    //public static void setBaseURI (){ RestAssured.baseURI = "http://generator.swagger.io/api";
    public static void setBaseURI(String baseUriTerm) {
        RestAssured.baseURI = baseUriTerm;
    }

    //Sets base path
    public static void setBasePath(String basePathTerm) {
        RestAssured.basePath = basePathTerm;
    }

    //Reset Base URI (after test)
    public static void resetBaseURI() {
        RestAssured.baseURI = null;
    }

    //Reset base path
    public static void resetBasePath() {
        RestAssured.basePath = null;
    }

    //Set ContentType
    public static void setContentType(ContentType Type) {
        given().contentType(Type);
    }

    //Set Json path term
    public static void setJsonPathTerm(String jsonPath) {
        jsonPathTerm = jsonPath;
    }

    //Create search query path
    public static void createSearchQueryPath(String searchTerm, String param, String paramValue) {
        path = searchTerm + "/" + jsonPathTerm + "?" + param + "=" + paramValue;
    }

    public static String getUATUri() {
        return "https://api.bread-ng.getbread.com";
    }

    //Set default headers
    public static Map<String, Object> defaultHeaders(String fullToken, String hours) throws IOException {
        //String fullToken = getJsonFileAsString("data/jwt_token");
        //System.out.println("Token is: " + fullToken);
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "*/*");
        headers.put("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
        headers.put("Authorization", fullToken);
        headers.put("Connection", "keep-alive");
        headers.put("X-Time-Offset", hours);
        //System.out.println("Headers: " + headers.toString());
        return headers;
    }

    //Set parameterized headers
    public static Map<String, String> paramHeaders(String cluster, String device_id) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("KALYKE_CLUSTER_ID", "cluster");
        headers.put("Device-Id", "device_id");
        return headers;
    }

    //Build request specification
    public static RequestSpecification buildRequestSpec(String uri, JSONObject body) throws JSONException {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBody(body);
        builder.setBaseUri(uri);
        builder.setContentType("application/json; charset=UTF-8");
        builder.addHeader("X-Bread-APP-ID", "5bb72e47-dd72-4301-843a-9c3f0db9e03d");
        builder.addHeader("Accept-Language", "en");
        return builder.build();
    }

    //Returns response by given path
    public static Response getResponseByPath(String path) {
        //System.out.println("path: " + path);
        return get(path);
    }

    //Return response
    public static Response getResponse() {
        //System.out.print("path: " + path +"\n");
        return get(path);
    }

    //Return Response as JsonPath object
    public static JsonPath getJsonPath(Response res) {
        String json = res.asString();
        //System.out.println("returned json: " + json);
        return new JsonPath(json);
    }

    public static String getRandomNumber() {
        Random rand = new Random(); //instance of random class
        int upper_bound = 10;
        //generate random values from 0-9
        int int_random = rand.nextInt(upper_bound);

        //System.out.println("Random integer value from 0 to " + (upper_bound-1) + " : "+ int_random);
        return Integer.toString(int_random);
    }

    public static String getRandomNumbers() {
        int min = 100;
        int max = 999;

        //Generate random int value from 50 to 100
        int random_int = (int) (Math.random() * (max - min + 1) + min);
        //System.out.println("Random value in int from "+min+" to "+max+ " : is " + random_int);
        return Integer.toString(random_int);
    }

    public static String generatePhoneNumber() {
        String phoneNumber = "+333065" + getRandomNumbers() + getRandomNumber() + getRandomNumber();
        return phoneNumber;
    }

    //Read Json file and return as a string
    public static String getJsonFileAsString(String pathname) throws IOException {
        String fileAsString = new String(Files.readAllBytes(Paths.get(pathname)));
        //System.out.println("File as string is: " + fileAsString);
        return fileAsString;

    }

    //Read Json file and return as JsonObject
    public static JSONObject readJsonFile(String pathname) {
        JSONObject jsonObject = new JSONObject();
        try {
            FileReader reader = new FileReader(pathname);
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(reader);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        //System.out.println("File as json object is: " + jsonObject);
        return jsonObject;
    }

    public static JSONObject getSendTokenRequest(Buyer buyer) {
        JSONObject jsonObject = new JSONObject();
        try {
            FileReader reader = new FileReader("data/auth/send_token");
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(reader);

            jsonObject.put("phone", buyer.getPhoneNumber());

            JSONObject metadataObj = (JSONObject) ((JSONArray) jsonObject.get("disclosures")).get(0);
            metadataObj.put("acceptedAt", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getAuthenticateRequest(Buyer buyer) {
        JSONObject jsonObject = new JSONObject();
        try {
            FileReader reader = new FileReader("data/auth/authentication");
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(reader);

            jsonObject.put("refID", buyer.getRefID());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getCreateBuyerRequest(Buyer buyer) {
        JSONObject jsonObject = new JSONObject();
        try {
            FileReader reader = new FileReader("data/buyer/create_buyer");
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(reader);

            JSONObject metadataObj = ((JSONObject) (jsonObject.get("identity")));

            metadataObj.put("email", buyer.getEmail());
            metadataObj.put("phone", buyer.getPhoneNumber());
            metadataObj.put("birthDate", buyer.getBirthdate());

            metadataObj = (JSONObject) ((JSONObject) (jsonObject.get("identity"))).get("name");
            metadataObj.put("givenName", buyer.getGivenName());
            metadataObj.put("familyName", buyer.getFamilyName());

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getAddContactRequest(Buyer buyer) {
        JSONObject jsonObject = new JSONObject();
        try {
            FileReader reader = new FileReader("data/buyer/add_contact");
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(reader);

            jsonObject.put("email", buyer.getShippingContactEmail());
            jsonObject.put("phone", buyer.getShippingContactPhone());

            JSONObject metadataObj = (JSONObject) (jsonObject.get("name"));
            metadataObj.put("givenName", buyer.getShippingContactGivenName());
            metadataObj.put("familyName", buyer.getShippingContactFamilyName());

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getUpdateContactRequest(Buyer buyer) {
        JSONObject jsonObject = new JSONObject();
        try {
            FileReader reader = new FileReader("data/buyer/update_contact");
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(reader);

            jsonObject.put("email", buyer.getEmail());
            jsonObject.put("phone", buyer.getPhoneNumber());

            JSONObject metadataObj = (JSONObject) (jsonObject.get("name"));
            metadataObj.put("givenName", buyer.getGivenName());
            metadataObj.put("familyName", buyer.getFamilyName());

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getUpdateBuyerRequest(Buyer buyer) {
        JSONObject jsonObject = new JSONObject();
        try {
            FileReader reader = new FileReader("data/buyer/update_buyer");
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(reader);

            JSONObject metadataObj = ((JSONObject) (jsonObject.get("identity")));

            metadataObj.put("email", buyer.getEmail());
            metadataObj.put("phone", buyer.getPhoneNumber());
            metadataObj.put("birthDate", buyer.getBirthdate());

            metadataObj = (JSONObject) ((JSONObject) (jsonObject.get("identity"))).get("name");
            metadataObj.put("givenName", buyer.getGivenName());
            metadataObj.put("familyName", buyer.getFamilyName());

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getCreateApplicationRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            FileReader reader = new FileReader("data/application/create_application");
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(reader);

            JSONObject metadataObj = (JSONObject) ((JSONArray) jsonObject.get("disclosures")).get(0);
            metadataObj.put("acceptedAt", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getCheckoutRequest(Buyer buyer) {
        JSONObject jsonObject = new JSONObject();
        try {
            FileReader reader = new FileReader("data/application/checkout");
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(reader);


            jsonObject.put("buyerShippingContactID", buyer.getShippingContactID());

            JSONObject metadataObj = (JSONObject) ((JSONArray) jsonObject.get("disclosures")).get(0);
            metadataObj.put("checksum", buyer.getChecksum());
            metadataObj.put("acceptedAt", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));


        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    //Update Json file before reading and returning it as JsonObject
    public static JSONObject getLoginUpdatedJsonFile(String pathname, String value) {
        JSONObject jsonObject = new JSONObject();
        try {
            FileReader reader = new FileReader(pathname);
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(reader);

            jsonObject.put("refID", value);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        //System.out.println("File as json object is: " + jsonObject);
        return jsonObject;
    }

    public static JSONObject getAcceptUpdatedJsonFile(String pathname, String value) {
        JSONObject jsonObject = new JSONObject();
        try {
            FileReader reader = new FileReader(pathname);
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(reader);

            JSONObject metadataObj = ((JSONObject) (jsonObject.get("acknowledgement")));

            metadataObj.put("buyer_metadata", value);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        //System.out.println("File as json object is: " + jsonObject);
        return jsonObject;
    }

}
