package com.openwes.core.utils;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Enumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xuanloc0511@gmail.com
 */
public final class KeystoreUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(KeystoreUtils.class);

    public final static void validKeystore(String keystorePath, String keystorePass, boolean passwordProtected) {
        try {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            try (FileInputStream fis = new FileInputStream(keystorePath)) {
                keystore.load(fis, keystorePass.toCharArray());
            }

            Enumeration<String> aliases = keystore.aliases();
            if (!aliases.hasMoreElements()) {
                throw new RuntimeException("Not found any alias in keystore that are valid");
            }
            while (aliases.hasMoreElements()) {
                String aliasName = aliases.nextElement();
                LOGGER.info("Validating keystore at {} with alias {}", keystorePath, aliasName);
                try {
                    if (passwordProtected) {
                        keystore.getEntry(aliasName, new KeyStore.PasswordProtection(keystorePass.toCharArray()));
                    } else {
                        keystore.getEntry(aliasName, null);
                    }
                    LOGGER.info("Found a valid alias {} in keystore {}", aliasName, keystorePath);
                    return;
                } catch (Exception e) {
                    LOGGER.warn("Validate keystore {} with alias {} fail: {}", keystorePath, aliasName, e.getMessage());
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public final static void validKeystore(String keystorePath, String keystorePass, String alias, boolean passwordProtected) {
        try {
            if (Validate.isEmpty(alias)) {
                LOGGER.info("Keystore alias is null or empty then if there are any valid alias then return ok");
                validKeystore(keystorePath, keystorePass, passwordProtected);
                return;
            }
            LOGGER.info("Validating keystore at {} with alias {}", keystorePath, alias);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            try (FileInputStream fis = new FileInputStream(keystorePath)) {
                keystore.load(fis, keystorePass.toCharArray());
            }

            if (passwordProtected) {
                keystore.getEntry(alias, new KeyStore.PasswordProtection(keystorePass.toCharArray()));
            } else {
                keystore.getEntry(alias, null);
            }
            LOGGER.info("Alias {} in keystore {} is valid", alias, keystorePath);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
