package com.example.workpod;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ModPerfilActivity extends AppCompatActivity {

    private final String[] TIPOS_DOCUMENTO = {"NIF", "NIE"};

    // CONTROLES DEL LAYOUT
    Spinner spnDNI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mod_perfil);

        // BUSCAR LOS CONTROLES EN EL LAYOUT
        spnDNI = findViewById(R.id.spnDNI);

        // LLENAR EL SPINNER DEL TIPO DE DOCUMENTO
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner_basic, TIPOS_DOCUMENTO);
        spnDNI.setAdapter(adapter);
    }
}
