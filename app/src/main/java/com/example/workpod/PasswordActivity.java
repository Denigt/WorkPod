package com.example.workpod;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.workpod.basic.Database;
import com.example.workpod.basic.Method;
import com.example.workpod.data.Usuario;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txtEmail;
    Button btnRecuperar;
    Button btnBack;
    ImageButton btnVolver;

    LinearLayout lytSolicitud;
    LinearLayout lytInformacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String email = getIntent().getStringExtra("email");
        setContentView(R.layout.activity_password);

        txtEmail = findViewById(R.id.txtEmail);
        btnRecuperar = findViewById(R.id.btnRecuperar);
        btnBack = findViewById(R.id.btnBack);
        btnVolver = findViewById(R.id.btnVolver);

        lytSolicitud = findViewById(R.id.lytSolicitud);
        lytInformacion = findViewById(R.id.lytInformacion);

        txtEmail.setText(email);
        lytSolicitud.setVisibility(View.VISIBLE);
        lytInformacion.setVisibility(View.GONE);

        btnRecuperar.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnVolver.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnBack.getId() || v.getId() == btnVolver.getId())
            finish();
        else {
            btnRecuperarOnClick(v);
        }
    }

    private void btnRecuperarOnClick(View v){
        String email = txtEmail.getText().toString().trim().toLowerCase();
        if (v.getId() == btnRecuperar.getId()){
            if (!Method.checkEmail(email)) {
                Method.showError(this, "El email no tiene un formato válido");
                txtEmail.setBackgroundTintList(getResources().getColorStateList(R.color.red));
            }else {
                Database<Usuario> reset = new Database<>(Database.RESET, new Usuario(email, ""));
                reset.postRunOnUI(this, ()->{
                    lytSolicitud.setVisibility(View.GONE);
                    lytInformacion.setVisibility(View.VISIBLE);
                });
                reset.start();
            }
        }
    }
}