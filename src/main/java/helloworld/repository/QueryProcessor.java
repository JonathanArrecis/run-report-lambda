package helloworld.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QueryProcessor {

    private final String processedQuery;
    private final List<String> orderedParameters;

    public QueryProcessor(String originalQuery){
        this.orderedParameters  = new LinkedList<>();
        this.processedQuery = processQuery(originalQuery);
    }

    private String processQuery(String query){
        Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(query);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String parameter = matcher.group(1);
            this.orderedParameters.add(parameter);
            matcher.appendReplacement(sb, "?");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public String getProcessedQuery(){
        return this.processedQuery;
    }

    public List<String> getOrderedParameters(){
        return this.orderedParameters;
    }


}