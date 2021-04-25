package com.example.workpod;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.workpod.fragments.Fragment_Maps;
import com.example.workpod.fragments.Fragment_Menu_Usuario;
import com.example.workpod.fragments.Fragment_Transaction_History;
import com.example.workpod.fragments.Fragment_sesion_finalizada;
import com.example.workpod.fragments.InfoFragment;
import com.example.workpod.fragments.fragment_support;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class WorkpodActivity extends FragmentActivity {

    // CONTROLES DEL XML
    public static BottomNavigationView btnNV;
    private LinearLayout LLFragment;
    private FragmentTransaction fTransaction;

    //BOOLEANO PARA CONTROLAR LA NAVEGACIÓN POR LOS FRAGMENTS
    public static Boolean boolLoc = false;

    //INSTANCIA DEL FRAGMENT INICIAL
    Fragment_sesion_finalizada sesion_finalizada = new Fragment_sesion_finalizada();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workpod);

        //INSTANCIAR LOS ELEMENTOS DEL XML
        LLFragment = (LinearLayout) findViewById(R.id.LLFragment);
        btnNV = (BottomNavigationView) findViewById(R.id.btnNV);
        btnNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {
                btnNVonNavigationItemSelected(menuitem);
                return true;
            }
        });

        //ESTABLECEMOS ESTE FRAGMENT POR DEFECTO CUADO ACCEDEMOS AL WORKPOD
        FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
        fTransaction = fragmentManager.beginTransaction();
        fTransaction.add(R.id.LLFragment, sesion_finalizada).commit();
        boolLoc = true;
    }

    /**
     * Método para que al pulsar en un icono del navigation view,
     * aparezca el fragment asociado a dicho icono
     *
     * @param menuitem parámetro relacionado con el icono del navigation view
     */
    private void btnNVonNavigationItemSelected(MenuItem menuitem) {

        if (menuitem.getItemId() == R.id.inv_location) {
            //HAY QUE BORRAR TODOS LOS ELEMENTOS DEL LAYOUT SI QUEREMOS QUE APAREZCA EL FRAGMENT SELECCIONADO
            FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
            //GESTIONO EL INICIO DE UNA TRANSACCIÓN PARA CARGAR EL FRAGMENTO, CADA TRANSACCIÓN ES UN CAMBIO
            fTransaction = fragmentManager.beginTransaction();

            // ALMACENAR CUAL ES EL FRAGMENT QUE SE MUESTRA AL USUARIO Y CUAL FUE EL ULTIMO MOSTRADO
            InfoFragment.anterior = InfoFragment.actual;
            InfoFragment.actual = InfoFragment.MAPA;
            // FRAGMENT DEL MAPA
            Fragment_Maps maps = new Fragment_Maps();
            //INCROPORO EN EL LINEAR LAYOUT EL FRAGMENT INICIAL
            fTransaction.replace(R.id.LLFragment, maps);
            fTransaction.commit();
            boolLoc = true;
            // FRAGMENT DE SESION FINALIZADA
            /*Fragment_sesion_finalizada sesion_finalizada = new Fragment_sesion_finalizada();
            //INCROPORO EN EL LINEAR LAYOUT EL FRAGMENT INICIAL
            fTransaction.replace(R.id.LLFragment, sesion_finalizada);
            fTransaction.commit();
            boolLoc = true;*/
        } else if (menuitem.getItemId() == R.id.inv_support) {
            FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
            //GESTIONO EL INICIO DE UNA TRANSACCIÓN PARA CARGAR EL FRAGMENTO, CADA TRANSACCIÓN ES UN CAMBIO
            fTransaction = fragmentManager.beginTransaction();
            fragment_support soporte = new fragment_support();
            //INCROPORO EN EL LINEAR LAYOUT EL FRAGMENT INICIAL
            fTransaction.replace(R.id.LLFragment, soporte);
            fTransaction.commit();
            boolLoc = false;
        } else if (menuitem.getItemId() == R.id.inv_folder) {
            FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
            //GESTIONO EL INICIO DE UNA TRANSACCIÓN PARA CARGAR EL FRAGMENTO, CADA TRANSACCIÓN ES UN CAMBIO
            fTransaction = fragmentManager.beginTransaction();
            Fragment_Transaction_History transaction_history = new Fragment_Transaction_History();
            //INCROPORO EN EL LINEAR LAYOUT EL FRAGMENT INICIAL
            fTransaction.replace(R.id.LLFragment, transaction_history);
            fTransaction.commit();
            boolLoc = false;
        } else if (menuitem.getItemId() == R.id.inv_menu_user) {
            FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
            //GESTIONO EL INICIO DE UNA TRANSACCIÓN PARA CARGAR EL FRAGMENTO, CADA TRANSACCIÓN ES UN CAMBIO
            fTransaction = fragmentManager.beginTransaction();
            //CREAMOS UN OBJETO DEL FRAGMENTO
            Fragment_Menu_Usuario menuUsuario = new Fragment_Menu_Usuario();
            //INCROPORO EN EL LINEAR LAYOUT EL FRAGMENT INICIAL
            fTransaction.replace(R.id.LLFragment, menuUsuario);
            fTransaction.commit();
            boolLoc = false;
        }
    }

    /**
     * Método que permite volver al fragment inicial (el de localización)
     * cuando el usuario le da al botón de volver atrás del móvil
     */
    private void volverAlFragmentInicial() {
        //ESTABLECEMOS ESTE FRAGMENT POR DEFECTO CUADO ACCEDEMOS AL WORKPOD
        FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
        //GESTIONO EL INICIO DE UNA TRANSACCIÓN PARA CARGAR EL FRAGMENTO, CADA TRANSACCIÓN ES UN CAMBIO
        fTransaction = fragmentManager.beginTransaction();
        //INCROPORO EN EL LINEAR LAYOUT EL FRAGMENT INICIAL
        fTransaction.replace(R.id.LLFragment, sesion_finalizada).commit();
        boolLoc = true;
    }

    //LISTENERS
    @Override
    public void onBackPressed() {
        if (boolLoc) {
            super.onBackPressed();
        } else {
            volverAlFragmentInicial();
            //CAMBIAMOS LA SELECCIÓN DEL NV AL ICONO DE LOCATION
            btnNV.setSelectedItemId(R.id.inv_location);
        }
    }
}