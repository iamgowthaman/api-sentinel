package utils;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class AllureLogger {

    // Attach request details to Allure
    public static void attachRequest(String apiName, RequestSpecification request, TestContext context) {
        if (!(request instanceof FilterableRequestSpecification)) {
            Allure.addAttachment(
                    apiName + " - Request",
                    "Request specification is not filterable"
            );
            return;
        }
        FilterableRequestSpecification req =
                (FilterableRequestSpecification) request;
        String requestBody = req.getBody() != null
                ? req.getBody().toString()
                : "No Body (GET Request or Empty)";
        String requestHeaders = req.getHeaders() != null
                ? req.getHeaders().toString()
                : "No Headers (GET Request or Empty)";
        String requestDetails = String.format("API: %s\nHeaders: %s\nRequest Body: %s",
                apiName, requestHeaders, requestBody);

        attach(
                apiName + " - Request",
                requestDetails
        );
        attachCurl(apiName, context);
    }

    public static void attachCurl(String apiName, TestContext context) {
        String curl = context.getCurlCommand();
        attach(
                apiName + " - cURL Command",
                curl != null && !curl.isEmpty()
                        ? curl
                        : "No cURL command available"
        );
    }

    // Attach response details to Allure
    public static void attachResponse(String apiName, Response response) {
        String responseDetails = String.format(
                "Status Code: %d%n%nResponse Body:%n%s",
                response.getStatusCode(),
                response.getBody().asString()
        );

        attach(apiName + " - Response", responseDetails);
        attachResponseHeader(apiName, response);
    }

    // Attach response headers to Allure
    public static void attachResponseHeader(String apiName, Response response) {
        String responseId = response.getHeader("x-response-id");
        String headerInfo = responseId != null
                ? "x-response-id: " + responseId
                : "x-response-id header not present";

        attach(apiName + " - Response x-response-id", headerInfo);
    }

    // Common attachment helper
    private static void attach(String name, String content) {
        Allure.addAttachment(
                name,
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))
        );
    }
}
