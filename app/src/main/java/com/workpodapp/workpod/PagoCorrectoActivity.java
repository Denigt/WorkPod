package com.workpodapp.workpod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PagoCorrectoActivity extends AppCompatActivity implements View.OnClickListener {

    //XML
    private TextView tVPagoCorrectoTitulo;
    private ImageView iVTickVerde;
    private TextView tVSeleccionar;
    private LinearLayout lLEncuesta;
    private Button btnEncuesta;
    private LinearLayout lLVolverMapa;
    private Button btnVolverMapa;
    private LinearLayout lLVolverInicio;
    private Button btnVolverInicio;

    //Finalizar Reserva
    public static boolean boolReservaFinalizada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_correcto);

        //Instanciamos elementos del XML
        tVPagoCorrectoTitulo = findViewById(R.id.TVPagoCorrectoTitulo);
        iVTickVerde = findViewById(R.id.IVTickVerde);
        tVSeleccionar = findViewById(R.id.TVSeleccionar);
        lLEncuesta = findViewById(R.id.LLEncuesta);
        btnEncuesta = findViewById(R.id.BtnEncuesta);
        lLVolverMapa = findViewById(R.id.LLVolverMapa);
        btnVolverMapa = findViewById(R.id.BtnVolverMapa);
        lLVolverInicio = findViewById(R.id.LLVolverInicio);
        btnVolverInicio = findViewById(R.id.BtnVolverInicio);

        //VOLCAMOS DE NUEVO LA INFORMACIÓN ESTO ES SI QUEREMOS Q AL VOLVER NO TENGAMOS Q VOLVER A LOGGEARNOS
        dbUsuario();

        //Establecemos controles para los eventos
        btnVolverInicio.setOnClickListener(this);
        btnVolverMapa.setOnClickListener(this);
        btnEncuesta.setOnClickListener(this);

    }

    /**
     * Este método sirve de ante sala para el método de la clase Methods donde escalamos los elementos del xml.
     * En este método inicializamos las colecciones donde guardamos los elementos del xml que vamos a escalar y
     * donde especificamos el width que queremos (match_parent, wrap_content o ""(si no ponemos nada significa que
     * el elemento tiene unos dp definidos que queremos que se conserven tanto en dispositivos grandes como en pequeños.
     * También especificamos en la List el estilo de letra (bold, italic, normal) y el tamaño de la fuente del texto tanto
     * para dispositivos pequeños como para dispositivos grandes).
     * <p>
     * Como el método scale de la clase Methods no es un activity o un fragment no podemos inicializar nuestro objeto de la clase
     * DisplayMetrics con los parámetros reales de nuestro móvil, es por ello que lo inicializamos en este método.
     * <p>
     * En resumen, en este método inicializamos el metrics y las colecciones y se lo pasamos al método de la clase Methods
     */
    private void escalarElementos() {
        //INICIALIZAMOS COLECCIONES
        List<View> lstView = new ArrayList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LLENAMOS COLECCIONES
        lstView.add(tVPagoCorrectoTitulo);
        lstView.add(tVSeleccionar);
        lstView.add(iVTickVerde);
        lstView.add(lLEncuesta);
        lstView.add(lLVolverInicio);
        lstView.add(lLVolverMapa);
        lstView.add(btnEncuesta);
        lstView.add(btnVolverInicio);
        lstView.add(btnVolverMapa);

        Method.scaleViews(metrics, lstView);
        escaladoParticular(metrics);

    }

    private void escaladoParticular(DisplayMetrics metrics) {
        float height = metrics.heightPixels / metrics.density;
        iVTickVerde.getLayoutParams().height = Integer.valueOf((int) Math.round(iVTickVerde.getLayoutParams().height * (height / Method.heightEmulator)));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BtnEncuesta) {
            onClickBtnEncuesta();
        } else if (v.getId() == R.id.BtnVolverMapa) {
            onClickBtnVolverMapa();
        } else if (v.getId() == R.id.BtnVolverInicio) {
            onClickBtnVolverInicio();
        }
    }

    private void onClickBtnVolverInicio() {
        File fileLogin = getFileStreamPath(InfoApp.LOGFILE);
        if (fileLogin.delete()) {
            Intent activity = new Intent(getApplicationContext(), InitActivity.class);
            finishAffinity();
            startActivity(activity);
            finish();
        }
    }

    private void onClickBtnVolverMapa() {
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

    private void onClickBtnEncuesta() {
        Intent activity = new Intent(getApplication(), ValoracionWorkpod.class);
        activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finishAffinity();
        startActivity(activity);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}