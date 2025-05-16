package lk.binuri.util;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomErrorException extends RuntimeException {
    private final Map<String, String> errors;

    public CustomErrorException() {
        errors = new HashMap<>();
    }

    public void addError(String key, String msg) {
        errors.put(key, msg);
    }

    public boolean isEmpty() {
        return errors.isEmpty();
    }
}
