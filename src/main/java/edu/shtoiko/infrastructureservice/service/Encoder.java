package edu.shtoiko.infrastructureservice.service;

public interface Encoder {
    String generateKey();
    String encrypt(String plainText, String secretKeyString);
    String decrypt(String encryptedText, String secretKeyString);

}
