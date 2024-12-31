package helloworld.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import helloworld.security.utils.SQLInjectionValidator;
import software.amazon.lambda.powertools.tracing.Tracing;

import java.util.HashMap;
import java.util.Map;

public class JsonProcessorService {


    private final ObjectMapper objectMapper = new ObjectMapper();



    @Tracing
    public String getNameReport(Map<String,Object> input) {
        String name = "";
        Object nameObj = input.get("name");
        if(nameObj instanceof String){
            name = (String) nameObj;
        }
        return name;
    }

    @Tracing
    public Map<String,String> getQueryParams(Map<String,Object> input) {
        Map<String,String> result = new HashMap<>();

        Object queryParamsObj = input.get("queryParams");

        if(queryParamsObj instanceof  Map){
            Map<String,Object> queryParams = (Map<String, Object>) queryParamsObj;

            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                String k = entry.getKey();
                if(k.startsWith("R_")){
                    String key = "${"+k.substring(2)+"}";
                    String value = entry.getValue().toString();
                    SQLInjectionValidator.validateSQLInput(value);
                    result.put(key, value != null ? value : "");
                }
            }
        }
        System.out.println("Query Params: " + result.toString());
        return result;
    }



}

