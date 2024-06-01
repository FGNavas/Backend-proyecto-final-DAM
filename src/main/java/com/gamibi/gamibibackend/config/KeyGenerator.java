package com.gamibi.gamibibackend.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class KeyGenerator {
public String generateKey(){
    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    byte[] encodedKey = key.getEncoded();
    String base64EncodedKey = Base64.getEncoder().encodeToString(encodedKey);
    return base64EncodedKey;

}


}
