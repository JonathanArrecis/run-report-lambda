package helloworld.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.lambda.powertools.tracing.Tracing;

import java.util.HashMap;
import java.util.Map;

public class JsonProcessorService {


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Tracing
    public String getNameReport(String input) {
        String name = "";
        try {
            JsonNode jsonNode = objectMapper.readTree(input);
            if(jsonNode.has("name")){
                name = jsonNode.get("name").asText();
            }

        }catch (Exception e){
            throw new RuntimeException("Error parsing input", e);
        }

        return name;
    }

    @Tracing
    public Map<String, String> getQueryParams(String input) {
        Map<String, String> queryParams = new HashMap<>();
        try {
            JsonNode jsonNode = objectMapper.readTree(input);
            if(jsonNode.has("queryParams")){
                JsonNode queryParamsNode = jsonNode.get("queryParams");
                queryParams = objectMapper.convertValue(queryParamsNode, Map.class);
            }

        }catch (Exception e){
            throw new RuntimeException("Error parsing input", e);
        }

        return queryParams;
    }


}
