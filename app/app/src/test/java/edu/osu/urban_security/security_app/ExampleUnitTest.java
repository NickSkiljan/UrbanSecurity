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
    public void encryption_isCorrect() throws Exception {
       String message = "hello world";
       AES obj = new AES();
       SecretKey key = obj.generateKey();
       byte[] encryptedData = obj.encrypt(key,message.getBytes());
       byte[] decryptedData = obj.decrypt(key,encryptedData);
       assertEquals(message, decryptedData.toString());
    }
}