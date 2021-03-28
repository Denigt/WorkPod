package com.example.workpod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InitActivity extends AppCompatActivity implements View.OnClickListener {

    // CONTROLES DEL XML
    private Button btnAcceder;
    private Button btnConectar;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        // BUSCAR LOS CONTROLES DEL XML
        btnAcceder = findViewById(R.id.btnAcceder);
        btnConectar = findViewById(R.id.btnConectar);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        // ESTABLECER EVENTOS PARA LOS CONTROLES
        btnAcceder.setOnClickListener(this);
        btnConectar.setOnClickListener(this);
        btnRegistrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        btnAccederOnClick(v);
        btnConectarOnClick(v);
        btnRegistrarOnClick(v);
    }

    // CLICK DE LOS BOTONES
    private void btnAccederOnClick(View v){
        if(v.getId() == btnAcceder.getId()){
            // PROVISIONAL PARA NO CAMBIAR EL MANIFIEST
            Intent activity = new Intent(getApplicationContext(), WorkpodActivity.class);
            startActivity(activity);
        }
    }

    private void btnConectarOnClick(View v){
        if(v.getId() == btnConectar.getId()){

        }
    }

    /**
     * Inicia una actividad para registrarse al pulsar "Registrarse"
     * @param v Vista clicada
     */
    private void btnRegistrarOnClick(View v){
        if(v.getId() == btnRegistrar.getId()){
            Intent activity = new Intent(getApplicationContext(), SigninActivity.class);
            // Indicar la pantalla de registro
            activity.putExtra("screen", 0);
            startActivity(activity);
        }
    }
}