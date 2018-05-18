package com.hhj73.a0517;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 초기화
        textView = (TextView) findViewById(R.id.textView);
    }

    public void btnGo(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        // String str = editText.getText().toString();

        String str = "http://api.icndb.com/jokes/random";

        // *1* 웹뷰
//        WebView webView = (WebView) findViewById(R.id.webView);
//        webView.setWebViewClient(new WebViewClient());
//        WebSettings set = webView.getSettings();
//        set.setJavaScriptEnabled(true);
//        set.setBuiltInZoomControls(true);
//        set.setDefaultTextEncodingName("utf-8");
//        webView.loadUrl(str);

        // *2* 텍스트뷰

//        Ion.with(this)
//                .load(str)
//                .asString()
//                .setCallback(new FutureCallback<String>() {
//                    @Override
//                    public void onCompleted(Exception e, String result) {
//                        // textView.setText(result);
//                        parsingHTML(result);
//                    }
//                });

        Ion.with(this)
                .load(str)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        parsingJSON(result);
                    }
                });

    }

    private void parsingHTML(String result){
        String word = "";
        int pt_start = -1;
        int pt_end = -1;

        String tag_start = "<title>";
        String tag_end = "</title>";

        pt_start=result.indexOf(tag_start);

        if(pt_start != -1) {
            pt_end = result.indexOf(tag_end);
            if(pt_end != -1) {
                word = result.substring(pt_start + tag_start.length(), pt_end);
                textView.append("파싱결과 :" + word);
            } else
                textView.append("데이터가 없습니다.");
        } else
            textView.append("데이터가 없습니다.");
    }

    private void parsingJSON(String result){
        try {
            JSONObject json = new JSONObject(result);
            String str = json.getJSONObject("value").getString("joke");
            textView.setText(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
