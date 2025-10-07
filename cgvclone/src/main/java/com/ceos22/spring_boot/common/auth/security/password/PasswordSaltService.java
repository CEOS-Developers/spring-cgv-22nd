package com.ceos22.spring_boot.common.auth.security.password;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class PasswordSaltService {

    private final Pbkdf2Props props;
    private final SecureRandom secureRandom = new SecureRandom();

    public PasswordSaltService(Pbkdf2Props props) { this.props = props; }

    public String generateSaltBase64() {
        byte[] salt = new byte[props.saltBytes()];
        secureRandom.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public String hashBase64(String rawPassword, String saltBase64) {
        try {
            byte[] salt = Base64.getDecoder().decode(saltBase64);
            PBEKeySpec spec = new PBEKeySpec(
                    rawPassword.toCharArray(),
                    salt,
                    props.iterations(),
                    props.keyLength()
            );
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] key = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(key);
        } catch (Exception e) {
            throw new IllegalStateException("PBKDF2 hashing failed", e);
        }
    }

    public boolean matches(String rawPassword, String saltBase64, String storedBase64) {
        String computed = hashBase64(rawPassword, saltBase64);
        // constant-time 비교
        return constantTimeEquals(storedBase64, computed);
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        byte[] x = a.getBytes(); byte[] y = b.getBytes();
        if (x.length != y.length) return false;
        int r = 0; for (int i=0;i<x.length;i++) r |= x[i]^y[i];
        return r == 0;
    }

    @Component
    @ConfigurationProperties(prefix = "security.password.pbkdf2")
    public static class Pbkdf2Props {
        private int iterations = 120000;
        private int keyLength = 256;
        private int saltBytes = 16;

        public int iterations() { return iterations; }
        public int keyLength() { return keyLength; }
        public int saltBytes() { return saltBytes; }

        public void setIterations(int i) { this.iterations = i; }
        public void setKeyLength(int k) { this.keyLength = k; }
        public void setSaltBytes(int s) { this.saltBytes = s; }
    }
}
