/**
 * 
 */
package org.reed.utils;

import org.reed.define.CodeDescTranslator;
import org.reed.entity.ReedResult;
import org.reed.log.ReedLogger;
import org.reed.system.ReedContext;
import com.alibaba.fastjson2.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ender
 *
 */
public final class StringUtil {

    /**
     * Default charset name is {@link Encoding#UTF8}
     */
    public static final Charset DEFAULT_CHARSET = Charset.forName(Encoding.UTF8.getEncoding());

    /**
     * Default charset name is {@link Encoding#UTF8}
     */
    public static final String DEFAULT_CHARSET_NAME = Encoding.UTF8.getEncoding();

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f' };

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F' };
    
    public static final Pattern Reed_PATTERN = Pattern.compile("\\^\\{\\w+\\}");
    public static final Pattern Reed_ENV = Pattern.compile("ReedEnv\\{\\w+\\}");
    public static final Pattern Reed_CIPHER = Pattern.compile("ReedCipher\\{\\w+\\}");

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNumStr(String str) {
        return !isEmpty(str) && Pattern.matches("^[-+]?\\d+(\\.\\d+)?$", str);
    }

    public static String genErrResult(int errCode, String errMsg) {
        return "{errCode:" + errCode + ", errMsg:" + errMsg + "}";
    }

    /**
     * @deprecated replaced by <code>genResponse(int code, String message, String data)</code>
     */
    @Deprecated
    public static String genResp(boolean success, int errCode, String errMsg, String resultData) {
        JSONObject json = new JSONObject();
        json.put("success", success);
        json.put("err_code", errCode);
        json.put("err_msg", isEmpty(errMsg) ? "" : errMsg);
        json.put("result_data", isEmpty(resultData) ? "" : JSONObject.parseObject(resultData));
        return json.toJSONString();
    }

    /**
     * @deprecated replaced by <code>genResponse(int code, String message, Object data)</code>
     */
    @Deprecated
    public static String genResp(boolean success, int errCode, String errMsg, Object resultData) {
        JSONObject json = new JSONObject();
        json.put("success", success);
        json.put("err_code", errCode);
        json.put("err_msg", isEmpty(errMsg) ? "" : errMsg);
        json.put("result_data", resultData);
        return json.toJSONString();
    }

    /**
     * @deprecated replaced by <code>genResponse(int code, String data)</code>
     */
    @Deprecated
    public static String genResp(int errCode, String resultData){
        JSONObject json = new JSONObject();
        json.put("success", errCode==0);
        json.put("err_code", errCode);
        json.put("err_msg", CodeDescTranslator.explain(errCode));
        json.put("result_data", isEmpty(resultData) ? "" : JSONObject.parseObject(resultData));
        return json.toJSONString();
    }

    /**
     * @deprecated replaced by <code>genResponse(int code,Object data)</code>
     */
    @Deprecated
    public static String genResp(int errCode, Object resultData){
        JSONObject json = new JSONObject();
        json.put("success", errCode==0);
        json.put("err_code", errCode);
        json.put("err_msg", CodeDescTranslator.explain(errCode));
        json.put("result_data", resultData);
        return json.toJSONString();
    }

    /**
     * @deprecated replaced by <code>genResponse(ReedResult result)</code>
     */
    @Deprecated
    public static String genResp(ReedResult result) {
        return result.getCode() == 0
                ? StringUtil.genResp(true, result.getCode(), result.getMessage(),
                        result.getData() == null ? null : result.getData().toString())
                : StringUtil.genResp(false, result.getCode(), result.getMessage(),
                        result.getData() == null ? null : result.getData());
    }

