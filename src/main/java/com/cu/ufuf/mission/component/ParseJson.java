package com.cu.ufuf.mission.component;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ParseJson {

    public int toInt(String paramName, String jsonString) {
        try {
            if (jsonString != null && !jsonString.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonString);
    
                // Check if the specified field exists
                if (jsonNode.has(paramName)) {
                    return jsonNode.get(paramName).asInt();
                } else {
                    // Handle the case where the specified field is missing
                    // You might want to throw an exception or return a default value
                    throw new IllegalArgumentException("Missing '" + paramName + "' field in JSON");
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Consider logging the exception instead of just printing it
        }
        throw new IllegalArgumentException("Invalid JSON input");
    }
}
