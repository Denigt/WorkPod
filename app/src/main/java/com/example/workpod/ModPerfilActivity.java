package com.example.workpod;

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

import com.example.workpod.basic.Database;
import com.example.workpod.basic.InfoApp;
import com.example.workpod.basic.Method;
import com.example.workpod.data.Usuario;
import com.example.workpod.scale.Scale_Buttons;
import com.example.workpod.scale.Scale_TextView;

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
                    if (!modUser.getVerificar().equals(true) && !modUser.getEmail().equals(modUser.getVerificar())) {
                        modUser.setEmail(modUser.getVerificar());
                        Database<Usuario> verificacion = new Database<>(Database.VERIFICACION, modUser);
                        verificacion.start();
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
        this.lstTv = new ArrayList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LLENAMOS COLECCIONES
        lstTv.add(new Scale_TextView(tVActModPerfilApellidos, "Match_Parent", "bold", 18,18, 18));
        lstTv.add(new Scale_TextView(tVActModPerfilEmail, "Match_Parent", "bold", 18, 18,18));
        lstTv.add(new Scale_TextView(tVActModPerfilNIFNIE, "Match_Parent", "bold", 24,24, 24));
        lstTv.add(new Scale_TextView(tVActModPerfilNombre, "Match_Parent", "bold", 18, 18,18));
        lstTv.add(new Scale_TextView(tVActModPerfilPregEmail, "Match_Parent", "bold", 24,24, 24));
        lstTv.add(new Scale_TextView(tVActModPerfilPregNombre, "Match_Parent", "bold", 24,24, 24));
        lstTv.add(new Scale_TextView(tVActModPerfilTitulo, "Match_Parent", "bold", 30, 30,34));

        Method.scaleTv(metrics, lstTv);

    }
}
