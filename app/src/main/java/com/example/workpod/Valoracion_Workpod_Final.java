package com.example.workpod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.workpod.fragments.Fragment_Dialog_Call;
import com.example.workpod.testUsuario.Informacion_Usuario;
import com.example.workpod.testUsuario.Informacion_Usuario2;

public class Valoracion_Workpod_Final extends AppCompatActivity implements View.OnClickListener {
    //XML
    private ImageView iVStar1, iVStar2, iVStar3, iVStar4, iVStar5;
    private Button btnLikedin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valoracion__workpod__final);

        //INSTANCIAS DE LOS ELEMENTOS DEL XML
        iVStar1 = (ImageView) findViewById(R.id.IVStar1);
        iVStar2 = (ImageView) findViewById(R.id.IVStar2);
        iVStar3 = (ImageView) findViewById(R.id.IVStar3);
        iVStar4 = (ImageView) findViewById(R.id.IVStar4);
        iVStar5 = (ImageView) findViewById(R.id.IVStar5);
        btnLikedin = (Button) findViewById(R.id.BtnLinkedin);

        //INSTANCIAMOS LOS LISTENERS
        iVStar1.setOnClickListener(this);
        iVStar2.setOnClickListener(this);
        iVStar3.setOnClickListener(this);
        iVStar4.setOnClickListener(this);
        iVStar5.setOnClickListener(this);
        btnLikedin.setOnClickListener(this);

        resultadovaloracionUsuario();
        resultadoTestUsuario();
    }

    //MÉTODOS SOBREESCRITOS
    @Override
    public void onBackPressed() {
        //MÉTODO QUE CIERRA COMPLETAMENTE LA APP
        finishAffinity();
    }

    @Override
    public void onClick(View v) {
        callToActionStars(v);
    }
    //MÉTODOS

    /**
     * Método que saca por pantalla el resultado del test hecho por el usuario, posteriormente se pasarán dichos datos a un campo
     * de la BD del servidor.
     * ResultadoTest es una matriz estática de la clase Informacion_Usuario
     */
    private void resultadoTestUsuario() {
        //HACEMOS ECO DEL RESULTADO PARA VERIFICAR QUE FUNCIONA, POSTERIORMENTE EL RESULTADO MIGRARÁ AL SERVIDOR
        for (int i = 0; i < Informacion_Usuario.resultadoTest.length; i++)
            Toast.makeText(getApplicationContext(), Informacion_Usuario.resultadoTest[i] + "\n", Toast.LENGTH_SHORT).show();
    }

    /**
     * Método para que nada más abrir este activity, te salgan las estrellas que hayas seleccionado en el activity ValoracionWorkpod
     * Este método permite no tener que repetir la valoración o cambiar la calificación de la valoración.
     * En el futuro, aquellos usuarios que ya hayan realizado el test alguna vez, solo aparecerá el activity Valoracion_Workpod_Final y
     * allí será donde hagan la valoración de la sesión de workpod.
     */
    private void resultadovaloracionUsuario() {
        if (ValoracionWorkpod.resultado_valoracion == 1) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar3.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar4.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar5.setColorFilter(Color.parseColor("#D8D9DA"));
        } else if (ValoracionWorkpod.resultado_valoracion == 2) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar3.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar4.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar5.setColorFilter(Color.parseColor("#D8D9DA"));
        } else if (ValoracionWorkpod.resultado_valoracion == 3) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar3.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar4.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar5.setColorFilter(Color.parseColor("#D8D9DA"));
        } else if (ValoracionWorkpod.resultado_valoracion == 4) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar3.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar4.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar5.setColorFilter(Color.parseColor("#D8D9DA"));
        } else if (ValoracionWorkpod.resultado_valoracion == 5) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar3.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar4.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar5.setColorFilter(Color.parseColor("#FFEB3B"));
        }
    }

    /**
     * Método para cambiar de color las estrellas
     * cuando el usuario la seleccione
     *
     * @param v instancia de la clase View con las que conectamos
     *          con cada estrella
     */
    private void callToActionStars(View v) {
        if (v.getId() == R.id.IVStar1) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar3.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar4.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar5.setColorFilter(Color.parseColor("#D8D9DA"));
        } else if (v.getId() == R.id.IVStar2) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar3.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar4.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar5.setColorFilter(Color.parseColor("#D8D9DA"));
        } else if (v.getId() == R.id.IVStar3) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar3.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar4.setColorFilter(Color.parseColor("#D8D9DA"));
            iVStar5.setColorFilter(Color.parseColor("#D8D9DA"));
        } else if (v.getId() == R.id.IVStar4) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar3.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar4.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar5.setColorFilter(Color.parseColor("#D8D9DA"));
        } else if (v.getId() == R.id.IVStar5) {
            iVStar1.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar2.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar3.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar4.setColorFilter(Color.parseColor("#FFEB3B"));
            iVStar5.setColorFilter(Color.parseColor("#FFEB3B"));
        }
    }
}