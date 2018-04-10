package edu.osu.urban_security.security_receiver_app;

/**
 * Created by nicks on 4/10/2018.
 */


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

public class AES {


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

    public static byte[] decrypt(SecretKey raw, byte[] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, raw);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

}