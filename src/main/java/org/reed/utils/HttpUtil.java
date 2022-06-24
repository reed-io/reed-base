/**
 * RedisClient/com.reed.redisclient.utils/HttpUtil.java
 */
package org.reed.utils;

import org.reed.entity.RequestMethod;
import org.reed.exceptions.HttpAbnormalStatusException;
import org.reed.utils.StringUtil.Encoding;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * @author chenxiwen
 * @date 2017年8月8日下午4:29:56
 */
public final class HttpUtil {
    private static SSLContext sslCtx = null;
    
    /**
     * default timeout value milliseconds
     */
    private static final int DEFAULT_TIMEOUT = 5000;

    private static final String BOUNDARY="--------------------REED@ORG.REED--------------------";
    
    
    static {

        try {
            sslCtx = SSLContext.getInstance("SSL");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sslCtx.init(null, new TrustManager[] { tm }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                // TODO Auto-generated method stub
                return true;
            }

        });

        HttpsURLConnection.setDefaultSSLSocketFactory(sslCtx.getSocketFactory());

    }
    
    public static HttpsURLConnection getHttpsPostConnection(final String uri) throws IOException{
        return getHttpsConnection(uri, RequestMethod.POST, DEFAULT_TIMEOUT, null);
    }
    
    public static HttpsURLConnection getHttpsGetConnection(final String uri) throws IOException{
        return getHttpsConnection(uri, RequestMethod.GET, DEFAULT_TIMEOUT, null);
    }
    
    public static HttpsURLConnection getHttpsPostConnection(final String uri, final Map<String, String> requestPropertyMap) throws IOException{
        return getHttpsConnection(uri, RequestMethod.POST, DEFAULT_TIMEOUT, requestPropertyMap);
    }
    
    public static HttpsURLConnection getHttpsGetConnection(final String uri, final Map<String, String> requestPropertyMap) throws IOException{
        return getHttpsConnection(uri, RequestMethod.GET, DEFAULT_TIMEOUT, requestPropertyMap);
    }
    
    public static HttpsURLConnection getHttpsConnection(final String uri, final RequestMethod method, final int timeout, final Map<String, String> requestPropertyMap) throws IOException{
        URL url=new URL(uri);  
        HttpsURLConnection conn=(HttpsURLConnection)url.openConnection();  
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);  
        conn.setReadTimeout(timeout);
        conn.setRequestMethod(method.toString());
        conn.setSSLSocketFactory(sslCtx.getSocketFactory());  
        if(requestPropertyMap!=null && !requestPropertyMap.isEmpty()){
            for(String key : requestPropertyMap.keySet()){
                conn.setRequestProperty(key, requestPropertyMap.get(key));
            }
        }
        return conn;
    }
    
    public static void post(final byte[] data, final HttpsURLConnection conn) throws IOException{
        if(!conn.getRequestMethod().equalsIgnoreCase("post")){
            throw new IllegalArgumentException("connection is not base on post way!");
        }
        OutputStream os = conn.getOutputStream();
        os.write(data);
        os.flush();
        os.close();
    }

    /**
     *
     * @param data 需要post的字节数组
     * @param fileMap 需要post的文件Map
     * @param conn
     * @throws IOException
     */
    public static void post(final byte[] data, Map<String, File> fileMap, final HttpsURLConnection conn) throws IOException{
        if(!conn.getRequestMethod().equalsIgnoreCase("post")){
            throw new IllegalArgumentException("connection is not base on post way!");
        }
        conn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + BOUNDARY);
        OutputStream os = conn.getOutputStream();
        if(!MapUtil.isEmpty(fileMap)){
            for(String name:fileMap.keySet()){
                System.out.println(name);
                StringBuffer strBuf = new StringBuffer();
                strBuf.append("\r\n").append("--").append(BOUNDARY)
                        .append("\r\n");
                strBuf.append("Content-Disposition: form-data; name=\""
                        + name + "\"; filename=\"" + name
                        + "\"\r\n");

                strBuf.append("Content-Type:application/octet-stream" + "\r\n\r\n");
                os.write(strBuf.toString().getBytes());
                DataInputStream in = new DataInputStream(
                        new FileInputStream(fileMap.get(name)));
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) {
                    System.out.println(bytes);
                    os.write(bufferOut, 0, bytes);
                }
                in.close();
            }
        }
        os.write(data);
        os.flush();
        os.close();
    }

    /**
     *
     * @param content 发送的json
     * @param fileMap 需要上传的文件Map
     * @param conn
     * @throws IOException
     */
    public static void post(final String content, Map<String, File> fileMap, final HttpsURLConnection conn) throws IOException{
        post(content.getBytes(Encoding.UTF8.getEncoding()), fileMap, conn);
    }

    //带文件的post
    public static void post(final String content, final Encoding encoding, Map<String, File> fileMap, final HttpsURLConnection conn) throws IOException{
        post(content.getBytes(encoding.getEncoding()), fileMap, conn);
    }
    
    public static byte[] readHttpURLConnection(final HttpsURLConnection conn) throws IOException{
        InputStream is = conn.getInputStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        is.close();
        return data;
    }
    
    /**
     * 从连接中按照指定的编码格式读取正确的文本信息（HttpResponseCode=200）
     * @param conn
     * @param encoding
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws HttpAbnormalStatusException
     */
    public static String readResult(final HttpsURLConnection conn, final Encoding encoding) throws UnsupportedEncodingException, IOException, HttpAbnormalStatusException{
        String message = new String(readHttpURLConnection(conn), encoding.getEncoding());
        if(conn.getResponseCode() == 200){
            return message;
        }
        throw new HttpAbnormalStatusException(conn.getResponseCode()+":"+message);
    }
    /**
     * 从连接中按照默认的编码格式（utf-8）读取正确的文本信息（HttpResponseCode=200）
     * @param conn
     * @return
     * @throws UnsupportedEncodingException
     * @throws HttpAbnormalStatusException
     * @throws IOException
     */
    public static String readResult(final HttpsURLConnection conn) throws UnsupportedEncodingException, HttpAbnormalStatusException, IOException{
        return readResult(conn, Encoding.UTF8);
    }


    public static HttpURLConnection getNormalHttpConnection(final String uri, final int timeout) throws IOException{
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        return conn;
    }
    
    /**
     * 通过传入的地址使用默认的超时时间获取一个post的http连接
     * @param uri
     * @return
     * @throws IOException
     */
    public static HttpURLConnection getPostHttpConnection(final String uri) throws IOException{
        return getPostHttpConnection(uri, DEFAULT_TIMEOUT);
    }
    /**
     * 通过传入的地址和超时时间获取一个post的http连接
     * @param uri
     * @param timeout
     * @return
     * @throws IOException
     */
    public static  HttpURLConnection getPostHttpConnection(final String uri, final int timeout) throws IOException{
        return getPostHttpConnection(uri, timeout, null);
    }
    /**
     * 通过传入的地址和超时时间获取一个post的http连接, 并根据requestPropertyMap预先设定好RequestProperty
     * @param uri
     * @param timeout
     * @param requestPropertyMap
     * @return
     * @throws IOException
     */
    public static HttpURLConnection getPostHttpConnection(final String uri, final int timeout, final Map<String, String> requestPropertyMap) throws IOException{
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        if(requestPropertyMap!=null && !requestPropertyMap.isEmpty()){
            for(String key : requestPropertyMap.keySet()){
                conn.setRequestProperty(key, requestPropertyMap.get(key));
            }
        }
        return conn;
    }
    public static void post(final byte[] data, final HttpURLConnection conn) throws IOException{
        if(!conn.getRequestMethod().equalsIgnoreCase("post")){
            throw new IllegalArgumentException("connection is not base on post way!");
        }
        OutputStream os = conn.getOutputStream();
        os.write(data);
        os.flush();
        os.close();
    }
    public static void post(final String content, final Encoding encoding, final HttpsURLConnection conn) throws IOException{
        post(content.getBytes(encoding.getEncoding()), conn);
    }

    public static void post(final String content,  final HttpsURLConnection conn) throws IOException{
        post(content.getBytes(Encoding.UTF8.getEncoding()), conn);
    }
    
    
    /**
     * 使用指定的连接通过post方式将指定的字符串使用指定的编码格式发送出去
     * @param content
     * @param encoding
     * @param conn
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static void post(final String content, final Encoding encoding, final HttpURLConnection conn) throws UnsupportedEncodingException, IOException{
        post(content.getBytes(encoding.getEncoding()), conn);
    }
    /**
     * 使用指定的连接通过post方式将指定的字符串使用默认的编码格式（utf-8）发送出去
     * @param content
     * @param conn
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static void post(final String content,  final HttpURLConnection conn) throws UnsupportedEncodingException, IOException{
        post(content.getBytes(Encoding.UTF8.getEncoding()), conn);
    }

    /**
     *
     * @param data 需要post的字节数组
     * @param fileMap 需要post的文件Map
     * @param conn
     * @throws IOException
     */
    public static void post(final byte[] data, Map<String, File> fileMap, final HttpURLConnection conn) throws IOException{
        if(!conn.getRequestMethod().equalsIgnoreCase("post")){
            throw new IllegalArgumentException("connection is not base on post way!");
        }
        conn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + BOUNDARY);
        OutputStream os = conn.getOutputStream();
        if(!MapUtil.isEmpty(fileMap)){
            for(String name:fileMap.keySet()){
//                System.out.println(name);
//                File f = fileMap.get(name);
//                System.out.println(f.getAbsolutePath());
//                System.out.println(f.getName());
                StringBuffer strBuf = new StringBuffer();
                strBuf.append("Content-Disposition: form-data; name=\""
                        + name + "\"; filename=\"" + name
                        + "\"\r\n");
                strBuf.append(BOUNDARY);

//                strBuf.append("Content-Type:application/octet-stream" + "\r\n\r\n");
                os.write(strBuf.toString().getBytes());
                DataInputStream in = new DataInputStream(
                        new FileInputStream(fileMap.get(name)));
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) {
//                    System.out.println(bytes);
                    os.write(bufferOut, 0, bytes);
                }
                in.close();

                os.write(BOUNDARY.getBytes());
            }
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("Content-Disposition: form-data; name=\"request_data\"");
        os.write(strBuf.toString().getBytes());
        System.out.println(new String(data));
        os.write(data);
        os.write(BOUNDARY.getBytes());
        os.flush();
        os.close();
    }

    /**
     *
     * @param content 发送的json
     * @param fileMap 需要上传的文件Map
     * @param conn
     * @throws IOException
     */
    public static void post(final String content, Map<String, File> fileMap, final HttpURLConnection conn) throws IOException{
        post(content.getBytes(Encoding.UTF8.getEncoding()), fileMap, conn);
    }

    //带文件的post
    public static void post(final String content, final Encoding encoding, Map<String, File> fileMap, final HttpURLConnection conn) throws IOException{
        post(content.getBytes(encoding.getEncoding()), fileMap, conn);
    }
    
    /**
     * 从连接中读取数据
     * @param conn
     * @return
     * @throws IOException
     */
    public static byte[] readHttpURLConnection(final HttpURLConnection conn) throws IOException{
        InputStream is = conn.getInputStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        is.close();
        return data;
    }
    
    /**
     * 从连接中按照指定的编码格式读取正确的文本信息（HttpResponseCode=200）
     * @param conn
     * @param encoding
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws HttpAbnormalStatusException
     */
    public static String readResult(final HttpURLConnection conn, final Encoding encoding) throws UnsupportedEncodingException, IOException, HttpAbnormalStatusException{
        String message = new String(readHttpURLConnection(conn), encoding.getEncoding());
        if(conn.getResponseCode() == 200){
            return message;
        }
        throw new HttpAbnormalStatusException(conn.getResponseCode()+":"+message);
    }
    /**
     * 从连接中按照默认的编码格式（utf-8）读取正确的文本信息（HttpResponseCode=200）
     * @param conn
     * @return
     * @throws UnsupportedEncodingException
     * @throws HttpAbnormalStatusException
     * @throws IOException
     */
    public static String readResult(final HttpURLConnection conn) throws UnsupportedEncodingException, HttpAbnormalStatusException, IOException{
        return readResult(conn, Encoding.UTF8);
    }

    /**
     * 
     */
    private HttpUtil() {
        // TODO Auto-generated constructor stub
    }

}
