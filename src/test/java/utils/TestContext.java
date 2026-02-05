package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


@Getter
@Setter
public class TestContext {
    private static final ThreadLocal<TestContext> CONTEXT =
            ThreadLocal.withInitial(TestContext::new);

    private final ObjectManager objectManager;
    private final ObjectMapper objectMapper;
    private String curlCommand;
    private UUID scenarioId;

    private RequestSpecification request;
    private Response response;
    private String endPoint;
    private String featureName;
    private String scenarioName;
    private String apiName;
    private long startTime;
    private long endTime;

    private final Map<String, String> metrics = new LinkedHashMap<>();


    public static TestContext get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public TestContext() {
        this.objectManager = new ObjectManager();
        this.objectMapper = new ObjectMapper();
    }
}
