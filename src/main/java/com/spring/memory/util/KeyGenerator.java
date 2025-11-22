package com.spring.memory.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // generates a secure 256-bit key
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
