package edu.osu.urban_security.security_app;

import org.junit.Test;

import javax.crypto.SecretKey;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
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
        byte[] messageToByte = message.getBytes();
        byte[] encryptedData = obj.encrypt(key,messageToByte);
        byte[] decryptedData = obj.decrypt(key,encryptedData);
        String decryptedMessage = new String(decryptedData);
        assertEquals(message, decryptedMessage);
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
        byte[] encryptedData = obj.encrypt(key,message.getBytes());
        byte[] decryptedData = obj.decrypt(key,encryptedData);
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

//    @Test
//    public void RSA_encryption_isCorrect() throws Exception {
//        String message = "goodbye";
//        RSA encryption = new RSA(message);
//        String encryptedMessage = encryption.encrypt(message);
//        String decryptedMessage = encryption.decrypt(message);
//        assertEquals(decryptedMessage, message);
//    }
}