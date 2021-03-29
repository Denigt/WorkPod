package com.example.workpod;

import com.example.workpod.basic.*;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener {
    // VARIABLES DE SIGNIN
    int pantalla;
    String nombre;
    String apellido;
    String email;
    String contrasena;

    // CONTROLES DEL XML
    private EditText txtNombre;
    private EditText txtApellido;
    private EditText txtEmail;
    private EditText txtContrasena;
    private EditText txtRContrasena;
    private ImageButton btnSiguiente;
    private CheckBox btnShowContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pantalla = 0;
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

    @Override
    public void onBackPressed() {
        if(pantalla == 0) {
            super.onBackPressed();
        }else
        {
            saveActivity();
            pantalla = 0;
            initActivity();
        }
    }

    // LISTENERS
    @Override
    public void onClick(View v) {
        btnSiguienteOnClick(v);
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
            txtRContrasena.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }else if (v.getId() == btnShowContrasena.getId()){
            // OCULTAR CONTRASENA
            txtContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            txtRContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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
            // VALIDAR DATOS DE LA PRIMERA PANTALLA
            if(pantalla == 0) {
                boolean error = false;

                saveActivity();
                if (nombre.equals(null) || nombre.equals("")){
                    Method.showError(this, "Introduzca su nombre");
                    txtNombre.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                    error = true;
                }
                if (apellido.equals(null) || apellido.equals("")){
                    Method.showError(this, "Introduzca su apellido");
                    txtApellido.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                    error = true;
                }
                if(!error) {
                    pantalla = 1;
                    initActivity();
                }
            }
            // VALIDAR DATOS DE LA SEGUNDA PANTALLA
            else {
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
                }else if (!contrasena.equals(txtRContrasena.getText().toString())){
                    Method.showError(this, "Las contraseñas no coinciden");
                    txtRContrasena.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                    error = true;
                }
                if(!error) {
                    Intent activity = new Intent(getApplicationContext(), WorkpodActivity.class);
                    startActivity(activity);
                    //finish();
                }
            }
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
        if (pantalla == 0)
            setContentView(R.layout.activity_signin);
        else
            setContentView(R.layout.activity_signin2);

        // BUSCAR LOS CONTROLES DEL XML
        if(pantalla == 0) {
            txtNombre = findViewById(R.id.txtNombre);
            txtNombre.setText(nombre);
            txtApellido = findViewById(R.id.txtApellido);
            txtApellido.setText(apellido);
        }else {
            txtEmail = findViewById(R.id.txtEmail);
            txtEmail.setText(email);
            txtContrasena = findViewById(R.id.txtContrasena);
            txtContrasena.setText(contrasena);
            txtRContrasena = findViewById(R.id.txtRContrasena);
            btnShowContrasena = findViewById(R.id.btnShowContrasena);
        }
        btnSiguiente = findViewById(R.id.btnSiguiente);

        // ESTABLECER EVENTOS PARA LOS CONTROLES
        if (pantalla == 0){
            txtNombre.setOnFocusChangeListener(this);
            txtApellido.setOnFocusChangeListener(this);
        }
        else {
            txtEmail.setOnFocusChangeListener(this);
            txtContrasena.setOnFocusChangeListener(this);
            txtRContrasena.setOnFocusChangeListener(this);
            btnShowContrasena.setOnCheckedChangeListener(this);
        }
        btnSiguiente.setOnClickListener(this);
    }

    /**
     * Guarda la actividad:
     *    Almacena en las variables de la actividad los valores de todos los campos de la misma
     */
    private void saveActivity(){
        if (pantalla == 0) {
            nombre = txtNombre.getText().toString();
            apellido = txtApellido.getText().toString();
        }else {
            email = txtEmail.getText().toString();
            contrasena = txtContrasena.getText().toString();
        }
    }
}