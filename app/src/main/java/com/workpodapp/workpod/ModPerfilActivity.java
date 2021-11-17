package com.workpodapp.workpod;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.workpodapp.workpod.basic.Database;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.data.Usuario;
import com.workpodapp.workpod.scale.Scale_Buttons;
import com.workpodapp.workpod.scale.Scale_TextView;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class ModPerfilActivity extends AppCompatActivity implements View.OnClickListener {

    private final String[] TIPOS_DOCUMENTO = {"NIF", "NIE"};

    // CONTROLES DEL LAYOUT
    private Spinner spnDNI;
    private EditText txtNombre;
    private EditText txtApellido;
    private EditText txtDNI;
    private EditText txtEmail;
    private TextView tVActModPerfilTitulo;
    private TextView tVActModPerfilPregNombre;
    private TextView tVActModPerfilNombre;
    private TextView tVActModPerfilApellidos;
    private TextView tVActModPerfilNIFNIE;
    private TextView tVActModPerfilPregEmail;
    private TextView tVActModPerfilEmail;
    private ImageButton btnGuardar;
    private ImageButton btnCancelar;

    //COLECCIONES
    List<Scale_Buttons> lstBtn;
    List<Scale_TextView> lstTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mod_perfil);

        // BUSCAR LOS CONTROLES EN EL LAYOUT
        spnDNI = findViewById(R.id.spnDNI);
        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtDNI = findViewById(R.id.txtDNI);
        txtEmail = findViewById(R.id.txtEmail);
        tVActModPerfilApellidos = findViewById(R.id.tVActModPerfilApellidos);
        tVActModPerfilEmail = findViewById(R.id.tVActModPerfilEmail);
        tVActModPerfilNIFNIE = findViewById(R.id.tVActModPerfilNIFNIE);
        tVActModPerfilNombre = findViewById(R.id.tVActModPerfilNombre);
        tVActModPerfilPregEmail = findViewById(R.id.tVActModPerfilPregEmail);
        tVActModPerfilPregNombre = findViewById(R.id.tVActModPerfilPregNombre);
        tVActModPerfilTitulo = findViewById(R.id.tVActModPerfilTitulo);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        // LISTENERS
        btnGuardar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        // LLENAR EL SPINNER DEL TIPO DE DOCUMENTO
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner_basic, TIPOS_DOCUMENTO);
        spnDNI.setAdapter(adapter);

        // INICIALIZAR DATOS DEL FRAGMENT SI HAY UN USUARIO REGISTRADO
        if (InfoApp.USER != null) {
            txtNombre.setHint(InfoApp.USER.getNombre());
            txtApellido.setHint(InfoApp.USER.getApellidos());
            txtEmail.setHint(InfoApp.USER.getEmail());
            txtDNI.setHint(InfoApp.USER.getDni());
            //txtDNI.setText(InfoApp.USER.getTelefono());
        }
        //ESCALAMOS ELEMENTOS
        escalarElementos();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnCancelar.getId()) {
            finish();
        } else if (InfoApp.USER != null && v.getId() == btnGuardar.getId()) {
            Usuario modUser = new Usuario();

            modUser.set(InfoApp.USER);
            if (!txtNombre.getText().toString().equals(""))
                modUser.setNombre(txtNombre.getText().toString());

            if (!txtApellido.getText().toString().equals(""))
                modUser.setApellidos(txtApellido.getText().toString());

            if (Method.checkEmail(txtEmail.getText().toString())) {
                modUser.setVerificar(txtEmail.getText().toString());
                modUser.setfVerificacion(ZonedDateTime.now());
                Toast.makeText(getApplicationContext(), "Recuerda verificar el nuevo Email para comenzar a usarlo", Toast.LENGTH_LONG).show();
            }

            //if (Method.checkDNI(txtDNI.getText().toString()))
            if (!txtDNI.getText().toString().equals(""))
                modUser.setDni(txtDNI.getText().toString());

            Database<Usuario> update = new Database<>(Database.UPDATE, modUser);
            update.postRunOnUI(this, () -> {
                if (update.getError().code > -1) {
                    Toast.makeText(this, "Datos modificados", Toast.LENGTH_SHORT).show();
                    InfoApp.USER.set(modUser);

                    // ENVIAR CORREO DE VERIFICACION SI SE HA CAMBIADO EL CORREO
                    if (!modUser.getVerificar().equals("true") && !modUser.getEmail().equals(modUser.getVerificar())) {
                        Database<Usuario> verificacion = new Database<>(Database.CAMBIO_EMAIL, modUser);
                        verificacion.start();

                        // CERRAR SESION
                        File fileLogin = this.getFileStreamPath(InfoApp.LOGFILE);
                        if (fileLogin.delete()) {
                            Toast.makeText(this, "Se ha cerrado la sesion", Toast.LENGTH_SHORT).show();
                            Intent activity = new Intent(this, InitActivity.class);
                            finishAffinity();
                            startActivity(activity);
                        }
                    }
                    finish();
                } else {
                    Toast.makeText(this, "No se han podido modificar los datos", Toast.LENGTH_SHORT).show();
                }
            });
            update.start();
        }
    }

    //METODOS
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
        List<T> lstView = new ArrayList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LLENAMOS COLECCIONES
        lstView.add((T)tVActModPerfilApellidos);
        lstView.add((T)tVActModPerfilEmail);
        lstView.add((T)tVActModPerfilNIFNIE);
        lstView.add((T)tVActModPerfilNombre);
        lstView.add((T)tVActModPerfilPregEmail);
        lstView.add((T)tVActModPerfilPregNombre);
        lstView.add((T)tVActModPerfilTitulo);
        lstView.add((T)txtApellido);
        lstView.add((T)txtDNI);
        lstView.add((T)txtEmail);
        lstView.add((T)txtNombre);
        lstView.add((T)btnCancelar);
        lstView.add((T)btnGuardar);

        Method.scaleViews(metrics, lstView);

    }
}
