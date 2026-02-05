package stepDefinitions;

import actions.CommonApiActions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import resources.APIResources;
import utils.ReUsableMethods;
import utils.TestContext;

import java.io.Closeable;

import static org.testng.Assert.assertEquals;
import static org.testng.FileAssert.fail;

public class CommonStepDefinitions extends BaseStepDefinitions {


    @When("User calls {string} with {string} http request")
    public void user_calls_with_http_request(String resource, String method) {
        APIResources resourceAPI = APIResources.valueOf(resource);
        context.setEndPoint(resourceAPI.getResource());
        CommonApiActions.callApi(context, resource, context.getEndPoint(), method);
    }

    @Then("the API call is successful with status code {int}")
    public void the_api_call_is_successful_with_status_code(Integer statusCode) {
        assertEquals(Integer.valueOf(context.getResponse().getStatusCode()), statusCode);
    }

    @Then("verify the response as {string}")
    public void verify_the_response_as(String message) {
        String responseMessage = context.getResponse().body().asString();
        assertEquals(responseMessage, "\"" + message + "\"");
    }

    @Then("verify {string} value is {string} in response")
    public void verify_value_is_in_response(String keyValue, String expectedValue) throws JsonProcessingException {
        Object actualValue =
                ReUsableMethods.getJsonPath(context.getResponse(), keyValue);
        assertEquals(
                String.valueOf(actualValue),
                expectedValue,
                "Mismatch for key: " + keyValue
        );
    }
}
