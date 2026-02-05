package utils;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.io.*;
import java.util.Properties;

public class SpecBuilders {


    private static final ThreadLocal<PrintStream> THREAD_LOG =
            ThreadLocal.withInitial(() -> {
                try {
                    return new PrintStream(
                            new FileOutputStream(
                                    "log-" + Thread.currentThread().getId() + ".txt"
                            )
                    );
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });


    static {
        try {
            ensureEnvFileExists();
        } catch (IOException e) {
            e.printStackTrace();

        }

}



    public static RequestSpecification addCustomHeaders(RequestSpecification request)
            throws IOException {

        RequestSpecification updatedRequest = request;
        String featureValue = ReUsableMethods.getGlobalValue("CUSTOM_FEATURE_HEADER");
        if (featureValue != null && !featureValue.trim().isEmpty()) {

            updatedRequest = request.header("x-feature", featureValue);
        }

        return updatedRequest;
    }

    private static void ensureEnvFileExists() throws IOException {
        String envFilePath = ".env";
        File file = new File(envFilePath);
        if (!file.exists()) {
            file.createNewFile();
            saveEmptyPropertiesToFile(envFilePath);
        }
    }

    private static void saveEmptyPropertiesToFile(String filePath) {
        try (FileOutputStream output = new FileOutputStream(filePath)) {
            new Properties().store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RequestSpecification authorization(String auth)
            throws IOException {
        RequestSpecification requestSpec = null;
        if (auth.equalsIgnoreCase("basicauth")) {
            requestSpec = requestWithToken();
        } else if (auth.equalsIgnoreCase("InvaldiApiKey")) {
            requestSpec = requestWithInvalidToken();

        }
        return requestSpec;
    }

    private RequestSpecification requestWithInvalidToken() {
        return new RequestSpecBuilder().setBaseUri(ReUsableMethods.getBaseUrl())
                .addFilter(new AllureRestAssured())
                .addFilter(new CurlLogging(TestContext.get()))
                .addFilter(RequestLoggingFilter.logRequestTo(THREAD_LOG.get()))
                .addFilter(ResponseLoggingFilter.logResponseTo(THREAD_LOG.get()))
                .setContentType(ContentType.JSON)
                .build();
    }


    public RequestSpecification requestWithToken() {
        return new RequestSpecBuilder().setBaseUri(ReUsableMethods.getBaseUrl())
                .addFilter(new AllureRestAssured())
                .addFilter(new CurlLogging(TestContext.get()))
                .addFilter(RequestLoggingFilter.logRequestTo(THREAD_LOG.get()))
                .addFilter(ResponseLoggingFilter.logResponseTo(THREAD_LOG.get()))
                .setContentType(ContentType.JSON)
                .build();
    }
    }
