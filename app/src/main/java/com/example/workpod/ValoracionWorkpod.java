package com.example.workpod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.workpod.testUsuario.Informacion_Usuario;

public class ValoracionWorkpod extends AppCompatActivity implements View.OnClickListener {
    //XML
    private ImageView iVStar1, iVStar2, iVStar3, iVStar4, iVStar5;
    private Button btnNoParticiparTest, btnParticiparTest;

    //GUARDAR INFORMACIÓN TEST
    public static int resultado_valoracion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valoracion_workpod);

        //INSTANCIAS DE LOS ELEMENTOS DEL XML
        iVStar1 = (ImageView) findViewById(R.id.IVStar1);
        iVStar2 = (ImageView) findViewById(R.id.IVStar2);
        iVStar3 = (ImageView) findViewById(R.id.IVStar3);
        iVStar4 = (ImageView) findViewById(R.id.IVStar4);
        iVStar5 = (ImageView) findViewById(R.id.IVStar5);
        btnNoParticiparTest = (Button) findViewById(R.id.BtnNoParticiparValoracion);
        btnParticiparTest = (Button) findViewById(R.id.BtnParticiparValoracion);

        // ESTABLECER EVENTOS PARA LOS CONTROLES
        iVStar1.setOnClickListener(this);
        iVStar2.setOnClickListener(this);
        iVStar3.setOnClickListener(this);
        iVStar4.setOnClickListener(this);
        iVStar5.setOnClickListener(this);
        btnNoParticiparTest.setOnClickListener(this);
        btnParticiparTest.setOnClickListener(this);


    }

    //MÉTODOS SOBREESCRITOS
    @Override
    public void onClick(View v) {
        callToActionStars(v);
        callToActionButtonsTest(v);
    }


    //MÉTODOS

    /**
     * Método para cambiar de color las estrellas
     * cuando el usuario la seleccione
     *
     * @param v instancia de la clase View con las que conectamos
     *          con cada estrella
     */
    private void callToActionStars(View v) {
        if (v.getId() == R.id.IVStar1) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar3.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar4.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar5.setColorFilter(Color.parseColor("#D8D9DA"));
            resultado_valoracion=1;
        } else if (v.getId() == R.id.IVStar2) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar3.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar4.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar5.setColorFilter(Color.parseColor("#D8D9DA"));
            resultado_valoracion=2;
        } else if (v.getId() == R.id.IVStar3) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar3.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar4.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar5.setColorFilter(Color.parseColor("#D8D9DA"));
            resultado_valoracion=3;
        } else if (v.getId() == R.id.IVStar4) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar3.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar4.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar5.setColorFilter(Color.parseColor("#D8D9DA"));
            resultado_valoracion=4;
        } else if (v.getId() == R.id.IVStar5) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar3.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar4.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar5.setColorFilter(Color.parseColor("#FFEB3B"));
            resultado_valoracion=5;
        }
    }



    /**
     * Método donde se encuentra la acción de los botones de participar en el test
     * que se realiza a los usuarios o la del botón de declinar la opción de participar
     * Si accionas el botón de participar, te lleva a los layouts donde se encuentran las preguntas
     * y si accionas el botón de no participar, te devuelve al activity inicial.
     *
     * @param v instancia de la clase View, nos permitirá comunicarnos con los botones.
     */
    private void callToActionButtonsTest(View v) {
        if (v.getId() == R.id.BtnParticiparValoracion) {
            Intent activity = new Intent(getApplicationContext(), Informacion_Usuario.class);
            //EVITA QUE SE DUPLIQUE EL ACTIVITY AL QUE SE VUELVE
            activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity);
        } else if (v.getId() == R.id.BtnNoParticiparValoracion) {
            Intent activity = new Intent(getApplicationContext(), InitActivity.class);
            activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity);
        }
    }
}