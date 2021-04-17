package com.example.workpod.testUsuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.workpod.R;
import com.example.workpod.ValoracionWorkpod;
import com.example.workpod.Valoracion_Workpod_Final;

public class Informacion_Usuario3 extends AppCompatActivity implements View.OnClickListener {
    private Button btnTrabajarSolo, btnTrabajarEquipo;
    private ImageView iVTrabajarSolo, iVTrabajarEquipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion__usuario3);
        btnTrabajarSolo = (Button) findViewById(R.id.BtnTrabajarSolo);
        btnTrabajarEquipo = (Button) findViewById(R.id.BtnTrabajarEquipo);
        iVTrabajarEquipo = (ImageView) findViewById(R.id.IVTrabajarEquipo);
        iVTrabajarSolo = (ImageView) findViewById(R.id.IVTrabajarSolo);

        btnTrabajarEquipo.setOnClickListener(this);
        btnTrabajarSolo.setOnClickListener(this);
        iVTrabajarSolo.setOnClickListener(this);
        iVTrabajarEquipo.setOnClickListener(this);
    }

    //LISTENERS
    @Override
    public void onClick(View v) {
        callToInformacionUsuario(v);
    }

    //MÉTODOS

    /**
     * Método que permite viajar entre los diferentes layouts
     * donde el usuario contestará una serie de preguntas que nos ayudará
     * a conocerle mejor para prestar un mejor servicio
     *
     * @param v instancia de la clase View, nos permitirá comunicarnos con los botones.
     */
    private void callToInformacionUsuario(View v) {
        if (v.getId() == R.id.BtnTrabajarSolo) {
            Intent activity = new Intent(getApplicationContext(), Valoracion_Workpod_Final.class);
            //EVITA QUE SE DUPLIQUE EL ACTIVITY AL QUE SE VUELVE
            activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity);

            //GUARDAMOS EN LA MATRIZ LA RESPUESTA DEL USUARIO
            Informacion_Usuario.resultadoTest[2] = "Solo";

        } else if (v.getId() == R.id.BtnTrabajarEquipo) {
            Intent activity = new Intent(getApplicationContext(), Valoracion_Workpod_Final.class);
            //EVITA QUE SE DUPLIQUE EL ACTIVITY AL QUE SE VUELVE
            activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity);

            //GUARDAMOS EN LA MATRIZ LA RESPUESTA DEL USUARIO
            Informacion_Usuario.resultadoTest[2] = "Equipo";
        } else if (v.getId() == R.id.IVTrabajarSolo) {
            Intent activity = new Intent(getApplicationContext(), Valoracion_Workpod_Final.class);
            //EVITA QUE SE DUPLIQUE EL ACTIVITY AL QUE SE VUELVE
            activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity);
            //GUARDAMOS EN LA MATRIZ LA RESPUESTA DEL USUARIO
            Informacion_Usuario.resultadoTest[2] = "Solo";
        } else if (v.getId() == R.id.IVTrabajarEquipo) {
            Intent activity = new Intent(getApplicationContext(), Valoracion_Workpod_Final.class);
            //EVITA QUE SE DUPLIQUE EL ACTIVITY AL QUE SE VUELVE
            activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity);
        }
    }
}