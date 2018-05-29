package com.example.hhj73.fix;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RatingBar;

public class Filter extends Activity {

    final  int ADDRESS = 123;
EditText address;
EditText miter;
EditText minCost;
EditText maxCost;
RatingBar help;
RatingBar pet;
RatingBar smoke;
RatingBar curfew;
float lot, lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_filter);
        //init
        address = (EditText)findViewById(R.id.FilterAddress);
        miter = (EditText)findViewById(R.id.FilterMiter);
        minCost = (EditText)findViewById(R.id.minCost);
        maxCost = (EditText)findViewById(R.id.maxCost);
        pet = (RatingBar)findViewById(R.id.RatePet);
        help = (RatingBar)findViewById(R.id.RateHelp);
        smoke = (RatingBar)findViewById(R.id.RateSmoke);
        curfew = (RatingBar)findViewById(R.id.RateCurfew);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE)
            return false;
        return true;
    }

    public void setFilter(View view) {//필터 설정 ㄱ ㄱ
        float petRate, helpRate, smokeRate, curfewRate, resultMiter, MinCost, MaxCost;
        String resultAddress;
        petRate = pet.getRating();
        helpRate = help.getRating();
        smokeRate  = smoke.getRating();
        curfewRate = curfew.getRating();
        resultMiter = Float.parseFloat(miter.getText().toString());
        MinCost = Integer.parseInt( minCost.getText().toString());
        MaxCost = Integer.parseInt(maxCost.getText().toString());

        Intent intent = new Intent(this, MatchingActivity.class);
        intent.putExtra("help",helpRate);
        intent.putExtra("curfew",curfewRate);
        intent.putExtra("smoke",smokeRate);
        intent.putExtra("pet",petRate);
        intent.putExtra("lot",lot);
        intent.putExtra("lat",lat);
        intent.putExtra("miter",resultMiter);
        intent.putExtra("minCost",MinCost);
        intent.putExtra("maxCost",MaxCost);
        setResult(RESULT_OK);
        finish();
    }

    public void searchAdd(View view) {//주소 검색
        // Toast.makeText(this, "주소 불러왔음",Toast.LENGTH_SHORT).show();      //delete
        Intent address = new Intent(this,Address.class);
        address.putExtra("activity",true);
        startActivityForResult(address, ADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADDRESS && resultCode == RESULT_OK){
                 address.setText(data.getStringExtra("ASSRESS"));
                 String location = data.getStringExtra("location");
            int idx = location.indexOf("/");
            lat = Float.parseFloat(location.substring(0, idx));
            lot = Float.parseFloat(location.substring(idx+1));
        }
    }
}