    public static String genResponse(int code, String message, String data) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("message", message);
        json.put("data", StringUtil.isEmpty(data) ? "" : JSONObject.parseObject(data));
        return json.toJSONString();
    }

    public static String genResponse(int code, String message, Object data) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("message", message);
        json.put("data", data);
        return json.toJSONString();
    }

    public static String genResponse(int code, String data){
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("message", CodeDescTranslator.explain(code));
        json.put("data", StringUtil.isEmpty(data) ? "" : JSONObject.parseObject(data));
        return json.toJSONString();
    }

    public static String genResponse(int code, Object data){
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("message", CodeDescTranslator.explain(code));
        json.put("data", data);
        return json.toJSONString();
    }
    public static String genResponse(ReedResult result) {
        return result.getCode() == 0
                ? StringUtil.genResponse(result.getCode(), result.getMessage(),
                result.getData() == null ? null : result.getData().toString())
                : StringUtil.genResponse( result.getCode(), result.getMessage(),
                result.getData() == null ? null : result.getData());
    }

    public static boolean isTrue(String boolStr) {
        return !isEmpty(boolStr)
                && (boolStr.equalsIgnoreCase("TRUE") || boolStr.equalsIgnoreCase("T") || boolStr.equalsIgnoreCase("0"));
    }

    public static String toUnderLineStr(String str) {
        Pattern p = Pattern.compile("[A-Z]");
        Matcher m = p.matcher(str);
        return m.replaceAll("_$0").toLowerCase();
    }

    public static boolean isIp(String str) {
        return !isEmpty(str) && Pattern.matches(
                "^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$",
                str.trim());
    }

    public static boolean isIPV4(String ip){
        return isIp(ip);
    }

    public static boolean isMacAddress(String str){
        return !isEmpty(str) && Pattern.matches("^[a-fA-F0-9]{2}(-[a-fA-F0-9]{2}){5}$|^[a-fA-F0-9]{2}(:[a-fA-F0-9]{2}){5}$|^[a-fA-F0-9]{12}$|^[a-fA-F0-9]{4}(\\.[a-fA-F0-9]{4}){2}$", str.trim());
    }

    /**
     * check input return true if it's validate as an IPV6 address
     * @param ip
     * @return
     */
    public static boolean isIPv6(String ip){
        return !isEmpty(ip) &&
                Pattern.matches("^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$", ip);
    }

    /**
     * check input return true if it's validate as a domain
     * eg: baidu.com
     * @param domain
     * @return
     */
    public static boolean isDomain(String domain){
        return !isEmpty(domain) && Pattern.matches("^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$", domain);
    }

    /**
     * check input return true if it's validate as a website
     * eg: http://www.baidu.com
     * @param website
     * @return
     */
    public static boolean isWebsite(String website){
        return !isEmpty(website) && Pattern.matches("^(?=^.{3,255}$)(http(s)?:\\/\\/)?(www\\.)?[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(:\\d+)*(\\/\\w+\\.\\w+)*$", website);
    }

    /**
     * check input return true if it's validate as a url
     * eg: http://www.tetet.com/index.html?q=1&m=test
     * @param url
     * @return
     */
    public static boolean isUrl(String url){
        return !isEmpty(url) && Pattern.matches("^(?=^.{3,255}$)(http(s)?:\\/\\/)?(www\\.)?[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(:\\d+)*(\\/\\w+\\.\\w+)*([\\?&]\\w+=\\w*)*$", url);
    }

    public static boolean isPort(String port) {
        return !isEmpty(port) && Pattern.matches("^[-+]?\\d+(\\.\\d+)?$", port) && Integer.parseInt(port) >= 0
                && Integer.parseInt(port) <= 65535;
    }

    public static boolean isPort(int port){
        return port>=0 && port<=65535;
    }

    public static <E> String getCollectionsStr(Collection<E> c, String splitStr) {
        StringBuilder strBuilder = new StringBuilder();
        for (E e : c) {
            strBuilder.append(e.toString());
            strBuilder.append(splitStr);
        }
        return isEmpty(splitStr) ? strBuilder.toString()
                : strBuilder.delete(strBuilder.lastIndexOf(splitStr), strBuilder.length()).toString();
    }

    public static <E> String getCollectionsStr(Object source, Collection<E> c, Method m, String splitStr)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        m.setAccessible(true);
        StringBuilder strBuilder = new StringBuilder();
        for (E e : c) {
            strBuilder.append(m.invoke(source, e));
            strBuilder.append(splitStr);
        }
        return isEmpty(splitStr) ? strBuilder.toString()
                : strBuilder.delete(strBuilder.lastIndexOf(splitStr), strBuilder.length()).toString();
    }

    public static <T, E> String getMapStr(Object source, Map<T, E> map, Method m, String splitStr)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        m.setAccessible(true);
        // StringBuilder strBuilder = new StringBuilder();
        // for(HostAndPort hap : map.keySet()){
        // strBuilder.append(m.invoke(source, map.get(hap)));
        // strBuilder.append(splitStr);
        // }
        // return
        // isEmpty(splitStr)?strBuilder.toString():strBuilder.delete(strBuilder.lastIndexOf(splitStr),
        // strBuilder.length()).toString();
        return m.invoke(source, map, splitStr).toString();
    }

    public static String encode(String content, String encoding) throws UnsupportedEncodingException {
        return URLEncoder.encode(content, encoding);
    }

    public static String encode(String content, Encoding encoding) throws UnsupportedEncodingException {
        return URLEncoder.encode(content, encoding.getEncoding());
    }

    public static byte[] sha1(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(str.getBytes(Encoding.UTF8.getEncoding()));
        return md.digest();
    }

    /**
     * Converts an array of bytes into a String representing the hexadecimal
     * values of each byte in order. The returned String will be double the
     * length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * @param data
     *            a byte[] to convert to Hex characters
     * @return A String containing hexadecimal characters
     * @since 1.4
     */
    public static String encodeHexString(final byte[] data) {
        return new String(encodeHex(data));
    }

    /**
     * Converts an array of bytes into an array of characters representing the
     * hexadecimal values of each byte in order. The returned array will be
     * double the length of the passed array, as it takes two characters to
     * represent any given byte.
     *
     * @param data
     *            a byte[] to convert to Hex characters
     * @return A char[] containing hexadecimal characters
     */
    public static char[] encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * Converts an array of bytes into an array of characters representing the
     * hexadecimal values of each byte in order. The returned array will be
     * double the length of the passed array, as it takes two characters to
     * represent any given byte.
     *
     * @param data
     *            a byte[] to convert to Hex characters
     * @param toLowerCase
     *            <code>true</code> converts to lowercase, <code>false</code> to
     *            uppercase
     * @return A char[] containing hexadecimal characters
     * @since 1.4
     */
    public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * Converts an array of bytes into an array of characters representing the
     * hexadecimal values of each byte in order. The returned array will be
     * double the length of the passed array, as it takes two characters to
     * represent any given byte.
     *
     * @param data
     *            a byte[] to convert to Hex characters
     * @param toDigits
     *            the output alphabet
     * @return A char[] containing hexadecimal characters
     * @since 1.4
     */
    protected static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }
    
    /**
     * 正向转换字符串,bytes的低位在string的低位
     */
    public static byte[] decodeHex(String bytes){
      byte[] _bytes=new byte[bytes.length()/2];
      for(int idx=0;idx<_bytes.length;idx++){
        _bytes[idx]=(byte)Integer.parseInt(bytes.substring(idx*2,idx*2+2),16);
      }
      return _bytes;
    }

    /**
     * 反向转换字符串,bytes的高位在string的低位
     */
    public static byte[] decodeHex_revert(String bytes){
      byte[] _bytes=new byte[bytes.length()/2];
      for(int idx=0;idx<_bytes.length;idx++){
        _bytes[_bytes.length-idx-1]=(byte)Integer.parseInt(bytes.substring(idx*2,idx*2+2),16);
      }
      return _bytes;
    }

    public static String replace(String source, String match, String replace) {
        StringBuffer buffer = new StringBuffer();
        if (match != null && match.length() > 0) {
            if (source.indexOf(match) == -1) {
                buffer.append(source);
            } else {
                int lpos = 0, len = match.length(), cpos = 0;
                while ((cpos = source.indexOf(match, cpos)) != -1) {
                    if (cpos != lpos) {
                        buffer.append(source, lpos, cpos);
                    }
                    lpos = (cpos += len);
                    buffer.append(replace);
                }
                if (lpos != source.length()) {
                    buffer.append(source.substring(lpos));
                }
            }
        } else {
            buffer.append(source);
        }
        return buffer.toString();
    }

    public static String[] toArray(String source) {
        return toArray(source, null);
    }

    public static String[] toArray(String source, String match) {
        List<String> arrays = split2List(source, match);
        return arrays.toArray(new String[0]);
    }

    public static List<String> split2List(String source) {
        return split2List(source, null);
    }

    public static List<String> split2List(String source, String match) {
        List<String> splits = new ArrayList<String>();
        if (match != null && match.length() > 0) {
            if (source.indexOf(match) == -1) {
                splits.add(source);
            } else {
                int lpos = 0, len = match.length(), cpos = 0;
                while ((cpos = source.indexOf(match, cpos)) != -1) {
                    if (cpos == lpos) {
                        lpos = (cpos += len);
                        continue;
                    } else {
                        splits.add(source.substring(lpos, cpos));
                        lpos = (cpos += len);
                    }
                }
                if (lpos != source.length()) {
                    splits.add(source.substring(lpos));
                }
            }
        } else {
            for (int idx = 0; idx < source.length(); idx++) {
                splits.add(source.substring(idx, idx + 1));
            }
        }
        return splits;
    }
    
    public static List<String> csplit(String source,String match){
        List<String> splits=new ArrayList<String>();
        if(match!=null&&match.length()>0){
          if(source.indexOf(match)==-1){
            splits.add(source);
          }
          else{
            int lpos=0,len=match.length(),cpos=0;
            while((cpos=source.indexOf(match,cpos))!=-1){
              splits.add(source.substring(lpos,cpos));
              lpos=(cpos+=len);
            }
            if(cpos!=source.length()-1){
              splits.add(source.substring(lpos));
            }
          }
        }
        else{
          for(int idx=0;idx<source.length();idx++){
            splits.add(source.substring(idx,idx+1));
          }
        }
        return splits;
      }

    public static void main(String[] args) {
        // System.out.println(isNumStr("-0.a0001"));
        // System.out.println(isTrue("t"));
        // JSONObject json = new JSONObject();
        // json.put("1", 1);
        // json.put("String", "String");
        // System.out.println(genResp(true, 0, null, json.toJSONString()));
        // System.out.println(genResp(true, 0, null, null));
        // System.out.println(toUnderLineStr("expireTime"));

        // System.out.println(isIp("255.11.11.1"));
        // System.out.println(isPort("65536"));

//        System.out.println(StringUtil.replace("ender is good", "ender", "chenxiwen"));
        
        System.out.println(("abcdefg".substring(0, 0)).equalsIgnoreCase(""));
        
        List<String> list = split2List("adsdfadsfasdfasfdasdf","y");
//        List<String> list = csplit("adsdfadsfasdfasfdasdf","y");
        for(String str : list){
            System.out.println(str);
        }
    }

    public enum Encoding {
        UTF8("utf-8"), GBK("gbk"), GB2312("gb2312");
        private final String encoding;

        Encoding(String encoding) {
            this.encoding = encoding;
        }

        public String getEncoding() {
            return encoding;
        }

        public static Encoding getEncoding(String encoding) {
            for (Encoding enc : Encoding.values()) {
                if (enc.getEncoding().equalsIgnoreCase("enc")) {
                    return enc;
                }
            }
            throw new IllegalArgumentException("[" + encoding + "], is not illegal !");
        }
    }
    
    /** 
     *  MurMurHash算法，是非加密HASH算法，性能很高， 
     *  比传统的CRC32,MD5，SHA-1（这两个算法都是加密HASH算法，复杂度本身就很高，带来的性能上的损害也不可避免） 
     *  等HASH算法要快很多，而且据说这个算法的碰撞率很低. 
     *  http://murmurhash.googlepages.com/ 
     */  
    public static Long hashLong(String key) {  
          
        ByteBuffer buf = ByteBuffer.wrap(key.getBytes());  
        int seed = 0x1234ABCD;  
          
        ByteOrder byteOrder = buf.order();  
        buf.order(ByteOrder.LITTLE_ENDIAN);  
  
        long m = 0xc6a4a7935bd1e995L;  
        int r = 47;  
  
        long h = seed ^ (buf.remaining() * m);  
  
        long k;  
        while (buf.remaining() >= 8) {  
            k = buf.getLong();  
  
            k *= m;  
            k ^= k >>> r;  
            k *= m;  
  
            h ^= k;  
            h *= m;  
        }  
  
        if (buf.remaining() > 0) {  
            ByteBuffer finish = ByteBuffer.allocate(8).order(  
                    ByteOrder.LITTLE_ENDIAN);  
            // for big-endian version, do this first:   
            // finish.position(8-buf.remaining());   
            finish.put(buf).rewind();  
            h ^= finish.getLong();  
            h *= m;  
        }  
  
        h ^= h >>> r;  
        h *= m;  
        h ^= h >>> r;  
  
        buf.order(byteOrder);  
        return h;  
    }  
    
    /**
     * 使用FNV1_32_HASH算法计算服务器的Hash值,这里不使用重写hashCode的方法，最终效果没区别
     */
    public static int hashInt(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++)
            hash = (hash ^ str.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }

    public static byte[] md5ToByte(String str)
    {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] encryption = md5.digest();
            return encryption;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String md5(String str){
        byte[] encryption = md5ToByte(str);
        if (encryption == null)
            return null;
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < encryption.length; i++) {
            if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
            } else {
                strBuf.append(Integer.toHexString(0xff & encryption[i]));
            }
        }
        return strBuf.toString();
    }
    
    /**
     * 获取定长的字符串字节数组，要求字符串长度小于等于len
     * @param s
     * @param len
     * @return
     */
    public final static byte[] getFixedStringBytes(String s,int len){
        byte[] val=new byte[len];
        String str=s==null?"":s;
        byte[] bytes;
        bytes = str.getBytes(StandardCharsets.UTF_8);
        int rlen=Math.min(bytes.length,len);
        System.arraycopy(bytes,0,val,0,rlen);
        return val;
      }
    
    public final static String getString(byte[] bytes){
        int rlen=zeroRange(bytes,0);
        String result;
        result = new String(bytes,0,rlen, StandardCharsets.UTF_8);
        return result;
      }
    
    /**
     * 计算从offset开始，非0字符的个数
     */
    public static int zeroRange(byte[] data,int off){
      for(int idx=off;idx<data.length;idx++){
        if(data[idx]==0){
          return idx-off;
        }
      }
      return data.length-off;
    }
    
    public static String getMatchedFromEnv(String str){
      //用参数替换模板中的^{}变量
        Matcher m = Reed_PATTERN.matcher(str); 
        if (m.find()) {  
            String param = m.group(); //^{xx}  
            String value = System.getenv().get( param.substring(2, param.length() - 1));  
//            System.out.println(value==null?"null":value.toString());
            return value;
        } 
        return null;
    }


    public static Object getMatchedFromProperties(String str){
        //用参数替换模板中的^{}变量  
        Matcher m = Reed_PATTERN.matcher(str); 
        while (m.find()) {  
            String param = m.group(); //^{xx}  
            Object value = System.getProperties().get( param.substring(2, param.length() - 1));  
//            System.out.println(value==null?"null":value.toString());
            return value;
        } 
        return null;
    }

    /**
     * Reed_PATTERN = ^{} 判断字符串是否满足^{}
     * @param str
     * @return
     */
    public static boolean isMatched(String str){
        Matcher m = Reed_PATTERN.matcher(str);
        return m.matches();
    }

    /**
     * 根据正则（patternStr）判断是否匹配字符串content
     * @param pattern
     * @param content
     * @return
     */
    public static boolean isMatched(String pattern, String content){
        return Pattern.matches(pattern, content);
    }

    /**
     * content是否匹配pattern
     * @param pattern
     * @param content
     * @return
     */
    public static boolean isMatched(Pattern pattern, String content){
        return pattern.matcher(content).matches();
    }

    public static boolean isContains(Pattern pattern, String content){
        return pattern.matcher(content).find();
    }

    /**
     * 得到str中第一个匹配^{}的字符串,没有则反馈""
     * @param str
     * @return
     */
    public static String getFirstMatched(String str){
        Matcher m = Reed_PATTERN.matcher(str);
        if(m.find()){
            String param = m.group(); //^{xx}
            return param;
        }
        return "";
    }

    /**
     * 获取content中首个匹配 pattern的字符串，没有则返回""
     * @param pattern
     * @param content
     * @return
     */
    public static String getFirstMatched(Pattern pattern, String content){
        Matcher m = pattern.matcher(content);
        if(m.find()){
            String param = m.group();
            return param;
        }
        return "";
    }

    /**
     * 获取str中所有匹配^{}的字符串List集合
     * @param str
     * @return
     */
    public static List<String> getMatched(String str){
        Matcher m = Reed_PATTERN.matcher(str);
        List<String> result = new ArrayList<String>();
        while(m.find()){
            result.add(m.group());
        }
        return result;
    }

    /**
     * 获取content中所有匹配pattern的字符串List集合
     * @param content
     * @return
     */
    public static List<String> getMatched(Pattern pattern, String content){
        Matcher m = pattern.matcher(content);
        List<String> result = new ArrayList<String>();
        while(m.find()){
            result.add(m.group());
        }
        return result;
    }

    /**
     * 获取大括号{}内的值，非法则返回""
     * @param str
     * @return
     */
    public static String extractVal(String str){
        int left = str.indexOf("{");
        int right = str.indexOf("}");
        if(left>0 && right>left){
            return str.substring(left+1, right);
        }
        return "";
    }


    /**
     * decrypt Reed_CIPHER pattern String
     * @param s
     * @return
     */
    public static String decryptCiphertext(String s, String securityCode){
        if(!isEmpty(ReedContext.getString("mode")) && ReedContext.getString("mode").equalsIgnoreCase("debug")){
            ReedLogger.debug(EnderUtil.devInfo()+" - found Matched Pattern["+StringUtil.Reed_CIPHER.toString()+"] in "+s);
        }
        List<String> cipherMatchedList = StringUtil.getMatched(StringUtil.Reed_CIPHER, s);
        for(String cipherMatched:cipherMatchedList){
            if(!StringUtil.isEmpty(cipherMatched)){
                if(!isEmpty(ReedContext.getString("mode")) && ReedContext.getString("mode").equalsIgnoreCase("debug")) {
                    ReedLogger.debug(EnderUtil.devInfo() + " - placeholder[" + cipherMatched + "]");
                }
                String ciphertext = StringUtil.extractVal(cipherMatched);
                if(!isEmpty(ReedContext.getString("mode")) && ReedContext.getString("mode").equalsIgnoreCase("debug")) {
                    ReedLogger.debug(EnderUtil.devInfo() + " - extract environment key from placeholder[" + cipherMatched + "] as " + ciphertext);
                }
                String plaintext = "";
                try {
                    plaintext = new String(DESUtil.decrypt(StringUtil.decodeHex(ciphertext), securityCode));
                } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException
                        | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e1) {
                    e1.printStackTrace();
                    plaintext = e1.getMessage();
                    ReedLogger.error(e1.getMessage(), e1);
                } catch (Exception e2){
                    e2.printStackTrace();
                    plaintext = e2.getMessage();
                    ReedLogger.error(e2.getMessage(), e2);
                }
                if(!isEmpty(ReedContext.getString("mode")) && ReedContext.getString("mode").equalsIgnoreCase("debug")) {
                    ReedLogger.debug(EnderUtil.devInfo() + " - Decrypt(" + ciphertext + ") >>> " + plaintext);
                }
                s = s.replace(cipherMatched, plaintext);
                if(!isEmpty(ReedContext.getString("mode")) && ReedContext.getString("mode").equalsIgnoreCase("debug")) {
                    ReedLogger.trace(EnderUtil.devInfo() + " - after replace s=" + s);
                }
            }else{
                if(!isEmpty(ReedContext.getString("mode")) && ReedContext.getString("mode").equalsIgnoreCase("debug")) {
                    ReedLogger.warn(EnderUtil.devInfo() + " - find empty str!");
                }
            }
        }
        return s;
    }

    public static String encrypt2CipherText(String msg, String securityCode) throws UnsupportedEncodingException{
        byte[] result = DESUtil.encrypt(msg.getBytes(StandardCharsets.UTF_8), securityCode);
        String afterHex = StringUtil.encodeHexString(result);
        return afterHex;
    }

    public static String trimToNull(String str) {
        String ts = str.trim();
        return isEmpty(ts) ? null : ts;
    }
}
