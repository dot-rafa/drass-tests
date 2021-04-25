package tests;

import testUtils.Helper;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import apiUtils.Utils;

public class TestBase {

    public Response response = null; //Response
    public JsonPath jp = null; //JsonPath

    //Instantiate a Helper Test Method (Helper) Object
    Helper helper = new Helper();

    @BeforeClass
    public void setup (){
        //Test Setup
        //Utils.setBaseURI("https://servicing-simulator.bread-ng.getbread.com"); //Setup Base URI
        Utils.setBaseURI("https://api.bread-ng.getbread.com");
        Utils.setBasePath("/api"); //Setup Base Path
    }

    @AfterClass
    public void afterTest (){
        //Reset Values
        Utils.resetBaseURI();
        Utils.resetBasePath();
    }

}

