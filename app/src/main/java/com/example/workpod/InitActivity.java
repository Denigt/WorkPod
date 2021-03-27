package com.example.workpod;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InitActivity extends AppCompatActivity {
BottomNavigationView bNV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
       // Toast.makeText(getApplicationContext(), Build.VERSION.RELEASE,Toast.LENGTH_LONG).show();
       // bNV=(BottomNavigationView)
    }
}