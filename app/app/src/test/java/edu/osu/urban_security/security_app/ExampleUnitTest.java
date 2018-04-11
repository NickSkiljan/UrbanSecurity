package edu.osu.urban_security.security_app;


import org.junit.Test;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

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


        System.out.println(java.util.Arrays.toString(keyBytes));
        byte[] newKeyBytes = {-34, 86, -34, 74, 119, 119, -64, -46, 86, 93, 55, -117, 52, -19, 4, 94};
        SecretKey newKey = new SecretKeySpec(newKeyBytes, 0, keyBytes.length, "AES");
        assertEquals(java.util.Arrays.toString(keyBytes),java.util.Arrays.toString(newKey.getEncoded()) );

        byte[] messageToByte = message.getBytes();
        byte[] encryptedData = obj.encrypt(key,messageToByte);
//        String encMsg = new String(encryptedData);
        String encMsg = bytesToHex(encryptedData);

        byte[] decryptedData = obj.decrypt(key,hexStringToByteArray(encMsg));
        String decryptedMessage = new String(decryptedData);

        System.out.println(encMsg);
        System.out.println(encMsg.length());
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
    /*
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
//        String keyString = new String(encryptedKey, "Latin-1");
//        String keyString = encoder.encodeToString(encryptedKey);

//        String keyString = Base64.encodeToString(encryptedKey, Base64.NO_WRAP);
//        System.err.println(keyString == null);
//        String keyString = Base64.encodeBase64String(encryptedKey);


//        String keyString = new String(encryptedKey, "ISO_8859_1");
        // Decrypt AES key_string using RSA key
//        byte[] keyStringGetBytes = Base64.decodeBase64(keyString);
//        byte[] keyStringGetBytes = Base64.decode(keyString, Base64.NO_WRAP);
//        byte[] keyStringGetBytes = decoder.decode(keyString);
        byte[] decryptedKey = encryption.decrypt(keyStringGetBytes);

        assertEquals(new String(encodedKey), new String(decryptedKey));
    }
    */

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

//    @Test
//    public void RSA_encryption_isCorrect() throws Exception {
//        String message = "goodbye";
//        RSA encryption = new RSA(message);
//        String encryptedMessage = encryption.encrypt(message);
//        String decryptedMessage = encryption.decrypt(message);
//        assertEquals(decryptedMessage, message);
//    }
}