package edu.shtoiko.infrastructureservice.service.implementation;

import edu.shtoiko.infrastructureservice.service.Encoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class EncoderImpl implements Encoder {

    @Value("${encoder.keysize}")
    private int keySize;

    @Value("${encoder.algorithm}")
    private String algorithm;

    public String generateKey() {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public String encrypt(String plainText, String secretKeyString){
        byte[] encryptedBytes = new byte[0];
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, convertStringToSecretKey(secretKeyString));
            encryptedBytes = cipher.doFinal(plainText.getBytes());
        } catch (Exception e){
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedText, String secretKeyString){
        byte[] decryptedBytes = new byte[0];
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, convertStringToSecretKey(secretKeyString));
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            decryptedBytes = cipher.doFinal(decodedBytes);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new String(decryptedBytes);
    }

    private SecretKey convertStringToSecretKey(String secretKeyString){
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyString);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, algorithm);
    }

}
