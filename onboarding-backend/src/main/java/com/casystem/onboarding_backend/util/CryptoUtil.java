package com.casystem.onboarding_backend.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CryptoUtil {

    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "C4S1st3m4sOnb04rd1ngPr0g12345678";

    public static String encrypt(String value) {
        if (value == null)
            return null;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error", e);
        }
    }

    public static String decrypt(String encryptedValue) {
        if (encryptedValue == null)
            return null;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedValue);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error", e);
        }
    }
}