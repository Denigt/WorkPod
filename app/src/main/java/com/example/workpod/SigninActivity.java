package com.example.workpod;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    // VARIABLES GLOBALES DE LA ACTIVIDAD
    int pantalla;

    // CONTROLES DEL XML
    private EditText txtNombre;
    private EditText txtApellidos;
    private EditText txtEmail;
    private EditText txtContrasena;
    private EditText txtRContrasena;
    private ImageButton btnSiguiente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fragment_open_enter, R.anim.fragment_close_enter);

        pantalla = getIntent().getIntExtra("screen", 0);

        if (pantalla == 0)
            setContentView(R.layout.activity_signin);
        else
            setContentView(R.layout.activity_signin2);

        // BUSCAR LOS CONTROLES DEL XML
        if(pantalla == 0) {
            txtNombre = findViewById(R.id.txtNombre);
            txtApellidos = findViewById(R.id.txtApellido);
        }else {
            txtEmail = findViewById(R.id.txtEmail);
            txtContrasena = findViewById(R.id.txtContrasena);
            txtRContrasena = findViewById(R.id.txtRContrasena);
        }
        btnSiguiente = findViewById(R.id.btnSiguiente);

        // ESTABLECER EVENTOS PARA LOS CONTROLES
        /*if(pantalla != 0) {
            txtContrasena.setOnFocusChangeListener(this);
            txtRContrasena.setOnFocusChangeListener(this);
        }*/
        btnSiguiente.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        if(pantalla == 1) {
            Intent activity = new Intent(getApplicationContext(), SigninActivity.class);
            // Indicar la pantalla de registro
            activity.putExtra("screen", 0);
            startActivity(activity);
            finish();
        }
        super.onPause();
    }

    // LISTENERS
    @Override
    public void onClick(View v) {
        btnSiguienteOnClick(v);
    }

    // CLICK DE LOS BOTONES
    /**
     * Si es la primera pantalla para registrarse abre la segunda
     * Si es la segunda pantalla, combrueba que los datos sean correctos, crea el usuario y abre
     * la aplicacion
     * @param v Vista clicada
     */
    private void btnSiguienteOnClick(View v){
        if(v.getId() == btnSiguiente.getId()){
            if(pantalla == 0) {
                Intent activity = new Intent(getApplicationContext(), SigninActivity.class);
                // Indicar la pantalla de registro
                activity.putExtra("screen", 1);
                startActivity(activity);
                finish();
            }
        }
    }
}