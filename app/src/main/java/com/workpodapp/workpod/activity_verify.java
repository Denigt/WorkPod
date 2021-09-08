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
import com.workpodapp.workpod.scale.Scale_Buttons;
import com.workpodapp.workpod.scale.Scale_Image_View;
import com.workpodapp.workpod.scale.Scale_TextView;

import java.util.ArrayList;
import java.util.List;

public class activity_verify extends AppCompatActivity implements View.OnClickListener {

    //XML
    private TextView tV_Verify_Titulo;
    private TextView tV_Verify_Body;
    private ImageView iV_Mail;
    private ImageView iV_Btn_Aceptar;
    private Button btnVerifyResendEmail;
    private Button btnStartOver;
    private Button btnUsuarioValidado;

    //ESCALADO
    DisplayMetrics metrics;
    float width;
    //COLECCIONES
    List<Scale_Buttons> lstBtn;
    List<Scale_TextView> lstTv;
    List<Scale_Image_View> lstIv;

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
        width = metrics.widthPixels / metrics.density;
        escalarElementos(metrics);
    }


    private void escalarElementos(DisplayMetrics metrics) {
        //INICIALIZAMOS COLECCIONES
        this.lstBtn = new ArrayList<>();
        this.lstTv = new ArrayList<>();
        this.lstIv = new ArrayList<>();

        //LLENAMOS COLECCIONES
        lstBtn.add(new Scale_Buttons(btnStartOver, "wrap_content", "bold", 15, 15, 18));
        lstBtn.add(new Scale_Buttons(btnUsuarioValidado, "wrap_content", "bold", 14, 15, 18));
        lstBtn.add(new Scale_Buttons(btnVerifyResendEmail, "wrap_content", "bold", 15, 15, 18));
        lstTv.add(new Scale_TextView(tV_Verify_Titulo, "match_parent", "bold", 20, 25, 30));
        lstTv.add(new Scale_TextView(tV_Verify_Body, "match_parent", "", 16, 16, 23));
        lstIv.add(new Scale_Image_View(iV_Btn_Aceptar, 180, 80, 270, 120, 350, 250, "", ""));
        lstIv.add(new Scale_Image_View(iV_Mail, 750, 150, 420, 220, 620, 420, "", ""));

        Method.scaleBtns(metrics, lstBtn);
        Method.scaleTv(metrics, lstTv);
        Method.scaleIv(metrics, lstIv);

        //ESTABLECEMOS EVENTOS PARA LOS CONTROLES
        btnVerifyResendEmail.setOnClickListener(this);
        btnUsuarioValidado.setOnClickListener(this);
        btnStartOver.setOnClickListener(this);
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
        consulta.postRun(()->{
            InfoApp.USER=consulta.getDato();
        });
        consulta.postRunOnUI(this,()->{
            if(!InfoApp.USER.getVerificar().equalsIgnoreCase("true")){
                Fragment_Dialog_Validar_Usuario fragment_Dialog_Validar_Usuario = new Fragment_Dialog_Validar_Usuario();
                fragment_Dialog_Validar_Usuario.show(this.getSupportFragmentManager(), "DialogValidarUsuario");
            }else{
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