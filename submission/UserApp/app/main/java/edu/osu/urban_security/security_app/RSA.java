package edu.osu.urban_security.security_app;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
/**
 * Authors: Andrew and Joe on 4/10/2018.
 * Class allows us to generate an RSA key and encrypt and decrypt with it.
 */
public class RSA {
    private Cipher cipher1; private Cipher cipher2;
    private KeyPairGenerator generator;
    private KeyPair keyPair;



    public RSA() throws Exception {
        // generateKeys
        generateKeys();
    }

    public void generateKeys() throws NoSuchAlgorithmException {
        generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        // generate key pair
        keyPair = generator.generateKeyPair();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public byte[] encrypt(byte[] text) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        // grab cipher  f
        //cipher1 = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        //cipher1.init(Cipher.ENCRYPT_MODE,keyPair.getPublic());
        cipher1.init(Cipher.PUBLIC_KEY, keyPair.getPublic());

        byte[] encrypted = cipher1.doFinal(text);
        return  encrypted;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public byte [] decrypt(byte[] text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        //cipher2 = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher2.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
//        InputStream in = new ByteArrayInputStream(text);
//        byte[] inputBuffer = new byte[ text.length ];
//
//        int r = in.read(inputBuffer);
//        while ( r >= 0 ) {
//            byte[] outputUpdate = cipher2.update( inputBuffer, 0, r );
//            r = in.read(inputBuffer);
//        }
//        byte[] outputFinalUpdate = cipher2.doFinal();

        byte [] decrypted = cipher2.doFinal(text);
        return decrypted;
    }

}
