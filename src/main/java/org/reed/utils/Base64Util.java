/**
 * IdeaProject @ org.reed.utils/Base64Util.java
 */
package org.reed.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Base64;

/**
 * @author chenxiwen
 * @createTime 2019年05月06日 下午5:38
 * @description
 */
public final class Base64Util {
//    final static BASE64Encoder encoder = new BASE64Encoder();
//    final static BASE64Decoder decoder = new BASE64Decoder();
    final static Base64.Encoder encoder = Base64.getEncoder();
    final static Base64.Decoder decoder = Base64.getDecoder();

    public static String encode(byte[] bytes){
//        return encoder.encode(bytes);
        return encoder.encodeToString(bytes);
    }

    public static String encode(ByteArrayOutputStream outputStream){
        return encode(outputStream.toByteArray());
    }

    public static byte[] decode(String str) throws IOException {
//        return decoder.decodeBuffer(str);
        return decoder.decode(str);
    }

    public static String decode2Utf8Str(String str) throws IOException {
        return decode(str, "UTF-8");
    }

    public static String decode(String str, String encoding) throws IOException {
        return new String(decode(str), encoding);
    }

    public static ByteBuffer decode2ByteBuffer(String str) throws IOException{
//        return decoder.decodeBufferToByteBuffer(str);
        return decoder.decode(ByteBuffer.wrap(str.getBytes()));
    }


}
