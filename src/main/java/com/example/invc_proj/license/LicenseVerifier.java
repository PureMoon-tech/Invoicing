package com.example.invc_proj.license;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.security.*;

/**
 * Verifies licenses offline using the public key
 */
public class LicenseVerifier {

    private static final String ALGORITHM = "RSA";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private final PublicKey publicKey;

    public LicenseVerifier(String publicKeyBase64) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
        this.publicKey = kf.generatePublic(spec);
    }



    /**
     * Verify and parse a license key
     */
    public LicenseInfo verifyLicense(String licenseKey) throws Exception {
        String[] parts = licenseKey.split("\\.");
        if (parts.length != 2) {
            throw new SecurityException("Invalid license format");
        }

        byte[] payloadBytes = Base64.getDecoder().decode(parts[0]);
        byte[] signatureBytes = Base64.getDecoder().decode(parts[1]);

        // Verify signature
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(payloadBytes);

        if (!sig.verify(signatureBytes)) {
            throw new SecurityException("Invalid license signature");
        }

        // Parse claims
        String payload = new String(payloadBytes);
        Map<String, String> claims = new HashMap<>();
        for (String claim : payload.split("\\|")) {
            String[] kv = claim.split("=", 2);
            if (kv.length == 2) {
                claims.put(kv[0], kv[1]);
            }
        }

        return new LicenseInfo(claims);
    }
    /**
     * Container for license information
     */
    public static class LicenseInfo {
        private final Map<String, String> claims;

        public LicenseInfo(Map<String, String> claims) {
            this.claims = claims;
        }

        public String get(String key) {
            return claims.get(key);
        }

        public boolean isExpired() {
            String expiry = claims.get("expiry");
            if (expiry == null) return false;

            LocalDate expiryDate = LocalDate.parse(expiry, DATE_FORMAT);
           // System.out.println(expiryDate);
            return LocalDate.now().isAfter(expiryDate);
        }

        public LocalDate getExpiryDate() {
            String expiry = claims.get("expiry");
            return expiry != null ? LocalDate.parse(expiry, DATE_FORMAT) : null;
        }

        public String getCustomer() {
            return claims.get("customer");
        }

        public String getEdition() {
            return claims.get("edition");
        }

        public int getMaxUsers() {
            String max = claims.get("maxUsers");
            return max != null ? Integer.parseInt(max) : 0;
        }

        public Map<String, String> getAllClaims() {
            return new HashMap<>(claims);
        }

        @Override
        public String toString() {
            return "LicenseInfo{" + claims + "}";
        }
    }
}
