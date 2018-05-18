package com.hhj73.a0514;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import link.fls.swipestack.SwipeStack;

public class MainActivity extends AppCompatActivity {

//    ImageView imageView;
//    TextView textView;

    ArrayList<String> m_dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        imageView = (ImageView) findViewById(R.id.image);
//        Picasso.with(this).load("http://img.gqkorea.co.kr/gq/2014/12/style_55ee8dbb59428.jpg")
//                .into(imageView);

//        Ion.with(imageView).load("https://i.pinimg.com/originals/d1/67/c6/d167c63c65b236175290284f71c124bd.gif");

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                YoYo.with(Techniques.Shake).duration(2000).playOn(imageView);
//            }
//        });


//        textView = (TextView) findViewById(R.id.text);
//        Ion.with(this).load("http://www.google.com")
//                .asString().setCallback(new FutureCallback<String>() {
//            @Override
//            public void onCompleted(Exception e, String result) {
//                textView.setText(result);
//            }
//        });

        m_dataList = new ArrayList<>();
        m_dataList.add("야옹이");
        m_dataList.add("길냥이");
        m_dataList.add("나비");
        m_dataList.add("냥냥");
        m_dataList.add("여러 이름으로 부르지");

        SwipeStack swipeStack = (SwipeStack) findViewById(R.id.swipeStack);
        swipeStack.setAdapter(new SwipeStackAdapter(m_dataList));
    }

    class SwipeStackAdapter extends BaseAdapter {

        List<String> m_data;

        public SwipeStackAdapter(List<String> data) {
            m_data = data;
        }

        @Override
        public int getCount() {
            return m_data.size();
        }

        @Override
        public Object getItem(int i) {
            return m_data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.cardview, viewGroup, false);
            TextView textView = view.findViewById(R.id.text);

            // 데이터 넣어주기
            textView.setText(m_data.get(i));

            return view;

            //return null;
        }
    }
}
