package com.example.zhiquan.httpstest;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

/**
 * 忽略所有证书验证
 * Created by whx on 2016/7/1.
 */
public class SSLTrustAllSocketFactory extends SSLSocketFactory{

    private static final String TAG = "SSLTrustAllSocketFactory";
    private SSLContext sslContext;

    //自定义一个TrustManager忽略所有校检
    public class SSLTrustAllManager implements X509TrustManager{

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
    public SSLTrustAllSocketFactory(KeyStore truststore) throws
            NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        try{
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null,new TrustManager[]{new SSLTrustAllManager()},null);

            setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }

    public static SSLSocketFactory getSocketFactory(){

        try{
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null,null);
            SSLSocketFactory factory = new SSLTrustAllSocketFactory(trustStore);
            return factory;
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

        return null;
    }
}
