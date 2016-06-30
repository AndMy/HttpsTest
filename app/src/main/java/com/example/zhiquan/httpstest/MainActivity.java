package com.example.zhiquan.httpstest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * 界面丑陋，主要是演示android使用AsyncHttpClient框架进行https请求
 * @author whx
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    /**
     * 此处的请求示例url为12306的注册登录接口
     */
    private static final String URL = "https://kyfw.12306.cn/otn/regist/init";

    private Button httpBtn,httpsBtn, clearBtn;
    private TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = (TextView)findViewById(R.id.display);

        httpBtn = (Button)findViewById(R.id.http_btn);
        httpBtn.setOnClickListener(this);

        httpsBtn = (Button)findViewById(R.id.https_btn);
        httpsBtn.setOnClickListener(this);

        clearBtn = (Button)findViewById(R.id.clear_btn);
        clearBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.http_btn:
                HttpUtils.get(URL, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        display.setText(statusCode + "  "+ responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        display.setText(responseString);
                    }
                });
                break;
            case R.id.https_btn:

                //使用https请求的关键代码之一
                HttpUtils.getClient().setSSLSocketFactory(SSLCustomSocketFactory.getSocketFactory(this));

                HttpUtils.get(URL, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        display.setText(statusCode + "  "+ responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        if (responseString != null) {
                            Intent intent = new Intent(MainActivity.this, WebActivity.class);
                            intent.putExtra("html",responseString);
                            startActivity(intent);
                        }
                    }
                });
                break;
            case R.id.clear_btn:
                display.setText("");
                break;
        }
    }
}
