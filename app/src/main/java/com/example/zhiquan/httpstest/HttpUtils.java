package com.example.zhiquan.httpstest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cz.msebera.android.httpclient.Header;


/**
 * 使用AsyncHttpClient网络请求框架的网络请求工具类
 * Created by whx on 2016/6/30.
 */
public class HttpUtils {

    public static final String TAG = "AppAsyncHttpClient";

    /**
     * 请求数据的hTTPClient
     */
    private static AsyncHttpClient client;
    /**
     * 是否开启安全模块
     */
    private static boolean enableSecurityModule = true;

    static {
        client = new AsyncHttpClient();

    }

    /**
     * 获得AsyncHttpClient对象
     * @return
     */
    public static AsyncHttpClient getClient(){
        return client;
    }
    /**
     * 初始化安全模块
     * <p/>
     * Set Headers
     *
     * @param key
     * @param userId
     */
    public static void initSecurityModule(String key, String userId) {
//        client.addHeader(URLConstants.RequestHeaderConstants.Header_Key, key);
//        client.addHeader(URLConstants.RequestHeaderConstants.HEADER_User, userId);

        Log.e(TAG, "init security module");
    }

    /**
     * 设置是否开启安全模块
     *
     * @param enable
     */
    public static void enableSecurityModule(boolean enable) {
        enableSecurityModule = enable; //其实并没有什么卵用
    }

    public static void get(String url, FileAsyncHttpResponseHandler responseHandler) {
        client.get(url, responseHandler);
        Log.e(TAG, "GET: " + url + "\t");
    }

    public static void get(String url, TextHttpResponseHandler responseHandler) {
        client.get(url, responseHandler);
        Log.e(TAG, "GET: " + url + "\t");
    }


    /**
     * no external parameters
     *
     * @param url
     * @param responseHandler
     */
    public static void get(String url, JsonHttpResponseHandler responseHandler) {
        get(url, new RequestParams(), responseHandler);
    }

    /**
     * @param url
     * @param map             需要传递参数的键值对 会自动将map中的键值对转换
     * @param responseHandler
     */
    public static void get(String url, @NonNull Map<String, String> map, JsonHttpResponseHandler responseHandler) {
        get(url, new RequestParams(map), responseHandler);
    }

    /**
     * 这个请求是应该最常用
     *
     * @param url
     * @param value           支持各种类型 list,hashMap, hashSet 等，
     * @param key
     * @param responseHandler
     */
    public static void get(String url, String key, Object value, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put(key, value);

        get(url, params, responseHandler);
    }

    /**
     * 最原始的请求形式
     *
     * @param url
     * @param params          请求所需要的参数
     * @param responseHandler
     */
    public static void get(@NonNull String url, @Nullable RequestParams params, @NonNull final JsonHttpResponseHandler responseHandler) {


        AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e(TAG, responseBody == null ? "Empty Body" : new String(responseBody));

                try {
                    if (responseHandler != null)
                        responseHandler.onSuccess(statusCode, headers, new JSONObject(new String(responseBody)));
                } catch (JSONException e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseBody, e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //匹配以4开头的错误码
                Pattern p = Pattern.compile("^4.*");
                Matcher m = p.matcher(String.valueOf(statusCode));
                if(m.find()){
                    Log.e(TAG, "onFailure: send "+statusCode+" Broadcast" );

                }
                Log.e(TAG, String.format("Response: %s\n error: %s, code: %d",
                        responseBody == null?  "Empty Body": new String(responseBody),
                        error == null ?  "Empty error": error.getLocalizedMessage(), statusCode));

                if (responseHandler != null)
                    responseHandler.onFailure(statusCode, headers, responseBody == null ? "" : new String(responseBody), error);
            }
        };

        if (params == null)
            client.get(url, handler);
        else
            client.get(url, params, handler);

        Log.e(TAG, "GET: " + url + (params == null ? "" : "\nparams: " + params.toString()));
    }


    public static void post(String url, JsonHttpResponseHandler responseHandler) {
        post(url, new RequestParams(), responseHandler);
    }

    /**
     * @param url
     * @param params          需要传递参数的键值对 会自动将map中的键值对转换
     * @param responseHandler
     */
    public static void post(String url, Map<String, String> params, final JsonHttpResponseHandler responseHandler) {
        post(url, new RequestParams(params), responseHandler);
    }

    /**
     * @param url
     * @param value           支持各种类型 list,hashMap, hashSet 等，
     * @param key
     * @param responseHandler
     */
    public static void post(String url, Object value, String key, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put(key, value);
        post(url, params, responseHandler);
    }

    /**
     * 最原始的请求形式
     *
     * @param url
     * @param params          参数
     * @param responseHandler
     */
    public static void post(String url, @Nullable RequestParams params, final JsonHttpResponseHandler responseHandler) {

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e(TAG, responseBody == null ? "Empty Body" : new String(responseBody));
                try {
                    if (responseHandler != null)
                        responseHandler.onSuccess(statusCode, headers, new JSONObject(new String(responseBody)));
                } catch (JSONException e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, e.getMessage().getBytes(), e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //匹配以4开头的错误码
                Pattern p = Pattern.compile("^4.*");
                Matcher m = p.matcher(String.valueOf(statusCode));
                if(m.find()){
                    Log.e(TAG, "onFailure: send "+statusCode+" Broadcast" );

                }
                Log.e(TAG, String.format("Response: %s\n error: %s",
                        responseBody == null ? "网络异常" : new String(responseBody),
                        error == null ? "Empty error" : error.getLocalizedMessage()));
                if (responseHandler != null)
                    responseHandler.onFailure(statusCode, headers, responseBody == null ? "Empty Body" : new String(responseBody), error);
            }
        });

        Log.e(TAG, "POST: " + url + (params == null ? "" : "\nparams: " + params.toString()));
    }
}
