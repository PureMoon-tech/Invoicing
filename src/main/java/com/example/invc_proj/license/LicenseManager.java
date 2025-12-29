package com.example.invc_proj.license;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class LicenseManager {

    @Value("${license.key}")
    private String licenseKey;

    @Value("${license.check-on-login:true}")
    private boolean checkOnLogin;

    @Value("${license.periodic-check-hours:24}")
    private int periodicCheckHours;

    @Value("${license.warning-days:7}")
    private int warningDays;

    private final LicenseVerifier verifier;
    private LicenseVerifier.LicenseInfo licenseInfo;
    private LocalDateTime lastChecked;

    public LicenseManager(LicenseVerifier verifier) {
        this.verifier = verifier;
    }

    @PostConstruct
    public void validateLicense() {
        try {
            performValidation();
            System.out.println("✓ License validated successfully at startup");
            System.out.println("  Customer: " + licenseInfo.getCustomer());
            System.out.println("  Edition: " + licenseInfo.getEdition());
            System.out.println("  Valid until: " + licenseInfo.getExpiryDate());

        } catch (Exception e) {
            throw new RuntimeException(
                    "License validation failed: " + e.getMessage() +
                            ". Please contact support.", e
            );
        }
    }

    /**
     * Core validation logic
     */
    private void performValidation() throws Exception {
        licenseInfo = verifier.verifyLicense(licenseKey);
        lastChecked = LocalDateTime.now();
        System.out.println(licenseInfo.toString());

        if (licenseInfo.isExpired()) {
            throw new RuntimeException(
                    "License expired on " + licenseInfo.getExpiryDate() +
                            ". Please contact support to renew your license."
            );
        }
    }

    /**
     * Check license validity - call this on login or periodically
     */
    public  void checkLicense() throws Exception {
        performValidation();
    }

    /**
     * Check if periodic validation is needed
     */
    public boolean needsPeriodicCheck() {
        if (lastChecked == null) return true;
        long hoursSinceCheck = ChronoUnit.HOURS.between(lastChecked, LocalDateTime.now());
        return hoursSinceCheck >= periodicCheckHours;
    }

    /**
     * Validate license if periodic check is due
     */
    public void checkIfNeeded() {
        if (needsPeriodicCheck()) {
            try {
                checkLicense();
                System.out.println("✓ Periodic license check passed");
            } catch (Exception e) {
                throw new RuntimeException("License validation failed: " + e.getMessage(), e);
            }
        }
    }

    public LicenseVerifier.LicenseInfo getLicenseInfo()
    {
        return licenseInfo;
    }

    public  boolean shouldCheckOnLogin()
    {
        return checkOnLogin;
    }

    public boolean hasFeature(String edition)
    {
        String licenseEdition = licenseInfo.getEdition();
        return licenseEdition != null && licenseEdition.equalsIgnoreCase(edition);
    }

    public int getMaxUsers()
    {
        return licenseInfo.getMaxUsers();
    }
}