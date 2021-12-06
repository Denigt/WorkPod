package com.workpodapp.workpod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.workpodapp.workpod.basic.Database;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.data.Usuario;
import com.workpodapp.workpod.fragments.InfoFragment;
import com.workpodapp.workpod.testUsuario.Informacion_Usuario;

import java.util.ArrayList;
import java.util.List;

public class ValoracionWorkpod extends AppCompatActivity implements View.OnClickListener {   
    //XML
    private ImageView iVStar1, iVStar2, iVStar3, iVStar4, iVStar5;
    private Button btnNoParticiparTest, btnParticiparTest;
    private TextView tVOpinion, tVOpinion1, tVOpinion2, tVOpinion3;
    private LinearLayout lLFragmentTest;
    public static boolean boolReservaFinalizada = false;

    //ESCALADO
    DisplayMetrics metrics;


    //GUARDAR INFORMACIÓN TEST
    public static int resultado_valoracion = 0;

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
        tVOpinion = (TextView) findViewById(R.id.TVOpinion);
        tVOpinion1 = (TextView) findViewById(R.id.TVOpinion1);
        tVOpinion2 = (TextView) findViewById(R.id.TVOpinion2);
        tVOpinion3 = (TextView) findViewById(R.id.TVOpinion3);
        btnNoParticiparTest = (Button) findViewById(R.id.BtnNoParticiparValoracion);
        btnParticiparTest = (Button) findViewById(R.id.BtnParticiparValoracion);
        lLFragmentTest = findViewById(R.id.LLFragmentTest);

        // ESTABLECER EVENTOS PARA LOS CONTROLES
        iVStar1.setOnClickListener(this);
        iVStar2.setOnClickListener(this);
        iVStar3.setOnClickListener(this);
        iVStar4.setOnClickListener(this);
        iVStar5.setOnClickListener(this);
        btnNoParticiparTest.setOnClickListener(this);
        btnParticiparTest.setOnClickListener(this);

        //VOLCAMOS DE NUEVO LA INFORMACIÓN ESTO ES SI QUEREMOS Q AL VOLVER NO TENGAMOS Q VOLVER A LOGGEARNOS
        dbUsuario();

        InfoFragment.actual = InfoFragment.VALORACION_WORKPOD;

        //Escalado
        metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        escalarElementos(metrics);

    }

    /**
     * Método para actualizar el usuario, sin tener que pasar por el loggeo de nuevo. Permitirá que se actualice el estado del workpod
     * al finalizar la sesión
     */
    private void dbUsuario() {
        Database<Usuario> consulta = new Database<>(Database.SELECTID, new Usuario(InfoApp.USER.getEmail(), InfoApp.USER.getPassword()));
        consulta.postRun(() -> {
            if (consulta.getError().code > -1) {
                InfoApp.USER = consulta.getDato();
                boolReservaFinalizada = true;
            } else if (consulta.getError().code > -3)
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Problema al comprobar tu usuario\nIntentalo más tarde, por favor", Toast.LENGTH_LONG).show();
        });
        consulta.start();
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
            resultado_valoracion = 1;
        } else if (v.getId() == R.id.IVStar2) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar3.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar4.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar5.setColorFilter(Color.parseColor("#D8D9DA"));
            resultado_valoracion = 2;
        } else if (v.getId() == R.id.IVStar3) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar3.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar4.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar5.setColorFilter(Color.parseColor("#D8D9DA"));
            resultado_valoracion = 3;
        } else if (v.getId() == R.id.IVStar4) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar3.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar4.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar5.setColorFilter(Color.parseColor("#D8D9DA"));
            resultado_valoracion = 4;
        } else if (v.getId() == R.id.IVStar5) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar3.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar4.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar5.setColorFilter(Color.parseColor("#FFEB3B"));
            resultado_valoracion = 5;
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
            try {
                while (!boolReservaFinalizada) {
                    Thread.sleep(10);
                }
                //LE INDICAMOS QUE QUEREMOS QUE VUELVA AL MAPA
                Intent activity = new Intent(getApplicationContext(), WorkpodActivity.class);
                //EVITA QUE SE DUPLIQUE EL ACTIVITY AL QUE SE VUELVE
                activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(activity);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void escalarElementos(DisplayMetrics metrics) {
        //INICIALIZAMOS COLECCIONES
        List<View> lstView = new ArrayList<>();

        //LLENAMOS COLECCIONES
        lstView.add(btnNoParticiparTest);
        lstView.add(btnParticiparTest);
        lstView.add(iVStar1);
        lstView.add(iVStar2);
        lstView.add(iVStar3);
        lstView.add(iVStar4);
        lstView.add(iVStar5);
        lstView.add(tVOpinion);
        lstView.add(tVOpinion1);
        lstView.add(tVOpinion2);
        lstView.add(tVOpinion3);
        lstView.add(lLFragmentTest);
        Method.scaleViews(metrics, lstView);
        escaladoParticular(metrics);
    }

    private void escaladoParticular(DisplayMetrics metrics) {
        float height = metrics.heightPixels / metrics.density;
        lLFragmentTest.getLayoutParams().height = Integer.valueOf((int) Math.round(lLFragmentTest.getLayoutParams().height * (height / Method.heightEmulator)));
    }

    @Override
    public void onBackPressed() {
        try {
            while (!boolReservaFinalizada) {
                Thread.sleep(10);
            }
            //LE INDICAMOS QUE QUEREMOS QUE VUELVA AL MAPA
            Intent activity = new Intent(getApplicationContext(), WorkpodActivity.class);
            //EVITA QUE SE DUPLIQUE EL ACTIVITY AL QUE SE VUELVE
            activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity);
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}