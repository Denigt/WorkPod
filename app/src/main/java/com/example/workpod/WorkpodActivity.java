package com.example.workpod;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.workpod.fragments.Fragment_Menu_Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class WorkpodActivity extends AppCompatActivity {

    private BottomNavigationView btnNV;
    private LinearLayout LLFragment;
    private Fragment fragment;
    private FragmentTransaction fTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workpod);
        LLFragment=(LinearLayout)findViewById(R.id.LLFragment);
        btnNV = (BottomNavigationView) findViewById(R.id.btnNV);
        btnNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {
                if(menuitem.getItemId()==R.id.inv_menu_user){
                    FragmentManager fragmentManager= WorkpodActivity.this.getSupportFragmentManager();
                    //GESTIONO EL INICIO DE UNA TRANSACCIÓN PARA CARGAR EL FRAGMENTO, CADA TRANSACCIÓN ES UN CAMBIO
                    fTransaction=fragmentManager.beginTransaction();
                    //CREAMOS UN OBJETO DEL FRAGMENTO
                    Fragment_Menu_Usuario menuUsuario=new Fragment_Menu_Usuario();
                    //INCROPORO EN EL LINEAR LAYOUT EL FRAGMENT INICIAL
                    fTransaction.add(R.id.LLFragment,menuUsuario);
                    fTransaction.commit();
                }
                return true;
            }
        });
    }

}