package com.example.workpod;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

public class WebActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnCancelar;
    private WebView wvNavegador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        String web = getIntent().getStringExtra("web");

        btnCancelar = findViewById(R.id.btnCancelar);
        wvNavegador = findViewById(R.id.wvNavegador);

        btnCancelar.setOnClickListener(this);

        wvNavegador.loadUrl(web);
    }

    @Override
    public void onClick(View v) {
        btnCancelarOnClick(v);
    }

    /**
     * Cierra la ventana de Login
     * @param v Vista clicada
     */
    private void btnCancelarOnClick(View v){
        if (v.getId() == btnCancelar.getId()){
            finish();
        }
    }

}