package com.example.workpod;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workpod.basic.Database;
import com.example.workpod.basic.InfoApp;
import com.example.workpod.basic.Method;
import com.example.workpod.data.Sesion;
import com.example.workpod.data.Usuario;
import com.example.workpod.scale.Scale_Buttons;
import com.example.workpod.scale.Scale_TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener {
    // VARIABLES DE LOGIN
    String email;
    String contrasena;

    // CONTROLES DEL XML
    private EditText txtEmail;
    private EditText txtContrasena;
    private ImageButton btnSiguiente;
    private ImageButton btnVolver;
    private CheckBox btnShowContrasena;
    private Button btnAcceder;
    private Button btnRegistrar;
    private TextView tVActvityLoginBienvenido;
    private TextView tVActvityLoginIniSesion;
    private TextView tVActvityLoginEmail;
    private TextView tVActvityLoginContrasena;
    private TextView tVActvityLoginAunNoLogin;
    private Sesion sesion;

    //COLECCIONES
    List<Scale_Buttons> lstBtn;
    List<Scale_TextView>lstTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity();
    }

    @Override
    protected void onPause() {
        saveActivity();
        super.onPause();
    }

    @Override
    protected void onResume() {
        initActivity();
        super.onResume();
    }

    // LISTENERS
    @Override
    public void onClick(View v) {
        btnSiguienteOnClick(v);
        btnVolverOnClick(v);
        btnAccederOnClick(v);
        btnRegistrarOnClick(v);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        editTextOnFocus(v, hasFocus);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        btnShowContrasenaOnChecked(buttonView, isChecked);
    }

    // OBTENER CHECK
    private void btnShowContrasenaOnChecked(View v, boolean isChecked){
        if(v.getId() == btnShowContrasena.getId() && isChecked){
            // MOSTRAR CONTRASENA
            txtContrasena.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }else if (v.getId() == btnShowContrasena.getId()){
            // OCULTAR CONTRASENA
            txtContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    // OBTENCION DEL FOCO
    /**
     * Retorna un EditText a sus colores por defecto si tiene el foco
     * @param v Control al que cambiar el color
     * @param hasFocus Indica si el control tiene el foco
     */
    private void editTextOnFocus(View v, boolean hasFocus){
        if(v.getClass().getName().toLowerCase().contains("edittext") && hasFocus){
            v.setBackgroundTintList(getResources().getColorStateList(R.color.editext));
        }
    }

    // CLICK DE LOS BOTONES
    /**
     * Si es la primera pantalla para registrarse abre la segunda
     * Si es la segunda pantalla, combrueba que los datos sean correctos, crea el usuario y abre
     * la aplicacion
     * @param v Vista clicada
     */
    private void btnSiguienteOnClick(View v){
        if (v.getId() == btnSiguiente.getId() || v.getId() == btnAcceder.getId()){
            // VALIDAR DATOS
            boolean error = false;

            saveActivity();
            if (email.equals(null) || email.equals("")){
                Method.showError(this, "Introduzca su email");
                txtEmail.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                error = true;
            }else if(!Method.checkEmail(email)){
                Method.showError(this, "Introduzca una direcci??n de email v??lida");
                txtEmail.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                error = true;
            }
            if (contrasena.equals(null) || contrasena.equals("")){
                Method.showError(this, "Introduzca una contrase??a");
                txtContrasena.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                error = true;
            } else if(!Method.checkPassword(contrasena)) {
                Method.showError(this, "La contrase??a ha de tener 10 caracteres y un s??mbolo");
                txtContrasena.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                error = true;
            }

            // SI NO HA HABIDO ERRORES COMPROBAR EMAIL Y CONTRASENA DEL USUARIO
            if(!error) {
                Database<Usuario> consulta = new Database<>(Database.SELECTID, new Usuario(email, contrasena));
                consulta.postRun(()->{
                    if (consulta.getError().code > -1) {
                        try {
                            String input = String.format("%s\n%s", consulta.getDato().getEmail(), consulta.getDato().getPassword());

                            File fileLogin = getFileStreamPath(InfoApp.LOGFILE);
                            fileLogin.createNewFile();

                            FileOutputStream loginWriter = openFileOutput(InfoApp.LOGFILE, Context.MODE_PRIVATE);
                            loginWriter.write(Method.encryptAES(input, fileLogin.getAbsolutePath() + InfoApp.INSTALLATION));
                            loginWriter.close();
                        } catch (FileNotFoundException e) {
                            Log.e("AUTOLOGIN", "No se puede crear el fichero");
                        } catch (IOException e) {
                            Log.e("AUTOLOGIN", "No se puede escribir en el fichero");
                        }
                    }
                });

                consulta.postRunOnUI(this, ()->{
                    if (consulta.getError().code > -1) {
                        InfoApp.USER = consulta.getDato();
                        InfoApp.USER.setInstalacion(InfoApp.INSTALLATION);
                    // Actualizacion de la instalacion del usuario en la base de datos
                        Database<Usuario> update = new Database<>(Database.UPDATE, InfoApp.USER);
                        update.start();

                        Intent activity = new Intent(getApplicationContext(), WorkpodActivity.class);
                        finishAffinity();
                        startActivity(activity);
                        finish();
                    }else if (consulta.getError().code > -3) Toast.makeText(this, "Usuario o contrase??a incorrectos", Toast.LENGTH_LONG).show();
                    else Toast.makeText(this, "Problema al comprobar tu usuario\nIntentalo m??s tarde, por favor", Toast.LENGTH_LONG).show();
                });
                consulta.start();
            }
        }
    }

    /**
     * Cierra la ventana de Login
     * @param v Vista clicada
     */
    private void btnVolverOnClick(View v){
        if (v.getId() == btnVolver.getId()){
            finish();
        }
    }

    /**
     * Lleva al usuario a la aplicacion principal como usuario no registrado
     * Ahora mismo sin uso
     * @param v Vista clicada
     */
    private void btnAccederOnClick(View v) {
        if (false) {
            Intent activity = new Intent(getApplicationContext(), WorkpodActivity.class);
            startActivity(activity);
            finish();
        }
    }

    /**
     * Lleva al usuario a la pantalla para registrase
     * @param v Vista clicada
     */
    private void btnRegistrarOnClick(View v) {
        if (v.getId() == btnRegistrar.getId()) {
            Intent activity = new Intent(getApplicationContext(), SigninActivity.class);
            startActivity(activity);
            finish();
        }
    }

    // OTROS METODOS
    /**
     * Inicializa la actividad:
     *    Elige el layout de la actividad
     *    Establece los valores de los controles de la actividad
     *    Anade los Listeners a los controles generados
     */
    private void initActivity(){
        // ESTABLECER LAYOUT DE LA ACTIVIDAD
        setContentView(R.layout.activity_login);
        // BUSCAR LOS CONTROLES DEL XML
        txtEmail = findViewById(R.id.txtEmail);
        txtEmail.setText(email);
        txtContrasena = findViewById(R.id.txtContrasena);
        txtContrasena.setText(contrasena);
        btnShowContrasena = findViewById(R.id.btnShowContrasena);

        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnVolver = findViewById(R.id.btnVolver);
        btnAcceder = findViewById(R.id.btnAcceder);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        tVActvityLoginAunNoLogin=findViewById(R.id.tVActvityLoginAunNoLogin);
        tVActvityLoginBienvenido=findViewById(R.id.tVActvityLoginBienvenido);
        tVActvityLoginContrasena=findViewById(R.id.tVActvityLoginContrasena);
        tVActvityLoginEmail=findViewById(R.id.tVActvityLoginEmail);
        tVActvityLoginIniSesion=findViewById(R.id.tVActvityLoginIniSesion);

        // ESTABLECER EVENTOS PARA LOS CONTROLES
        txtEmail.setOnFocusChangeListener(this);
        txtContrasena.setOnFocusChangeListener(this);
        btnShowContrasena.setOnCheckedChangeListener(this);

        btnSiguiente.setOnClickListener(this);
        btnVolver.setOnClickListener(this);
        btnAcceder.setOnClickListener(this);
        btnRegistrar.setOnClickListener(this);

        // DIBUJAR FOREGROUND SI LA VERSION ES MENOR A LA 23
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            FrameLayout lyt = findViewById(R.id.lytForeground1);
            lyt.setBackground(getDrawable(R.drawable.rounded_border_button));
        }

        //ESCALAMOS ELEMENTOS
        escalarElementos();
    }

    /**
     * Guarda la actividad:
     *    Almacena en las variables de la actividad los valores de todos los campos de la misma
     */
    private void saveActivity(){
        email = txtEmail.getText().toString().trim().toLowerCase();
        contrasena = txtContrasena.getText().toString();
    }

    /**
     * Este m??todo sirve de ante sala para el m??todo de la clase Methods donde escalamos los elementos del xml.
     * En este m??todo inicializamos las colecciones donde guardamos los elementos del xml que vamos a escalar y
     * donde especificamos el width que queremos (match_parent, wrap_content o ""(si no ponemos nada significa que
     * el elemento tiene unos dp definidos que queremos que se conserven tanto en dispositivos grandes como en peque??os.
     * Tambi??n especificamos en la List el estilo de letra (bold, italic, normal) y el tama??o de la fuente del texto tanto
     * para dispositivos peque??os como para dispositivos grandes).
     *
     * Como el m??todo scale de la clase Methods no es un activity o un fragment no podemos inicializar nuestro objeto de la clase
     * DisplayMetrics con los par??metros reales de nuestro m??vil, es por ello que lo inicializamos en este m??todo.
     *
     * En resumen, en este m??todo inicializamos el metrics y las colecciones y se lo pasamos al m??todo de la clase Methods
     *
     */
    private void escalarElementos() {
       //INICIALIZAMOS COLECCIONES
        this.lstBtn=new ArrayList<>();
        this.lstTv=new ArrayList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LLENAMOS COLECCIONES
        lstBtn.add(new Scale_Buttons(btnAcceder,"Match_Parent","bold",12,12,15));
        lstBtn.add(new Scale_Buttons(btnRegistrar,"Match_Parent","bold",12,12,15));

        lstTv.add(new Scale_TextView(tVActvityLoginAunNoLogin,"Match_Parent","bold",20,20,24));
        lstTv.add(new Scale_TextView(tVActvityLoginBienvenido,"Match_Parent","bold",34,34,34));
        lstTv.add(new Scale_TextView(tVActvityLoginContrasena,"Match_Parent","bold",18,18,18));
        lstTv.add(new Scale_TextView(tVActvityLoginEmail,"Match_Parent","bold",18,18,18));
        lstTv.add(new Scale_TextView(tVActvityLoginIniSesion,"Match_Parent","bold",24,24,24));

        Method.scaleBtns(metrics, lstBtn);
        Method.scaleTv(metrics, lstTv);

        //LLENAMOS COLECCIONES
        Method.scaleBtns(metrics, lstBtn);
        Method.scaleTv(metrics,lstTv);
    }
}