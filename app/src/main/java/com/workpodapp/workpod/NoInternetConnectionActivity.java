package com.workpodapp.workpod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.fragments.InfoFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NoInternetConnectionActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iVNoInternetConecction;
    private TextView tVNoInternetConecction;
    private TextView tVCheckInternet;
    private LinearLayout lLCheckInternet;
    private Button btnBackInit;
    private Button btnReload;

    //Escaladp
    DisplayMetrics metrics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);

        //Instanciamos elementos del XML
        iVNoInternetConecction = findViewById(R.id.IVNoInternetConecction);
        tVNoInternetConecction = findViewById(R.id.TVNoInternetConecction);
        tVCheckInternet = findViewById(R.id.TVCheckInternet);
        lLCheckInternet = findViewById(R.id.LLCheckInternet);
        btnBackInit = findViewById(R.id.btnBackInit);
        btnReload = findViewById(R.id.btnReload);

        //Respuesta a los eventos
        btnReload.setOnClickListener(this);
        btnBackInit.setOnClickListener(this);

        //ECALADO
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        escalarElementos(metrics);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBackInit) {
            cerrarSesion();
        } else if (v.getId() == R.id.btnReload) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null || (networkInfo.isConnected() == false)) {
                Toast.makeText(getApplicationContext(), "Sigues sin Internet", Toast.LENGTH_LONG).show();
            } else {
                Intent activity = new Intent(getApplicationContext(), WorkpodActivity.class);
                finishAffinity();
                startActivity(activity);
                finish();
            }
        }
    }


    private void cerrarSesion() {
        File fileLogin = getFileStreamPath(InfoApp.LOGFILE);
        if (fileLogin.delete()) {
            Intent activity = new Intent(getApplicationContext(), InitActivity.class);
            finishAffinity();
            startActivity(activity);
            finish();
        }
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
     *
     * @param metrics
     */
    private void escalarElementos(DisplayMetrics metrics) {
        //INICIALIZAMOS COLECCIONES
        List<View> lstView = new ArrayList<>();

        //LLENAMOS COLECCIONES
        lstView.add(btnBackInit);
        lstView.add(btnReload);
        lstView.add(tVCheckInternet);
        lstView.add(tVNoInternetConecction);
        lstView.add(iVNoInternetConecction);
        lstView.add(lLCheckInternet);

        Method.scaleViews(metrics, lstView);

        escaladoParticular(metrics);
        InfoFragment.noInternetConnection=true;
    }

    private void escaladoParticular(DisplayMetrics metrics) {
        float height = metrics.heightPixels / metrics.density;
        iVNoInternetConecction.getLayoutParams().height = Integer.valueOf((int) Math.round(iVNoInternetConecction.getLayoutParams().height * (height / Method.heightEmulator)));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}


