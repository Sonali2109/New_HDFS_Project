package com.example.hdfs.service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.*;

public class EncryptionUtil {
    private static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";
    private static final String AES_ALGORITHM = "AES";
    private static final int RSA_KEY_SIZE = 2048;
    private static final int AES_KEY_SIZE = 256;

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyGen.initialize(RSA_KEY_SIZE);
        return keyGen.generateKeyPair();
    }

    public static byte[] encrypt(byte[] data, PublicKey publicKey) throws Exception {
        // Generate a random AES key
        KeyGenerator keyGen = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGen.init(AES_KEY_SIZE, SecureRandom.getInstanceStrong());
        SecretKey aesKey = keyGen.generateKey();

        // Encrypt the data with the AES key
        Cipher aesCipher = Cipher.getInstance(AES_ALGORITHM);
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encryptedData = aesCipher.doFinal(data);

        // Encrypt the AES key with the RSA public key
        Cipher rsaCipher = Cipher.getInstance(RSA_ALGORITHM);
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedAesKey = rsaCipher.doFinal(aesKey.getEncoded());

        // Combine the encrypted AES key and the encrypted data
        ByteBuffer byteBuffer = ByteBuffer.allocate(encryptedAesKey.length + encryptedData.length + 4);
        byteBuffer.put(encryptedAesKey);
        byteBuffer.put(encryptedData);
        byteBuffer.putInt(encryptedAesKey.length); // Store the length of the encrypted AES key

        return byteBuffer.array();
    }

    public static byte[] decrypt(byte[] encryptedData, PrivateKey privateKey) throws Exception {
        // Read the length of the encrypted AES key
        ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedData);
        int aesKeyLength = byteBuffer.getInt(encryptedData.length - 4);

        // Extract the encrypted AES key and the encrypted data
        byte[] encryptedAesKey = new byte[aesKeyLength];
        byte[] encryptedDataBytes = new byte[encryptedData.length - aesKeyLength - 4];
        byteBuffer.get(encryptedAesKey);
        byteBuffer.get(encryptedDataBytes);

        // Decrypt the AES key with the RSA private key
        Cipher rsaCipher = Cipher.getInstance(RSA_ALGORITHM);
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] aesKeyBytes = rsaCipher.doFinal(encryptedAesKey);
        SecretKey aesKey = new SecretKeySpec(aesKeyBytes, AES_ALGORITHM);

        // Decrypt the data with the AES key
        Cipher aesCipher = Cipher.getInstance(AES_ALGORITHM);
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
        return aesCipher.doFinal(encryptedDataBytes);
    }
}
