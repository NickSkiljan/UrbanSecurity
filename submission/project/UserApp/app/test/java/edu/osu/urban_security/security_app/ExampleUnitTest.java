package edu.osu.urban_security.security_app;


import org.junit.Test;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static org.junit.Assert.*;

/**
 * Authors: Andrew, Joe, Sunny
 * Unit tests for Encryption
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void AES_encryption_isCorrect_1letter() throws Exception {
        String message = "a";
        AES obj = new AES();
        SecretKey key = obj.generateKey();
        byte[] keyBytes = key.getEncoded();

        byte[] messageToByte = message.getBytes();
        byte[] encryptedData = obj.encrypt(key,messageToByte);
//        String encMsg = new String(encryptedData);
        String encMsg = AES.bytesToHex(encryptedData);

        byte[] decryptedData = obj.decrypt(key,AES.hexStringToByteArray(encMsg));
        String decryptedMessage = new String(decryptedData);

        System.out.println(encMsg);
        System.out.println(encMsg.length());
        assertEquals(message, decryptedMessage);
    }

    @Test
    public void Convert_Key_to_Bytes_and_Back() throws Exception {
        String message = "Maxwell";
        AES obj = new AES();
        SecretKey key = obj.generateKey();
        byte[] keyBytes = key.getEncoded();


        System.out.println(java.util.Arrays.toString(keyBytes));
        byte[] newKeyBytes = {-34, 86, -34, 74, 119, 119, -64, -46, 86, 93, 55, -117, 52, -19, 4, 94};
        SecretKey newKey = new SecretKeySpec(newKeyBytes, 0, keyBytes.length, "AES");
        assertEquals(java.util.Arrays.toString(keyBytes),java.util.Arrays.toString(newKey.getEncoded()) );
    }

    @Test
    public void AES_encryption_isCorrect_2letters() throws Exception {
        String message = "ab";
        AES obj = new AES();
        SecretKey key = obj.generateKey();
        byte[] encryptedData = obj.encrypt(key,message.getBytes());
        byte[] decryptedData = obj.decrypt(key,encryptedData);
        String decryptedMessage = new String(decryptedData);
        assertEquals(message, decryptedMessage);
    }

    @Test
    public void AES_encryption_isCorrect_2words() throws Exception {
        String message = "hello world";
        AES obj = new AES();
        SecretKey key = obj.generateKey();
        byte[] encryptedData = AES.encrypt(key,message.getBytes());
        byte[] decryptedData = AES.decrypt(key,encryptedData);
        String decryptedMessage = new String(decryptedData);
        assertEquals(message, decryptedMessage);
    }

    @Test
    public void RSA_encryption_isCorrect() throws Exception {
        AES obj = new AES();
        SecretKey key = obj.generateKey();
        byte [] e = key.getEncoded();

        RSA encryption = new RSA();
        byte[] encryptedMessage = encryption.encrypt(e);
        byte[] decryptedMessage = encryption.decrypt(encryptedMessage);
        String key_string = new String(e);
        String decrypt_key = new String(decryptedMessage);
        assertEquals(key_string, decrypt_key);
    }

    @Test
    public void RSA_encryption_of_AES() throws Exception {
        AES aes = new AES();
        // AES key
        SecretKey key = aes.generateKey();
        byte [] encodedKey = key.getEncoded();
        // RSA key to encrypt AES key
        RSA encryption = new RSA();
        // Encrypted AES key using RSA key
        byte[] encryptedKey = encryption.encrypt(encodedKey);
        byte[] decryptedKey = encryption.decrypt(encryptedKey);

        assertEquals(new String(encodedKey), new String(decryptedKey));
    }

    @Test
    public void RSA_encryption_of_AES_then_encrypt() throws Exception {
        AES aes = new AES();
        // AES key
        SecretKey key = aes.generateKey();
        byte [] encodedKey = key.getEncoded();
        // RSA key to encrypt AES key
        RSA encryption = new RSA();
        // Encrypted AES key using RSA key
        byte[] encryptedKey = encryption.encrypt(encodedKey);

        String keyString = new String(encryptedKey, "ISO_8859_1");
        // Decrypt AES key_string using RSA key
        byte[] decryptedKey = encryption.decrypt(keyString.getBytes("ISO_8859_1"));


        SecretKey sK = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, "AES");
        byte[] expected = AES.encrypt(key, "test".getBytes());
        byte[] result = AES.encrypt(sK, "test".getBytes());
        assertEquals(new String(expected), new String(result));
    }
}