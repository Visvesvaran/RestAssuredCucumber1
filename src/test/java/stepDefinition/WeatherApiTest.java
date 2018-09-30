package stepDefinition;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import java.util.concurrent.TimeUnit;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.testng.annotations.BeforeClass;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class WeatherApiTest {

	@Before
	@Given("^Launch the API$")
	public void launch_the_API() throws Throwable {
		RestAssured.baseURI = "http://api.openweathermap.org";

	}

	// Request Validation

	@Then("^Validate Invalid Request Header$")
	public void validate_Invalid_Request_Header() throws Throwable {
		given().params("zip", "46240,us", "appid", "eff4ea11579c589d9971574847d378b9").expect().statusCode(404).when()
				.get("/redirect");
	}

	@Then("^Validate Bad Request$")
	public void validate_Bad_Request() throws Throwable {
		given().params("ZIPPY", "46240,us", "appid", "eff4ea11579c589d9971574847d378b9").expect().statusCode(400).when()
				.get("/data/2.5/weather");
	}

	@Then("^Validate Bad AppId$")
	public void validate_Bad_AppId() throws Throwable {
		given().params("zip", "46240,us", "appid", "eff4ea11579c589d9971574847d30000").expect().statusCode(401).when()
				.get("/data/2.5/weather");
	}

	@Then("^Validate UnAuthorization$")
	public void validate_UnAuthorization() throws Throwable {
		given().auth().basic("Tom", "password").expect().statusCode(401).when().get("/data/2.5/weather");
	}

	// Response Validation
	@Then("^The Response code is successful$")
	public void the_Response_code_is_successful() throws Throwable {
		given().when().get("/data/2.5/weather?zip=46240,us&appid=eff4ea11579c589d9971574847d378b9").then()
				.statusCode(200);

	}

	@Then("^The Response code if negative$")
	public void the_Response_code_if_negative() throws Throwable {
		given().params("zip", "46240,AAA", "appid", "eff4ea11579c589d9971574847d378b9").when().get("/data/2.5/weather")
				.then().body("message", equalTo("city not found"), "cod", equalTo("404"));

	}

	@Then("^The Response is in JSON format$")
	public void the_Response_is_in_JSON_format() throws Throwable {
		given().params("zip", "46240,us", "appid", "eff4ea11579c589d9971574847d378b9").when().get("/data/2.5/weather")
				.then().assertThat().contentType("application/json");

	}

	@Then("^Validate the temperature range$")
	public void validate_the_temperature_range() throws Throwable {

		float minTemp = given().params("zip", "46240,us", "appid", "eff4ea11579c589d9971574847d378b9").when()
				.get("/data/2.5/weather").then().extract().path("main.temp_min");

		float maxTemp = given().params("zip", "46240,us", "appid", "eff4ea11579c589d9971574847d378b9").when()
				.get("/data/2.5/weather").then().extract().path("main.temp_max");

		float temp = given().params("zip", "46240,us", "appid", "eff4ea11579c589d9971574847d378b9").when()
				.get("/data/2.5/weather").then().extract().path("main.temp");
		System.out.println(temp);

		Assert.assertTrue(minTemp <= temp && temp <= maxTemp);

	}

	@Then("^The City name is displayed as expected$")
	public void the_City_name_is_displayed_as_expected() throws Throwable {
		
		given().params("zip", "46240,us", "appid", "eff4ea11579c589d9971574847d378b9").when().get("/data/2.5/weather")
				.then().assertThat().body("name", equalTo("Indianapolis"));
	}

	@Then("^The Key name is available in the response$")
	public void the_Key_name_is_available_in_the_response() throws Throwable {

		given().params("zip", "46240,us", "appid", "eff4ea11579c589d9971574847d378b9").when().get("/data/2.5/weather")
				.then().assertThat().body("$", hasKey("name"));
	}

	@Then("^The Key IsInvisible is not available in the response$")
	public void the_Key_IsInvisible_is_not_available_in_the_response() throws Throwable {

		given().params("zip", "46240,us", "appid", "eff4ea11579c589d9971574847d378b9").when().get("/data/2.5/weather")
				.then().assertThat().body("$", not(hasKey("IamInvisible")));
	}

	@Then("^Validate response time$")
	public void validate_response_time() throws Throwable {

		given().params("zip", "46240,us", "appid", "eff4ea11579c589d9971574847d378b9").when().get("/data/2.5/weather")
				.then().assertThat().time(lessThan(1000L), TimeUnit.MILLISECONDS);
	}

	// ResponseSpecification respSpec;

	public ResponseSpecification createResponseSpecification() {
		ResponseSpecification respSpec;
		respSpec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON)
				.expectBody("name", equalTo("Indianapolis")).build();
		return respSpec;
	}

	@Then("^Validate standard assertions using ResponseSpec$")
	public void validate_standard_assertions_using_ResponseSpec() throws Throwable {
		given().params("zip", "46240,us", "appid", "eff4ea11579c589d9971574847d378b9").when().get("/data/2.5/weather")
				.then().spec(createResponseSpecification());

	}

}
