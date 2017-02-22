package com.loginair.gshopping.http;

import com.loginair.gshopping.utils.IOUtils;
import com.loginair.gshopping.utils.LogUtils;
import com.loginair.gshopping.utils.StringUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.xml.sax.InputSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by PineChang on 2017/2/17.
 */

public class HttpHelper {

    public static final String URL = "http://127.0.0.1:8090/";
    //封装一个HttpResult{
    public  static class HttpResult{
        private HttpResponse mResponse;
        private InputStream mIn;//从响应中获取流
        private String mStr;//从响应中获取字符串;
        private HttpClient mHttpClient;
        private HttpRequestBase mRequestBase;
        //要输入响应体,HttpClient,以及requestBase
        public  HttpResult(HttpResponse response,HttpClient httpClient,HttpRequestBase requestBase){
            mResponse = response;
            mHttpClient = httpClient;
            mRequestBase = requestBase;

        }

        //拿到响应Code
        public  int getCode(){
            StatusLine status = mResponse.getStatusLine();
            return status.getStatusCode();
        }
        //拿到流
        public  InputStream getInputStream(){
            if(mIn == null && getCode()<300){
                //如果从未得到InputStream并且传递进来的entity状态码不是404,
                HttpEntity entity =  mResponse.getEntity();
                try{
                    mIn = entity.getContent();
                }catch(Exception e){
                    LogUtils.e(e)
                }
            }
            return mIn;
        }

        //拿到字符串,保存字符串,关闭流
        public  String getString(){
            //如果以前已经存储过了,直接取出
            if(!StringUtils.isEmptyString(mStr)){
                return mStr;
            }
            //否则就从响应体中拿到
            InputStream  inputStream = getInputStream();
            ByteArrayOutputStream  out   = null;
            if (inputStream!=null){
                try{
                    out = new ByteArrayOutputStream();
                    byte[] buffer  = new byte[1024*4];
                    int len = -1;
                    while((len=inputStream.read(buffer)!=-1)){
                        out.write(buffer,0,len);
                    }
                    byte[] data = out.toByteArray();
                    mStr  = new String (data,"utf-8");

                }catch(Exception e){
                    LogUtils.e(e);
                }finally{
                    IOUtils.close(out);
                    close();
                }
            }
            return mStr;

        }
        //获取后要将所有的都关闭了,即要关闭请求又要关闭HttpClient;
        private  void close(){
            //关闭流连接
            if(mRequestBase!=null){
                mRequestBase.abort();
            }
            IOUtils.close(mIn);
            if(mHttpClient!=null){
                mHttpClient.getConnectionManager().closeExpiredConnections();
            }

        }
    }

    //以下为暴露出去的get,post,download方法
    public static HttpResult get(String url){
        HttpGet  httpGet = new HttpGet(url);
        return  execute(url,httpGet);
    }
    //这里HttpResult为什么会用bytes,是因为post请求的可能发送二进制数据比如视频,图片
    public static HttpResult post(String url,byte[] bytes){
        HttpPost httpPost = new HttpPost(url);
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bytes);
        httpPost.setEntity(byteArrayEntity);
        return execute(url,httpPost);
    }
    public static HttpResult download(String url){
        HttpGet httpGet = new HttpGet(url);
        return execute(url,httpGet);
    }

    /** 执行网络访问 */
    private static HttpResult execute(String url, HttpRequestBase requestBase) {
        boolean isHttps = url.startsWith("https://");//判断是否需要采用https
        AbstractHttpClient httpClient = HttpClientFactory.create(isHttps);
        HttpContext httpContext = new SyncBasicHttpContext(new BasicHttpContext());
        HttpRequestRetryHandler retryHandler = httpClient.getHttpRequestRetryHandler();//获取重试机制
        int retryCount = 0;
        boolean retry = true;
        while (retry) {
            try {
                HttpResponse response = httpClient.execute(requestBase, httpContext);//访问网络
                if (response != null) {
                    return new HttpResult(response, httpClient, requestBase);
                }
            } catch (Exception e) {
                IOException ioException = new IOException(e.getMessage());
                retry = retryHandler.retryRequest(ioException, ++retryCount, httpContext);//把错误异常交给重试机制，以判断是否需要采取从事
                LogUtils.e(e);
            }
        }
        return null;
    }
}
