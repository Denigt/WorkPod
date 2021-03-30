package com.example.workpod;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.workpod.fragments.Fragment_Menu_Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
                    fragment=new Fragment_Menu_Usuario();
                    fTransaction = getSupportFragmentManager().beginTransaction();
                    fTransaction.replace(R.id.LLFragment, fragment);
                    fTransaction.addToBackStack(null);
                    fTransaction.commit();//COMMIT ES PARA CONFIRMAR UN CONJUNTO DE CAMBIOS DE MANERA PERMANENTE
                }
                return true;
            }
        });
    }

}