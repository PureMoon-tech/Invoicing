package com.example.invc_proj.license;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot configuration for license management
 */
@Configuration
public class LicenseConfig {

    // Embed your public key in the application (from LicenseGenerator output)
    private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA44GJrbhslRJ/v7OaKwkABRvVWjkGWCJ6dAufdb0yuIvKPk0o/+7OSpGd60J0Up5BiSzEONgeOl/OlXrJBAAxjZ17gUtFm9rESC27dhezhTC2r1a5eZcXzu/6+/gN0nk4njZ5jn4nuXxNSevT7pndSNibMfYjou3F9FUEHYzCK+jbvbTi5IFCcmHhHemIEayCjFBHAJZ8610h9FFY+PDuuy7POSqbM/z6iN4/W2yonCeKXey2Yahyib8YvTtwQmcGY7+xyHvhdw7sN0qACfxYWUhT2AiFw0H+lCZZRh1UYZ4Y1FmD4CL6H7Ye7PQyMJao5e9oH7c0Cas/KqKb1c9i5wIDAQAB";

    @Bean
    public LicenseVerifier licenseVerifier() throws Exception {
        return new LicenseVerifier(PUBLIC_KEY);
    }
}