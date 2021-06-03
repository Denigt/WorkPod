package com.example.workpod;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workpod.basic.Database;
import com.example.workpod.basic.InfoApp;
import com.example.workpod.basic.Method;
import com.example.workpod.data.Usuario;
import com.example.workpod.fragments.InfoFragment;

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
        if (v.getId() == btnSiguiente.getId()){
            // VALIDAR DATOS
            boolean error = false;

            saveActivity();
            if (email.equals(null) || email.equals("")){
                Method.showError(this, "Introduzca su email");
                txtEmail.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                error = true;
            }else if(!Method.checkEmail(email)){
                Method.showError(this, "Introduzca una dirección de email válida");
                txtEmail.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                error = true;
            }
            if (contrasena.equals(null) || contrasena.equals("")){
                Method.showError(this, "Introduzca una contraseña");
                txtContrasena.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                error = true;
            } else if(!Method.checkPassword(contrasena)) {
                Method.showError(this, "La contraseña ha de tener 10 caracteres y un símbolo");
                txtContrasena.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                error = true;
            }

            // SI NO HA HABIDO ERRORES COMPROBAR EMAIL Y CONTRASENA DEL USUARIO
            if(!error) {
                Database<Usuario> consulta = new Database<>(Database.SELECTID, new Usuario(email, contrasena));
                consulta.postRunOnUI(this, ()->{
                    if (consulta.getError().code > -1) {
                        InfoApp.USER = consulta.getDato();
                        Intent activity = new Intent(getApplicationContext(), WorkpodActivity.class);
                        startActivity(activity);
                        finish();
                    }else if (consulta.getError().code > -3) Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                    else Toast.makeText(this, "Problema al comprobar tu usuario\nIntentalo más tarde, por favor", Toast.LENGTH_LONG).show();
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
     * @param v Vista clicada
     */
    private void btnAccederOnClick(View v) {
        if (v.getId() == btnAcceder.getId()) {
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
    }

    /**
     * Guarda la actividad:
     *    Almacena en las variables de la actividad los valores de todos los campos de la misma
     */
    private void saveActivity(){
        email = txtEmail.getText().toString().trim().toLowerCase();
        contrasena = txtContrasena.getText().toString();
    }
}