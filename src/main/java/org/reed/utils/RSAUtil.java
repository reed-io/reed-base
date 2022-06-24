/**
 * E5Projects#org.reed.utils/RSAUtil.java
 */
package org.reed.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * @author chenxiwen
 * @date 2020-12-29 10:33:38
 */
public final class RSAUtil {

    public static final String ALGORITHM = "RSA";

    public static final int KEY_SIZE = 1024;

    private final static KeyPairGenerator keyPairGenerator;

    private final static byte[] publicKeyBytes;

    private final static byte[] privateKeyBytes;

    static{
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            publicKeyBytes = keyPair.getPublic().getEncoded();
            privateKeyBytes = keyPair.getPrivate().getEncoded();
//            KeyFactory.getInstance(ALGORITHM).
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("RSA Algorithm not found within Runtime Environment!");
        } finally {

        }
    }

    protected static String getPublicKey(){
        return Base64Util.encode(publicKeyBytes);
    }

    protected static String getPrivateKey(){
        return Base64Util.encode(privateKeyBytes);
    }

    public static String encrypt(byte[] content){

        return null;
    }

    public static String decrypt(byte[] content){

        return null;
    }

    public static void main(String[] args) {
        System.out.println(RSAUtil.getPublicKey());
    }


}
