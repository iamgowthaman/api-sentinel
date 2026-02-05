package utils;
import java.io.*;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import io.github.cdimascio.dotenv.Dotenv;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
public class ReUsableMethods {


    public static JsonPath rawToJson(String response) {

        JsonPath js = new JsonPath(response);
        return js;
    }


    public static String getJsonPath(Response response, String key) {
        String responseAsString = response.asString();
        JsonPath jsonPath = new JsonPath(responseAsString);
        return jsonPath.getString(key);
    }
    public static String getJsonPath(String response, String key) {
        JsonPath jsonPath = new JsonPath(response);
        return jsonPath.getString(key);
    }

    public static boolean convertStringToBoolean(String stringValue) {
        boolean booleanValue = Boolean.parseBoolean(stringValue);
        return booleanValue;

    }
    public static double convertStringToDouble(String stringValue) {
        double doubleValue = Double.parseDouble(stringValue);
        return doubleValue;

    }

    public static String getEpochTimestamp() {
        Instant instant = Instant.now();
        long epochTimestamp = instant.atZone(ZoneOffset.UTC).toEpochSecond();
        String stringTimestamp = Long.toString(epochTimestamp);
        return stringTimestamp;
    }

    public static String auth(){

        String encodedApiKey = Base64.getEncoder().encodeToString("testing".getBytes());
        return encodedApiKey = "Basic " + encodedApiKey;
    }

    public static String getTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(formatter);
        return timestamp;
    }

    public static String getEnvValue(String key) {

        Dotenv dotenv = Dotenv.load();
        String value = dotenv.get(key);
        return value;
    }

    public static String getGlobalValue(String key) {
        Map<String, String> properties = loadOrCreateProperties();
        return properties.get(key);
    }

    public static void setGlobalValue(String key , String value) {
        Map<String, String> properties = loadOrCreateProperties();
        updateProperty(properties, key, value);
        saveProperties(properties, "src/test/resources/global.properties");
    }

    private static Map<String, String> loadOrCreateProperties()  {
        File file = new File("src/test/resources/global.properties");
        Map<String, String> defaultProperties = new LinkedHashMap<>();
        if (file.exists()) {
            return loadProperties();
        } else {
            defaultProperties.put("baseUrl", "https://thinking-tester-contact-list.herokuapp.com/users");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                saveProperties(defaultProperties, "src/test/resources/global.properties"); //
            }// Save default properties to the file
            catch (IOException e) {
                throw new RuntimeException(e);
            }

            return defaultProperties;
        }
    }

    private static Map<String, String> loadProperties() {
        Map<String, String> properties = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/global.properties"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#") && line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    properties.put(parts[0], parts[1]);
                } else if (line.trim().isEmpty()) {
                    properties.put("", ""); // Add blank line to properties map
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load global.properties", e);
        }
        return properties;
    }

    private static void updateProperty(Map<String, String> properties, String key, String value) {
        properties.put(key, value);
    }
    private static void saveProperties(Map<String, String> properties, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("#Updated properties\n");
            writer.write("#" + new java.util.Date() + "\n");
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                if (!entry.getKey().isEmpty()) {
                    writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
                } else {
                    writer.write("\n"); // Write a blank line
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save global.properties", e);
        }
    }

    public static String generateRandomNumber(int length) {
        StringBuilder sb = new StringBuilder(length);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // Generates a random digit between 0 and 9
        }
        return sb.toString();
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public static String getBaseUrl() {

        String env = System.getProperty("env");

        if (env == null || env.isBlank()) {
            env = getEnvValue("ENV"); // .env
        }

        if (env == null || env.isBlank()) {
            env = "sandbox"; // safe default
        }

        String baseUrl = getGlobalValue("baseUrl." + env);
        System.err.println("baseUrl: " + baseUrl);
        System.err.println("env: " + env);

        if (baseUrl == null) {
            throw new RuntimeException("Base URL not configured for env: " + env);
        }
        return baseUrl;
    }
}
