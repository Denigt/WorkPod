package com.workpodapp.workpod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.workpodapp.workpod.basic.Database;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.data.Instalacion;
import com.workpodapp.workpod.data.Usuario;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {
    // DECLARACION DE VARIABLES
    private boolean isLogon = false;
    private TextView txtVersion;
    private TextView tVDesarrolladores;

    //ESCALADO
    DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ESTABLECER LAYOUT
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // ESTABLECER VERSION
        txtVersion = findViewById(R.id.txtVersion);
        tVDesarrolladores=findViewById(R.id.TVDesarrolladores);
        txtVersion.setText(BuildConfig.VERSION_NAME);


        escalarElementos();

        // NADA MAS INICIAR LA APP OBTENER LA IDENTIFICACION DE LA APLICACION E INFORMAR A LA BD SOBRE LA INSTALACION
        InfoApp.INSTALLATION = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        InfoApp.TOPICS.add(InfoApp.INSTALLATION);
        Database<Instalacion> install = new Database<Instalacion>(Database.INSTALL, new Instalacion(getContentResolver()));
        install.postRun(()->{
            //if (install.getError().code > -1)
            for (String topic : InfoApp.TOPICS) {
                FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.e("SUBSCRIBE_TOPICS", String.format("No se ha podido suscribir %s al tema %s", InfoApp.INSTALLATION, topic));
                        }else{
                            Log.i("SUBSCRIBE_TOPICS", String.format("%s suscrito al tema %s", InfoApp.INSTALLATION, topic));
                        }
                    }
                });
            }
        });
        install.start();

        // COMPROBAR SI HAY FICHERO DE AUTOLOGIN
        File fileLogin = getFileStreamPath(InfoApp.LOGFILE);
        String[] data = new String[2];
        try {
            if (fileLogin.exists()) {
                FileInputStream loginReader = openFileInput(InfoApp.LOGFILE);
            /*
            List<Byte> aux = new ArrayList<>();
            int read = loginReader.read();
            while (read != -1){
                aux.add((byte) read);
                read = loginReader.read();
            }
            byte[] output = new byte[aux.size()];
            for (int i = 0; i<aux.size(); i++)
                output[i] = aux.get(i);
            */
                byte[] output = new byte[(int) fileLogin.length()];
                loginReader.read(output);
                data = Method.decryptAES(output, fileLogin.getAbsolutePath() + InfoApp.INSTALLATION).split("\n");
            }
        } catch (FileNotFoundException e) {
            Log.e("AUTOLOGIN", "No se puede crear el fichero");
        } catch (IOException e) {
            Log.e("AUTOLOGIN", "No se puede escribir en el fichero");
        } catch (NullPointerException e) {
            Log.e("AUTOLOGIN", "Fichero invalido");
        }

        Database<Usuario> consulta = new Database<>(Database.SELECTID, new Usuario(data[0], data[1]));
        consulta.postRunOnUI(this, ()->{
            if (consulta.getError().code > -1) {
                // SI EL FICHERO ES VALIDO ACCEDER A LA PANTALLA PRINCIPAL
                InfoApp.USER = consulta.getDato();
                if (InfoApp.USER.getInstalacion().equals(InfoApp.INSTALLATION)) {
                    isLogon = true;
                    Intent activity = new Intent(getApplicationContext(), WorkpodActivity.class);
                    startActivity(activity);
                    finish();
                }else {
                    fileLogin.delete();
                    Toast.makeText(this, "Se ha iniciado sesión en otro dispositivo\nIntroduzca sus datos para acceder", Toast.LENGTH_LONG).show();
                }
            }

            if (!isLogon){
                Intent activity = new Intent(getApplicationContext(), InitActivity.class);
                startActivity(activity);
                finish();
            }
        });
        consulta.start();

    }

    /**
     * Este método sirve de ante sala para el método de la clase Methods donde escalamos los elementos del xml.
     * En este método inicializamos las colecciones donde guardamos los elementos del xml que vamos a escalar y
     * donde especificamos el width que queremos (match_parent, wrap_content o ""(si no ponemos nada significa que
     * el elemento tiene unos dp definidos que queremos que se conserven tanto en dispositivos grandes como en pequeños.
     * También especificamos en la List el estilo de letra (bold, italic, normal) y el tamaño de la fuente del texto tanto
     * para dispositivos pequeños como para dispositivos grandes).
     *
     * Como el método scale de la clase Methods no es un activity o un fragment no podemos inicializar nuestro objeto de la clase
     * DisplayMetrics con los parámetros reales de nuestro móvil, es por ello que lo inicializamos en este método.
     *
     * En resumen, en este método inicializamos el metrics y las colecciones y se lo pasamos al método de la clase Methods
     *
     */
    private <T extends View> void escalarElementos() {
        //INICIALIZAMOS COLECCIONES
        List<T>lstView =new ArrayList<>();
        metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //LLENAMOS COLECCIONES
        lstView.add((T) txtVersion);
        lstView.add((T) tVDesarrolladores);
        Method.scaleViews(metrics, lstView);
    }
}