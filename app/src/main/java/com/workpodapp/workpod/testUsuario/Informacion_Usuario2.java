package com.workpodapp.workpod.testUsuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.workpodapp.workpod.R;

public class Informacion_Usuario2 extends AppCompatActivity implements View.OnClickListener {
    private Button btnTrabajoFijo, btnNomada;
    private ImageView iVPantalla, iVPortatil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion__usuario2);
        btnTrabajoFijo = (Button) findViewById(R.id.BtnTrabajoFijo);
        btnNomada = (Button) findViewById(R.id.BtnNomada);
        iVPantalla=(ImageView)findViewById(R.id.IVPantalla);
        iVPortatil=(ImageView)findViewById(R.id.IVPortatil);

        btnTrabajoFijo.setOnClickListener(this);
        btnNomada.setOnClickListener(this);
        iVPortatil.setOnClickListener(this);
        iVPantalla.setOnClickListener(this);
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
        if (v.getId() == R.id.BtnTrabajoFijo) {
            Intent activity = new Intent(getApplicationContext(), Informacion_Usuario3.class);
            //EVITA QUE SE DUPLIQUE EL ACTIVITY AL QUE SE VUELVE
            activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity);
            //GUARDAMOS EN LA MATRIZ LA RESPUESTA DEL USUARIO
            Informacion_Usuario.resultadoTest[1] = "Trabaja fijo";
        } else if (v.getId() == R.id.BtnNomada) {
            Intent activity = new Intent(getApplicationContext(), Informacion_Usuario3.class);
            //EVITA QUE SE DUPLIQUE EL ACTIVITY AL QUE SE VUELVE
            activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity);
            //GUARDAMOS EN LA MATRIZ LA RESPUESTA DEL USUARIO
            Informacion_Usuario.resultadoTest[1] = "Es Nomada";

        }else if(v.getId()==R.id.IVPantalla){
            Intent activity = new Intent(getApplicationContext(), Informacion_Usuario3.class);
            //EVITA QUE SE DUPLIQUE EL ACTIVITY AL QUE SE VUELVE
            activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity);
        }else if(v.getId()==R.id.IVPortatil){
            Intent activity = new Intent(getApplicationContext(), Informacion_Usuario3.class);
            //EVITA QUE SE DUPLIQUE EL ACTIVITY AL QUE SE VUELVE
            activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity);
            //GUARDAMOS EN LA MATRIZ LA RESPUESTA DEL USUARIO
            Informacion_Usuario.resultadoTest[1] = "Es Nomada";

        }
    }
}