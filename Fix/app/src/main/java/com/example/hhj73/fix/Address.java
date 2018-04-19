package com.example.hhj73.fix;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class Address extends AppCompatActivity {

    WebView webView;
    TextView result;
    Handler handler;
    String confirm;

    TextView tv ;
    Boolean activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        Intent intent = getIntent();
       activity = intent.getBooleanExtra("activity",true);

        result = (TextView) findViewById(R.id.result);

        // WebView 초기화
        init_webView();

        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();
    }

    public void init_webView() {
        // WebView 설정
        webView = (WebView) findViewById(R.id.webview);
        // JavaScript 허용
        webView.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new WebChromeClient());

//----------------------------------------------------------------------------------------------

//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//               view.loadUrl(url);
//               return true;
//            }
//        });

        //--------------------------------------------------------------------------------------
        //webView.loadUrl("http://kuxzl.entd21u.my03w.com/addtest.php");    //php환경
        webView.loadUrl("http://kuxzl.oicp.io:31717/add.html");             //hosting 31717  ,host 80 금지
    }

    public void addclick(View view) {
        Intent add = new Intent(this,SeniorJoinActivity.class);

        add.putExtra("ADDRESS",confirm);
        if(activity==false)
            add.setClass(this,SeniorJoinActivity.class);
        else
            add.setClass(this,StudentJoinActivity.class);
        add.putExtra("ADDCOUNT",true);

        startActivity(add);

        //Toast.makeText(this, confirm, Toast.LENGTH_SHORT).show();      //리턴 값을 테스트


    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    result.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                    init_webView();   //초기화 안하는 경우 이상해짐 !

                    confirm=("("+ arg1+")"+ arg2+ arg3);  // String 전송
                }
            });
        }
    }
}
