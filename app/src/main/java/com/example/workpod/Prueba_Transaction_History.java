package com.example.workpod;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workpod.basic.Database;
import com.example.workpod.data.Sesion;
import com.example.workpod.data.Workpod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Prueba_Transaction_History extends AppCompatActivity {

    private TextView tvPruebas;
    //ALMACENAMIENTO DE DATOS
    private List<Sesion> lstSesiones = new ArrayList<>();
    // INFORMAR DE QUE TODOS LOS HILOS HAN DE FINALIZAR
    private boolean killHilos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba__transaction__history);
        tvPruebas = (TextView) findViewById(R.id.TVPruebasTransactionHistory);
        try {
            Database<Sesion> dbSesion = new Database<>(Database.SELECTALL, new Sesion());
            dbSesion.postRun(() -> {
                lstSesiones.addAll(dbSesion.getLstSelect());
            });
            dbSesion.postRunOnUI(this, () -> {
                mostrarSesiones();
            });
            dbSesion.start();
        } catch (NullPointerException e) {

        }



    }

    private void mostrarSesiones() {
        String eco = "";
        for (Sesion sesion : lstSesiones) {
            eco += sesion.getID() + "\n" +
                    sesion.getEntrada() + "\n" +
                    sesion.getSalida() + "\n" +
                    sesion.getTiempo() + "\n" +
                    sesion.getPrecio() + "\n" +
                    sesion.getDescuento() + "\n" +
                    "-----------------------------------------\n";

        }
        tvPruebas.setText(eco);


    }

}