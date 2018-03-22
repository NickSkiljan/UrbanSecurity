package edu.osu.urban_security.security_app;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSA {
    private Cipher cipher1; private Cipher cipher2;
    private KeyPairGenerator generator;
    private KeyPair keyPair;
    private PrivateKey priKey;
    private PublicKey pubKey;

    byte[] encrypted;
    byte [] decrypted;


    public RSA(String text) throws Exception {
        // check text to make sure its valid
        if(text == null || text.length() == 0) {
            throw new Exception("Empty String");
        }
        // generateKeys
        generateKeys();
    }

    public void generateKeys() throws NoSuchAlgorithmException {
        generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        // generate key pair
        keyPair = generator.generateKeyPair();
        this.setPriKey(keyPair.getPrivate());
        this.setPubKey(keyPair.getPublic());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String encrypt(String text) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        // grab cipher  f
        cipher1 = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher1.init(Cipher.ENCRYPT_MODE,getPublicKey());
        //
        encrypted = cipher1.doFinal(text.getBytes());
        return  encrypted.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String decrypt(String text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher2 = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher2.init(Cipher.DECRYPT_MODE,getPrivateKey());
        decrypted = cipher2.doFinal(text.getBytes());
        return decrypted.toString();
    }

    public PublicKey getPublicKey(){
        return pubKey;
    }

    public PrivateKey getPrivateKey(){
        return priKey;
    }

    public void setPriKey(PrivateKey key){
        priKey = key;
    }

    public void setPubKey(PublicKey key){
        pubKey = key;
    }
}
