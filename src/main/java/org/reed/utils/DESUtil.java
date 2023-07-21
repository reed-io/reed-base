/**
 * base/org.reed.utils/DESUtil.java
 */
package org.reed.utils;

import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * @author chenxiwen
 * @date 2017年12月19日上午9:24:25
 */
public final class DESUtil {

    public static final String DEFAULT_SECURITY_CODE = "123456781234567812345678123456781234567812345678";

    /**
     * 加密
     * 
     * @param datasource
     *            byte[]
     * @param password
     *            String
     * @return byte[]
     */
    public static byte[] encrypt(byte[] datasource, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            // 现在，获取数据并加密
            // 正式执行加密操作
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * 
     * @param src
     *            byte[]
     * @param password
     *            String
     * @return byte[]
     * @throws InvalidKeyException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeySpecException 
     * @throws NoSuchPaddingException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     */
    public static byte[] decrypt(byte[] src, String password) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }

    /**
     * @param args
     */
//    public static void main(String[] args) {
//        // TODO Auto-generated method stub
//        byte[] result = DESUtil.encrypt(text.getBytes(), key);
//        String afterHex = StringUtilTest.encodeHexString(result);
//        System.out.println("加密后：" + afterHex);
//
//        // 直接将如上内容解密
//        try {
//            byte[] decryResult = StringUtilTest.decodeHex(afterHex);
//            decryResult = DESUtil.decrypt(decryResult, key);
//            System.out.println("解密后：" + new String(decryResult));
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//        
//        String s = "^2222f27cfecd13bac6499537ddedf47e40a91593270e66ef28901c47e3dab2f87d91c38a3feecae9".substring(1);
//        System.out.println(s);
//        try {
//            byte[] decryResult = StringUtilTest.decodeHex(s);
//            decryResult = DESUtil.decrypt(decryResult, key);
//            System.out.println("test：" + new String(decryResult));
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//    }

    /**
     * 3DES加密
     *
     * @param keybyte    密钥，24位
     * @param srcStr 将加密的字符串
     * @return lee on 2017-08-09 10:51:44
     */
    public static String encode3Des(byte[] keybyte, String srcStr) {
        byte[] src = srcStr.getBytes();
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, "DESede");
            //加密
            Cipher c1 = Cipher.getInstance("DESede");
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            BASE64Encoder encoder = new BASE64Encoder();
            String pwd = encoder.encode(c1.doFinal(src));
//           return c1.doFinal(src);//在单一方面的加密或解密
            return pwd;
        } catch (NoSuchAlgorithmException e1) {
            // TODO: handle exceptions
            e1.printStackTrace();
        } catch (NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 3DES解密
     *
     * @param keybyte    加密密钥，长度为24字节
     * @param desStr 解密后的字符串
     * @return lee on 2017-08-09 10:52:54
     */
    public static String decode3Des(byte[] keybyte, String desStr) {

        try {
            byte[] src = Base64Util.decode(desStr);
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, "DESede");
            //解密
            Cipher c1 = Cipher.getInstance("DESede");
            c1.init(Cipher.DECRYPT_MODE, deskey);
            String pwd = new String(c1.doFinal(src));
//            return c1.doFinal(src);
            return pwd;
        } catch (NoSuchAlgorithmException e1) {
            // TODO: handle exceptions
            e1.printStackTrace();
        } catch (NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
}
