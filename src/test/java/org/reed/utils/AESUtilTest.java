/**
 * E5Projects#org.reed.utils/AESUtilTest.java
 */
package org.reed.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * @author chenxiwen
 * @date 2021-04-22 10:40:50
 */
public class AESUtilTest {

    public static void main(String[] args) {
        String k = "ender";

        byte[] karr = null;
        //            karr = "12345678901234561234567890123456".getBytes("UTF-8");
        karr = StringUtil.md5(k).toUpperCase().getBytes(StandardCharsets.UTF_8);
        final SecretKey secretKey = new SecretKeySpec(karr, "AES");
        Key key = new Key() {
            @Override
            public String getAlgorithm() {
                return secretKey.getAlgorithm();
            }

            @Override
            public String getFormat() {
                return secretKey.getFormat();
            }

            @Override
            public byte[] getEncoded() {
                return secretKey.getEncoded();
            }
        };

        String test = "asdfasdfasdfasdfasdfasdf";
        String test1 = "2ae7727cf98096fd9412ed08c7fb94914526e504987f4c4a956cf3b3ca107246";
        try {
            byte[] decrypt = AESUtil.encrypt(test.getBytes(), key);
            byte[] encrypt = AESUtil.decrypt(StringUtil.decodeHex(test1), key);
            System.out.println(StringUtil.encodeHexString(decrypt));
            System.out.println(new String(encrypt));
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
}
