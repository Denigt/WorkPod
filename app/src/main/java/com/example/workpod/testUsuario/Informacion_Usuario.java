package com.example.workpod.testUsuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.workpod.R;
import com.example.workpod.Valoracion_Workpod_Final;

public class Informacion_Usuario extends AppCompatActivity implements View.OnClickListener {
    private Button btnTrabajarCuentaAjena, btnFreelance;
    public static String []resultadoTest=new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion__usuario);
        btnTrabajarCuentaAjena = (Button) findViewById(R.id.BtnTrabajarCuentaAjena);
        btnFreelance = (Button) findViewById(R.id.BtnFreelance);

        btnTrabajarCuentaAjena.setOnClickListener(this);
        btnFreelance.setOnClickListener(this);
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
        if (v.getId() == R.id.BtnTrabajarCuentaAjena) {
            Intent activity = new Intent(getApplicationContext(), Informacion_Usuario2.class);
            //EVITA QUE SE DUPLIQUE EL ACTIVITY AL QUE SE VUELVE
            activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity);

            //GUARDAMOS EN LA MATRIZ LA RESPUESTA DEL USUARIO
            resultadoTest[0]="Cuenta ajena";


        } else if (v.getId() == R.id.BtnFreelance) {
            Intent activity = new Intent(getApplicationContext(), Informacion_Usuario2.class);
            //EVITA QUE SE DUPLIQUE EL ACTIVITY AL QUE SE VUELVE
            activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity);

            //GUARDAMOS EN LA MATRIZ LA RESPUESTA DEL USUARIO
            resultadoTest[0]="Freelance";

        }
    }
}