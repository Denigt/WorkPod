package com.workpodapp.workpod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.workpodapp.workpod.basic.Database;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.data.Usuario;
import com.workpodapp.workpod.fragments.Fragment_Dialog_Validar_Usuario;

import java.util.ArrayList;
import java.util.List;

public class VerifyActivity extends AppCompatActivity implements View.OnClickListener {

    //XML
    private TextView tV_Verify_Titulo;
    private TextView tV_Verify_Body;
    private ImageView iV_Mail;
    private ImageView iV_Btn_Aceptar;
    private Button btnVerifyResendEmail;
    private Button btnStartOver;
    private Button btnUsuarioValidado;

    //ESCALADO
    DisplayMetrics metrics;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        //INICIALIZAMOS XML
        tV_Verify_Titulo = findViewById(R.id.TV_Verify_Titulo);
        tV_Verify_Body = findViewById(R.id.TV_Verify_Body);
        iV_Mail = findViewById(R.id.IV_Email);
        iV_Btn_Aceptar = findViewById(R.id.IV_Verify_Btn_Aceptar);
        btnStartOver = findViewById(R.id.BtnStartOver);
        btnUsuarioValidado = findViewById(R.id.BtnUsuarioValidado);
        btnVerifyResendEmail = findViewById(R.id.BtnVerifyResendEmail);

        //ESCALAMOS ELEMENTOS
        metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        escalarElementos(metrics);

        //ESTABLECEMOS EVENTOS PARA LOS CONTROLES
        btnVerifyResendEmail.setOnClickListener(this);
        btnUsuarioValidado.setOnClickListener(this);
        btnStartOver.setOnClickListener(this);
    }


    private <T extends View> void escalarElementos(DisplayMetrics metrics) {
        //INICIALIZAMOS COLECCIONES
        List<T> lstView = new ArrayList<>();

        //LLENAMOS COLECCIONES
        lstView.add((T) btnStartOver);
        lstView.add((T) btnUsuarioValidado);
        lstView.add((T) btnVerifyResendEmail);
        lstView.add((T) tV_Verify_Titulo);
        lstView.add((T) tV_Verify_Body);
        lstView.add((T) iV_Btn_Aceptar);
        lstView.add((T) iV_Mail);

        Method.scaleViews(metrics, lstView);
        escaladoParticular( metrics);
    }

    private void escaladoParticular(DisplayMetrics metrics) {
        float height = metrics.heightPixels / metrics.density;
        iV_Mail.getLayoutParams().height = Integer.valueOf((int) Math.round(iV_Mail.getLayoutParams().height * (height / Method.heightEmulator)));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BtnStartOver) {
            startOver();
        } else if (v.getId() == R.id.BtnVerifyResendEmail) {
            resendVerifyEmail();
        } else if (v.getId() == R.id.BtnUsuarioValidado) {
            verifyUser();
        }
    }

    /**
     * Comprueba si el usuario verdaderamente se ha validado. Si se ha validado, accede al mapa si no, se tiene que validar.
     * Hago un SELECTID, para que se actualice la verificación del usuario y que si al ppio sale su correo, que luego salga true.
     */
    private void verifyUser() {
        //NECESITAMOS VOLVER A VOLCAR EL USUARIO PARA QUE SE ACTUALICE QUE HA VALIDADO SU EMAIL
        Database<Usuario> consulta = new Database<>(Database.SELECTID, new Usuario(InfoApp.USER.getEmail(), InfoApp.USER.getPassword()));
        consulta.postRun(() -> {
            InfoApp.USER = consulta.getDato();
        });
        consulta.postRunOnUI(this, () -> {
            if (!InfoApp.USER.getVerificar().equalsIgnoreCase("true")) {
                Fragment_Dialog_Validar_Usuario fragment_Dialog_Validar_Usuario = new Fragment_Dialog_Validar_Usuario();
                fragment_Dialog_Validar_Usuario.show(this.getSupportFragmentManager(), "DialogValidarUsuario");
            } else {
                Intent activity = new Intent(getApplicationContext(), WorkpodActivity.class);
                finishAffinity();
                startActivity(activity);
                finish();
            }
        });
        consulta.start();
    }

    /**
     * Envía al usuario email de validación
     */
    private void resendVerifyEmail() {
        // ENVIAR CORREO DE VERIFICACION
        Database<Usuario> verificacion = new Database<>(Database.VERIFICACION, InfoApp.USER);
        verificacion.start();
        //ECO DE CORREO ENVIADO
        Toast.makeText(this, "Correo verificación vuelto a enviar", Toast.LENGTH_LONG).show();
    }

    /**
     * Volvemos al InitActivity
     */
    private void startOver() {
        Intent activity = new Intent(getApplicationContext(), InitActivity.class);
        finishAffinity();
        startActivity(activity);
        finish();
    }
}