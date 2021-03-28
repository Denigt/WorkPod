package com.example.workpod;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    // VARIABLES DE SIGNIN
    int pantalla;
    String nombre;
    String apellido;
    String email;
    String contrasena;

    // CONTROLES DEL XML
    private EditText txtNombre;
    private EditText txtApellido;
    private EditText txtEmail;
    private EditText txtContrasena;
    private EditText txtRContrasena;
    private ImageButton btnSiguiente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pantalla = getIntent().getIntExtra("screen", 0);
        nombre  = getIntent().getStringExtra("name");
        apellido  = getIntent().getStringExtra("surname");
        email  = getIntent().getStringExtra("email");
        contrasena  = getIntent().getStringExtra("pass");

        if (pantalla == 0)
            setContentView(R.layout.activity_signin);
        else
            setContentView(R.layout.activity_signin2);

        // BUSCAR LOS CONTROLES DEL XML
        if(pantalla == 0) {
            txtNombre = findViewById(R.id.txtNombre);
            txtNombre.setText(nombre);
            txtApellido = findViewById(R.id.txtApellido);
            txtApellido.setText(apellido);
        }else {
            txtEmail = findViewById(R.id.txtEmail);
            txtEmail.setText(email);
            txtContrasena = findViewById(R.id.txtContrasena);
            txtContrasena.setText(contrasena);
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
    protected void onDestroy() {
        if(pantalla == 1) {
            Intent activity = new Intent(getApplicationContext(), SigninActivity.class);
            // Indicar la pantalla de registro
            activity.putExtra("screen", 0);
            // Guardar datos de los campos
            activity.putExtra("name", nombre);
            activity.putExtra("surname", apellido);
            activity.putExtra("email", txtEmail.getText().toString());
            activity.putExtra("pass", txtContrasena.getText().toString());
            startActivity(activity);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
        super.onDestroy();
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
                // Guardar los datos de los campos
                activity.putExtra("name", txtNombre.getText().toString());
                activity.putExtra("surname", txtApellido.getText().toString());
                activity.putExtra("email", email);
                activity.putExtra("pass", contrasena);
                startActivity(activity);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                finish();
            }
        }
    }
}