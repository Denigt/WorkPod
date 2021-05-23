package com.example.workpod;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workpod.basic.Database;
import com.example.workpod.basic.InfoApp;
import com.example.workpod.basic.Method;
import com.example.workpod.data.Usuario;

public class ModPerfilActivity extends AppCompatActivity implements View.OnClickListener {

    private final String[] TIPOS_DOCUMENTO = {"NIF", "NIE"};

    // CONTROLES DEL LAYOUT
    Spinner spnDNI;
    EditText txtNombre;
    EditText txtApellido;
    EditText txtDNI;
    EditText txtEmail;
    ImageButton btnGuardar;

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
        btnGuardar = findViewById(R.id.btnGuardar);

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
    }

    @Override
    public void onClick(View v) {
        if (InfoApp.USER != null) {
            Usuario modUser = new Usuario();

            modUser.set(InfoApp.USER);
            modUser.setNombre(txtNombre.getText().toString());
            modUser.setApellidos(txtApellido.getText().toString());
            if (Method.checkEmail(txtEmail.getText().toString()))
                modUser.setEmail(txtEmail.getText().toString());
            //if (Method.checkDNI(txtDNI.getText().toString()))
            modUser.setDni(txtDNI.getText().toString());

            Database<Usuario> update = new Database<>(Database.UPDATE, InfoApp.USER, modUser);
            update.postRunOnUI(this, () -> {
                if(update.getError().code > -1){
                    Toast.makeText(this, "Datos modificados", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "No se han podido modificar los datos", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
