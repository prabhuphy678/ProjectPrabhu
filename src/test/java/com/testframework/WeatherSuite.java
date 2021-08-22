package com.testframework;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.json.JSONArray;

import com.pages.PageUI;


/**
 * Unit test for simple App.
 */
public class WeatherSuite 
{
	static HashMap<String, HashMap<String, String>> testSheet;
	static WebDriver driver;
	static Properties prop;
	static String url;
	static String baseUrl;
	static String appId;
	
    @BeforeTest
    public void init() throws Exception {
    	driver=Utils.webDriverUtil();
    	prop=Utils.getProperty();
    	url=prop.getProperty("url");
    	baseUrl =prop.getProperty("baseUrl");
    	appId = prop.getProperty("appId");
    	testSheet = Utils.getTestData(WeatherSuite.class.getName(), "testDataXLS.xls");
    }
    
    @Test (dataProvider = "login")
    public void test(String testCaseId) {
    	HashMap<String, String> testData  = testSheet.get(testCaseId);
    	driver.get(url);
    	PageUI pgUI = new PageUI(testData);
    	HashMap<String,String> resultFromUI = pgUI.getWeatherDetailsForCity(testData.get("CITY"));
    	Response response = getWeatherDetailsForCityFromAPI(testData.get("CITY"));
    	 Assert.assertEquals(response.getStatusCode(), 200);

    	 JSONObject object = new JSONObject(response.asString());
    	 String pressure = object.getJSONObject("main").get("Pressure").toString();
    	 Assert.assertEquals(pressure, resultFromUI.get("Pressure"));
         
    	System.out.println(resultFromUI);
    	System.out.println(testData);
    
    }
    
    
    
    Response getWeatherDetailsForCityFromAPI(String city){
    	HashMap<String,String> pathparams = new HashMap<String,String>();
    	RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        pathparams.put("q", city);
        pathparams.put("appid", appId);
        
        request.pathParams(pathparams);
        
        Response response = request.get("/data/2.5/weather");

    	return response;
    }
    @DataProvider
    public Object[][] login() {
		return new Object[][] { { "WeatherInfo"}
		};
    }
    }
