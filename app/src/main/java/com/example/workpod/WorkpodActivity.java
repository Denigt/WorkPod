package com.example.workpod;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.workpod.basic.Database;
import com.example.workpod.basic.InfoApp;
import com.example.workpod.data.Sesion;
import com.example.workpod.data.Workpod;
import com.example.workpod.fragments.Fragment_Maps;
import com.example.workpod.fragments.Fragment_Menu_Usuario;
import com.example.workpod.fragments.Fragment_Transaction_History;
import com.example.workpod.fragments.Fragment_sesion;
import com.example.workpod.fragments.InfoFragment;
import com.example.workpod.fragments.fragment_support;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class WorkpodActivity extends FragmentActivity {

    // CONTROLES DEL XML
    public static BottomNavigationView btnNV;
    private LinearLayout LLFragment;
    private FragmentTransaction fTransaction;

    //BD
    Workpod workpod;
    Sesion sesion;

    //BOOLEANO PARA CONTROLAR LA NAVEGACIÓN POR LOS FRAGMENTS
    public static Boolean boolLoc = false;
    public static Boolean boolfolder = false;
    public static Boolean boolSession = false;
    private Boolean boolOther = false;

    //INSTANCIA DEL FRAGMENT INICIAL
    Fragment_Maps fragment_maps = new Fragment_Maps();
    //INSTANCIA DEL FRAGMENT DEL HISTÓRICO DE TRANSACCIONES
    Fragment_Transaction_History fragment_transaction_history = new Fragment_Transaction_History();
    Fragment_sesion fragment_sesion;

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
        dBSession();

        if(InfoApp.sesion!=null){
            fragment_sesion=new Fragment_sesion(InfoApp.sesion);
        }
        if (InfoApp.USER.getReserva() != null) {
            if (InfoApp.USER.getReserva().getEstado().equalsIgnoreCase("En Uso")) {
                Fragment_sesion fragmentSesion = new Fragment_sesion(InfoApp.sesion);
                this.getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragmentSesion).commit();
            } else {
                //ESTABLECEMOS ESTE FRAGMENT POR DEFECTO CUADO ACCEDEMOS AL WORKPOD SI EL USUARIO NO TIENE RESERVA
                FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
                fTransaction = fragmentManager.beginTransaction();
                fTransaction.add(R.id.LLFragment, fragment_maps).commit();
                boolLoc = true;
            }
        } else {
            //ESTABLECEMOS ESTE FRAGMENT POR DEFECTO CUADO ACCEDEMOS AL WORKPOD SI EL USUARIO NO TIENE RESERVA
            FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
            fTransaction = fragmentManager.beginTransaction();
            fTransaction.add(R.id.LLFragment, fragment_maps).commit();
            boolLoc = true;
        }


        //INICIALIZAMOS EL WORKPOD
        // workpod= InfoApp.USER.ge
    }

    /**
     * Realiza la consulta a la BD para que si el usuario tiene una sesión no le salga el mapa y le salga su sesión
     */
    private void dBSession() {
        try {
            Database<Sesion> consultaSesion = new Database<>(Database.SELECTUSER, new Sesion());
            consultaSesion.postRun(() -> {
                for (Sesion session : consultaSesion.getLstSelect()) {
                    sesion = session;
                }
                InfoApp.sesion = sesion;
            });
            consultaSesion.start();
            //ESPERAMOS A QUE LA CONSULTA TERMINE PARA QUE NO SE ABRA EL FRAGMENT DE SESIÓN SIN QUE SE HAYA HECHO LA CONSULTA
            consultaSesion.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            boolOther = true;
        } else if (menuitem.getItemId() == R.id.inv_folder) {
            FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
            //GESTIONO EL INICIO DE UNA TRANSACCIÓN PARA CARGAR EL FRAGMENTO, CADA TRANSACCIÓN ES UN CAMBIO
            fTransaction = fragmentManager.beginTransaction();
            Fragment_Transaction_History transaction_history = new Fragment_Transaction_History();
            //INCROPORO EN EL LINEAR LAYOUT EL FRAGMENT INICIAL
            fTransaction.replace(R.id.LLFragment, transaction_history);
            fTransaction.commit();
            boolLoc = false;
            boolOther = true;
            //
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
            boolOther = true;
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
        fTransaction.replace(R.id.LLFragment, fragment_maps).commit();
        boolLoc = true;
        //CAMBIAMOS LA SELECCIÓN DEL NV AL ICONO DE LOCATION
        btnNV.setSelectedItemId(R.id.inv_location);
    }

    private void volverAlFragmentTransactionHistory() {
        //ESTABLECEMOS ESTE FRAGMENT POR DEFECTO CUADO ACCEDEMOS AL WORKPOD
        FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
        //GESTIONO EL INICIO DE UNA TRANSACCIÓN PARA CARGAR EL FRAGMENTO, CADA TRANSACCIÓN ES UN CAMBIO
        fTransaction = fragmentManager.beginTransaction();
        //INCROPORO EN EL LINEAR LAYOUT EL FRAGMENT INICIAL
        fTransaction.replace(R.id.LLFragment, fragment_transaction_history).commit();
        boolLoc = false;
        boolfolder = false;
        //CAMBIAMOS LA SELECCIÓN DEL NV AL ICONO DE LOCATION
        btnNV.setSelectedItemId(R.id.inv_folder);
    }

    private void volverAlFragmentSession() {
        fragment_sesion=new Fragment_sesion(InfoApp.sesion);
        //ESTABLECEMOS ESTE FRAGMENT POR DEFECTO CUADO ACCEDEMOS AL WORKPOD
        FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
        //GESTIONO EL INICIO DE UNA TRANSACCIÓN PARA CARGAR EL FRAGMENTO, CADA TRANSACCIÓN ES UN CAMBIO
        fTransaction = fragmentManager.beginTransaction();
        //INCROPORO EN EL LINEAR LAYOUT EL FRAGMENT INICIAL
        fTransaction.replace(R.id.LLFragment, fragment_sesion, fragment_sesion.getClass().getName()).commit();
        boolSession = false;
        btnNV.setSelectedItemId(0);
        //PERMITIRÁ QUE AL DARLE ATRÁS TE SALGAS DE LA SESIÓN
        boolOther = false;

    }

    //LISTENERS
    @Override
    public void onBackPressed() {
        if ((boolfolder) && (boolLoc)) {
            volverAlFragmentTransactionHistory();
        } else if (!boolLoc && !boolSession) {
            volverAlFragmentInicial();
        } else if (boolSession && boolOther) {
            volverAlFragmentSession();
        } else if (boolLoc) {
            boolSession = false;
            super.onBackPressed();
        }
    }

}