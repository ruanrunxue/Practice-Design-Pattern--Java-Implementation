package db.dsl;

import java.util.LinkedHashMap;
import java.util.Map;

public class Result {
    private final Map<String, String> results;

    private Result() {
        this.results = new LinkedHashMap<>();
    }

    public static Result empty() {
        return new Result();
    }

    public Result add(String column, String value) {
        results.put(column, value);
        return this;
    }

    public Map<String, String> toMap() {
        return results;
    }
}
