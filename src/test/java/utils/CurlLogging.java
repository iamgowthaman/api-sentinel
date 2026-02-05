package utils;


import java.util.Objects;
import java.util.logging.LogRecord;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class CurlLogging implements Filter {

    TestContext context;
    public CurlLogging(TestContext context) {
        this.context = context;
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        logRequestDetails(requestSpec);

        // Proceed with the request and capture the response

        Response response = ctx.next(requestSpec, responseSpec);

        logResponseDetails(response);
        return response;
    }


    private void logRequestDetails(FilterableRequestSpecification requestSpec) {

        String method = requestSpec.getMethod();
        String uri = requestSpec.getURI();
        Headers headers = requestSpec.getHeaders();
        String body = Objects.toString(requestSpec.getBody());

        // ✅ Construct the cURL command
        String curlCommand = "curl -X " + method + " \"" + uri + "\"";
        for (io.restassured.http.Header header : headers) {
            curlCommand += " -H \"" + header.getName() + ": " + header.getValue() + "\"";
        }
        if (body != null && !body.isEmpty() && !"null".equals(body)) {
            curlCommand += " -d '" + body + "'";
        }

        context.setCurlCommand(curlCommand);
        System.out.println("Captured cURL Command:");
        System.out.println(curlCommand);
    }
    private void logResponseDetails(Response response) {
        int statusCode = response.getStatusCode();
        Headers headers = response.getHeaders();
        String body = response.getBody().asString();
    }
//

}
