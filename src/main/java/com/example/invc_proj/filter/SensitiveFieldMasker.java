package com.example.invc_proj.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class SensitiveFieldMasker {

    // Configurable via application.yml
    @Value("${logging.mask.fields:password,token,secret,cvv,cardNumber,ssn,pin}")
    private List<String> sensitiveFields;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String mask(String json) {
        if (!StringUtils.hasText(json)) return json;
        try {
            JsonNode root = objectMapper.readTree(json);
            maskNode(root);
            return objectMapper.writeValueAsString(root);
        } catch (Exception e) {
            // Not valid JSON — return as-is (don't expose parse errors)
            return "[unparseable body]";
        }
    }

    @SuppressWarnings("unchecked")
    private void maskNode(JsonNode node) {
        if (node.isObject()) {
            ObjectNode obj = (ObjectNode) node;
            obj.fields().forEachRemaining(entry -> {
                if (isSensitive(entry.getKey())) {
                    obj.put(entry.getKey(), "***MASKED***");
                } else {
                    maskNode(entry.getValue());  // recurse into nested objects
                }
            });
        } else if (node.isArray()) {
            node.forEach(this::maskNode);
        }
    }

    private boolean isSensitive(String fieldName) {
        String lower = fieldName.toLowerCase();
        return sensitiveFields.stream().anyMatch(lower::contains);
    }
}
