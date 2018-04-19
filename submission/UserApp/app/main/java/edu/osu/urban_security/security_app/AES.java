package edu.osu.urban_security.security_app;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
/**
 * Authors: Andrew and Joe on 4/10/2018.
 * Class allows us to generate an AES key and encrypt and decrypt with it. Also allows us convert
 * the data to a Firebase friendly format and back.
 */
public class AES {

    // Static encoding of a SecretKey generated by AES.java. This serves as a static AES key for
    //  encryption and decryption
    public static byte[] AESSecretKeyInBytes = {-34, 86, -34, 74, 119, 119, -64, -46, 86, 93, 55, -117, 52, -19, 4, 94};


    public SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        Provider provide = generator.getProvider();
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        // rand not random at all
        // TODO::generate seed based on floor of X minutes from internal clock
        sr.setSeed((long) 12.2);
        generator.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = generator.generateKey();
        return skey;
    }

    public static byte[] encrypt(SecretKey raw, byte[] clear) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, raw);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    public static String encryptToString(SecretKey raw, byte[] clear) throws Exception {
        return bytesToHex(encrypt(raw, clear));
    }

    public static byte[] decrypt(SecretKey raw, byte[] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, raw);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String decryptString(SecretKey raw, String encrypted) throws Exception {
        return new String(decrypt(raw, hexStringToByteArray(encrypted)));
    }

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

}