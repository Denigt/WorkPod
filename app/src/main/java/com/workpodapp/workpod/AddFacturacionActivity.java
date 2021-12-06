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
import com.workpodapp.workpod.data.Facturacion;
import com.workpodapp.workpod.data.Usuario;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class AddFacturacionActivity extends AppCompatActivity implements View.OnClickListener {

    // CONTROLES DEL LAYOUT
    private EditText txtPais;
    private EditText txtProvincia;
    private EditText txtCiudad;
    private EditText txtDireccion;
    private EditText txtCP;
    private EditText txtPaisPos;
    private EditText txtProvinciaPos;
    private EditText txtCiudadPos;
    private EditText txtDireccionPos;
    private EditText txtCPPos;
    private ImageButton btnGuardar;
    private ImageButton btnCancelar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_facturacion);

        // BUSCAR LOS CONTROLES EN EL LAYOUT
        txtPais = findViewById(R.id.txtPais);
        txtProvincia = findViewById(R.id.txtProvincia);
        txtCiudad = findViewById(R.id.txtCiudad);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtCP = findViewById(R.id.txtCP);

        txtPaisPos = findViewById(R.id.txtPaisPos);
        txtProvinciaPos = findViewById(R.id.txtProvinciaPos);
        txtCiudadPos = findViewById(R.id.txtCiudadPos);
        txtDireccionPos = findViewById(R.id.txtDireccionPos);
        txtCPPos = findViewById(R.id.txtCPPos);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        // LISTENERS
        btnGuardar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        //ESCALAMOS ELEMENTOS
        escalarElementos();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnCancelar.getId()) {
            finish();
        } else if (InfoApp.USER != null && v.getId() == btnGuardar.getId()) {
            Facturacion dirFacturacion = new Facturacion();

            if (!txtPais.getText().toString().equals(""))
                dirFacturacion.getFacturacion().setPais(txtPais.getText().toString());

            if (!txtProvincia.getText().toString().equals(""))
                dirFacturacion.getFacturacion().setProvincia(txtProvincia.getText().toString());

            if (!txtCiudad.getText().toString().equals(""))
                dirFacturacion.getFacturacion().setCiudad(txtCiudad.getText().toString());

            if (!txtDireccion.getText().toString().equals(""))
                dirFacturacion.getFacturacion().setCiudad(txtDireccion.getText().toString());

            if (!txtCP.getText().toString().equals(""))
                dirFacturacion.getFacturacion().setCodPostal(Integer.parseInt(txtCP.getText().toString()));

            if (!txtPaisPos.getText().toString().equals(""))
                dirFacturacion.getPostal().setPais(txtPaisPos.getText().toString());

            if (!txtProvinciaPos.getText().toString().equals(""))
                dirFacturacion.getPostal().setProvincia(txtProvinciaPos.getText().toString());

            if (!txtCiudadPos.getText().toString().equals(""))
                dirFacturacion.getPostal().setCiudad(txtCiudadPos.getText().toString());

            if (!txtDireccionPos.getText().toString().equals(""))
                dirFacturacion.getPostal().setCiudad(txtDireccionPos.getText().toString());

            if (!txtCPPos.getText().toString().equals(""))
                dirFacturacion.getPostal().setCodPostal(Integer.parseInt(txtCPPos.getText().toString()));


            Database<Facturacion> insert = new Database<>(Database.INSERT, dirFacturacion);
            insert.postRunOnUI(this, () -> {
                if (insert.getError().code > -1) {
                    Toast.makeText(this, "Direccion añadida", Toast.LENGTH_SHORT).show();
                    if (InfoApp.USER.getDirFacturacion() == null)
                        InfoApp.USER.setDirFacturacion(new ArrayList<>());
                    InfoApp.USER.getDirFacturacion().add(dirFacturacion);

                        // CERRAR SESION
                        File fileLogin = this.getFileStreamPath(InfoApp.LOGFILE);
                        finish();
                    finish();
                } else {
                    Toast.makeText(this, "No se ha podido añadir la dirección", Toast.LENGTH_SHORT).show();
                }
            });
            insert.start();
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
    private void escalarElementos() {

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LLENAMOS COLECCIONES

      //  Method.scaleTv(metrics, lstTv);

    }
}
