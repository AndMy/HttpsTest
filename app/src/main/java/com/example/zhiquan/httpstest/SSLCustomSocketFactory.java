package com.example.zhiquan.httpstest;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

/**
 * 验证自定义证书
 * Created by whx on 2016/6/30.
 */
public class SSLCustomSocketFactory extends SSLSocketFactory {

    private static final String TAG = "SSLCustomSocketFactory";

    private static final String KEY_PASS = "123456";

    public SSLCustomSocketFactory(KeyStore truststore) throws
            NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);
    }

    public static SSLSocketFactory getSocketFactory(Context context){

        try{

            //将证书生成的bks格式文件放入raw中，在此读取
            InputStream inputStream = context.getResources().openRawResource(R.raw.test);

            KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());

            try{
                truststore.load(inputStream,KEY_PASS.toCharArray());
            }
            finally {
                inputStream.close();
            }

            SSLSocketFactory factory = new SSLCustomSocketFactory(truststore);

            return factory;
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

        return null;
    }

}
