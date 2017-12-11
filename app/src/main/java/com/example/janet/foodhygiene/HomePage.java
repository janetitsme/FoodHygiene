package com.example.janet.foodhygiene;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public void searchPosition(View view) {
        Intent intent = new Intent(HomePage.this,SearchByLatLong.class);
        startActivity(intent);
    }

    public void searchLocation(View view) {
        Intent intent=new Intent(HomePage.this,SearchByLocation.class);
        startActivity(intent);
    }

    public void searchPostCode(View view) {
        Intent intent=new Intent(HomePage.this,SearchByPostCode.class);
        startActivity(intent);
    }


}
