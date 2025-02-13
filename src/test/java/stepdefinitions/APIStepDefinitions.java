package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIStepDefinitions {
    private RequestSpecification request;
    private Response response;
    private Map<String, Object> requestPayload = new HashMap<>();

    @Given("I set the base URL for the API")
    public void setBaseURL() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        request = RestAssured.given();
        request.header("Content-Type", "application/json");
    }

    @When("I send a POST request to {string} with the following payload:")
    public void sendPostRequest(String endpoint, DataTable dataTable) {
        // Convert DataTable to a Map
        List<Map<String, String>> payloadData = dataTable.asMaps(String.class, String.class);

        // Build the request payload dynamically
        Map<String, Object> requestPayload = new HashMap<>();
        for (Map<String, String> row : payloadData) {
            String key = row.get("key");
            String type = row.get("type");
            String value = row.get("value");

            // Handle nested keys (e.g., "bookingdates.checkin")
            if (key.contains(".")) {
                String[] keys = key.split("\\.");
                Map<String, Object> nestedMap = requestPayload.containsKey(keys[0]) ?
                        (Map<String, Object>) requestPayload.get(keys[0]) : new HashMap<>();
                nestedMap.put(keys[1], parseValue(type, value));
                requestPayload.put(keys[0], nestedMap);
            } else {
                requestPayload.put(key, parseValue(type, value));
            }
        }

        // Send the request
        response = request.body(requestPayload).post(endpoint);
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int statusCode) {
        Assert.assertEquals(response.getStatusCode(), statusCode);
    }

    @Then("the response should contain the booking details")
    public void verifyBookingDetails() {
        String firstName = response.jsonPath().getString("booking.firstname");
        String lastName = response.jsonPath().getString("booking.lastname");
        int totalPrice = response.jsonPath().getInt("booking.totalprice");

        Assert.assertNotNull(firstName, "First name should not be null");
        Assert.assertNotNull(lastName, "Last name should not be null");
        Assert.assertTrue(totalPrice > 0, "Total price should be greater than 0");
    }

    // Helper method to parse values based on type
    private Object parseValue(String type, String value) {
        switch (type.toLowerCase()) {
            case "number":
                return Integer.parseInt(value);
            case "boolean":
                return Boolean.parseBoolean(value);
            case "string":
            default:
                return value;
        }
    }

    @Given("order API with params:")
    public void setRequestPayload(DataTable dataTable) {
        // Convert DataTable to a List of Maps
        List<Map<String, String>> payloadData = dataTable.asMaps(String.class, String.class);

        // Build the request payload dynamically
        for (Map<String, String> row : payloadData) {
            String key = row.get("key");
            String type = row.get("type");
            String value = row.get("value");

            // Handle nested keys (e.g., "bookingdates.checkin")
            if (key.contains(".")) {
                String[] keys = key.split("\\.");
                Map<String, Object> nestedMap = requestPayload.containsKey(keys[0]) ?
                        (Map<String, Object>) requestPayload.get(keys[0]) : new HashMap<>();
                nestedMap.put(keys[1], parseValue(type, value));
                requestPayload.put(keys[0], nestedMap);
            } else {
                requestPayload.put(key, parseValue(type, value));
            }
        }
    }
    @When("remove {string} from JSON params")
    public void removeParamFromPayload(String param) {
        // Remove the specified parameter from the payload
        if (param.contains(".")) {
            String[] keys = param.split("\\.");
            Map<String, Object> nestedMap = (Map<String, Object>) requestPayload.get(keys[0]);
            nestedMap.remove(keys[1]);
        } else {
            requestPayload.remove(param);
        }
    }

    @When("send POST request to {string}")
    public void sendPostRequest(String endpoint) {
        // Send the request
        response = request.body(requestPayload).post(endpoint);
    }

    @Then("json body must contain params:")
    public void verifyResponseBody(DataTable dataTable) {
        Map<String, String> expectedParams = dataTable.asMap(String.class, String.class);

        for (Map.Entry<String, String> entry : expectedParams.entrySet()) {
            String actualValue = response.jsonPath().getString(entry.getKey());
            Assert.assertEquals(actualValue, entry.getValue(), "Mismatch for key: " + entry.getKey());
        }
    }
}