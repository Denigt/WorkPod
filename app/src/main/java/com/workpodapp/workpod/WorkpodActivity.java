package com.workpodapp.workpod;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.workpodapp.workpod.basic.Database;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.data.Sesion;
import com.workpodapp.workpod.data.Workpod;
import com.workpodapp.workpod.fragments.Fragment_Canjear_Codigos;
import com.workpodapp.workpod.fragments.Fragment_Maps;
import com.workpodapp.workpod.fragments.Fragment_Menu_Usuario;
import com.workpodapp.workpod.fragments.Fragment_Transaction_History;
import com.workpodapp.workpod.fragments.Fragment_sesion;
import com.workpodapp.workpod.fragments.InfoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.workpodapp.workpod.fragments.Fragment_Support;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

    //BOOLEANO NECESARIO PARA VOLVER AL FRAGMENT_SESION Y NO AL MAPA CUANDO ESTÁS EN UNA SESIÓN
    public static Boolean boolSession = false;
    public static Boolean boolValoracion = false;

    //INSTANCIA DEL FRAGMENT INICIAL
    Fragment_Maps fragment_maps;
    //INSTANCIA DEL FRAGMENT DEL HISTÓRICO DE TRANSACCIONES
    Fragment_Transaction_History fragment_transaction_history;
    Fragment_sesion fragment_sesion;

    //CONSTRUCTOR POR DEFECTO
    public WorkpodActivity() {
        fragment_maps = new Fragment_Maps();
        fragment_transaction_history = new Fragment_Transaction_History();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

        //REFRESCAMOS LA SESIÓN
        conectarseBDSesion(this);
        //ACCEDER A LA APP
        accederApp();
    }

    /**
     * Si el usuaurio no se ha loggeado, accede al mapa
     * Si el usuario no está en una sesión, accede al mapa
     * Si el usuario tiene una sesión, accede a la sesión sin pasar por el mapa
     */
    private void accederApp() {
        try {

            if (InfoApp.SESION != null) {
                fragment_sesion = new Fragment_sesion(InfoApp.SESION);
            }
            if (InfoFragment.noInternetConnection) {
                if (InfoFragment.actual == InfoFragment.TRANSACCIONES) {
                    InfoFragment.noInternetConnection = false;
                    volverAlFragmentTransactionHistory();

                } else if (InfoFragment.actual == InfoFragment.DESCUENTOS) {
                    InfoFragment.noInternetConnection = false;
                    InfoFragment.anterior = InfoFragment.actual;
                    InfoFragment.actual = InfoFragment.DESCUENTOS;

                    Fragment_Canjear_Codigos fragment_canjear_codigos = new Fragment_Canjear_Codigos();
                    FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
                    fTransaction = fragmentManager.beginTransaction();
                    fTransaction.replace(R.id.LLFragment, fragment_canjear_codigos).commit();
                }
            } else {
                Log.i("INFO", InfoApp.USER.getReserva().getEstado());
                if (InfoApp.USER.getReserva() != null) {
                    if (InfoApp.USER.getReserva().getEstado().equalsIgnoreCase("En Uso") && (!ValoracionWorkpod.boolReservaFinalizada)) {
                        Fragment_sesion fragmentSesion = new Fragment_sesion(InfoApp.SESION);
                        this.getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragmentSesion).commit();
                    } else if (InfoApp.USER.getReserva().getEstado().equalsIgnoreCase("En Uso") && (ValoracionWorkpod.boolReservaFinalizada)) {
                        InfoApp.USER.getReserva().setEstado("FINALIZADA");
                        InfoApp.RESERVA.setEstado("FINALIZADA");
                        //BOOLEANO QUE CONTROLA QUE CUANDO EL USUARIO TIENE UNA RESERVA Y ACCEDE AL FRAGMENT MAPS, SE MUESTRE EL DIALOG_WORKPOD
                        Fragment_Maps.miReserva = true;
                        //ESTABLECEMOS ESTE FRAGMENT POR DEFECTO CUADO ACCEDEMOS AL WORKPOD SI EL USUARIO NO TIENE RESERVA
                        FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
                        fTransaction = fragmentManager.beginTransaction();
                        fTransaction.add(R.id.LLFragment, fragment_maps).commit();
                        boolSession = false;
                        InfoFragment.actual = InfoFragment.MAPA;
                        ValoracionWorkpod.boolReservaFinalizada = false;
                    } else {
                        //BOOLEANO QUE CONTROLA QUE CUANDO EL USUARIO TIENE UNA RESERVA Y ACCEDE AL FRAGMENT MAPS, SE MUESTRE EL DIALOG_WORKPOD
                        Fragment_Maps.miReserva = true;
                        //ESTABLECEMOS ESTE FRAGMENT POR DEFECTO CUADO ACCEDEMOS AL WORKPOD SI EL USUARIO NO TIENE RESERVA
                        FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
                        fTransaction = fragmentManager.beginTransaction();
                        fTransaction.add(R.id.LLFragment, fragment_maps).commit();
                        boolSession = false;
                        ValoracionWorkpod.boolReservaFinalizada = false;
                    }
                } else {
                    //ESTABLECEMOS ESTE FRAGMENT POR DEFECTO CUADO ACCEDEMOS AL WORKPOD SI EL USUARIO NO TIENE RESERVA
                    FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
                    fTransaction = fragmentManager.beginTransaction();
                    fTransaction.add(R.id.LLFragment, fragment_maps).commit();
                    boolSession = false;
                    ValoracionWorkpod.boolReservaFinalizada = false;
                }
            }

        } catch (NullPointerException e) {
            //SI USER APUNTA A NULL ES QUE EL USUARIO NO SE HA LOGGEADO
            if (InfoApp.USER == null) {
                //OCULTAMOS LAS PARTES DEL NV A LA QUE NO PUEDE ACCEDER EL USUARIO NO REGISTRADO
                btnNV.getMenu().clear();
                btnNV.inflateMenu(R.menu.bottom_nav_menu_usuario_no_registrado);
            } else {
                //ESTABLECEMOS ESTE FRAGMENT POR DEFECTO CUADO ACCEDEMOS AL WORKPOD SI EL USUARIO NO SE HA LOGGEADO
                FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
                fTransaction = fragmentManager.beginTransaction();
                fTransaction.add(R.id.LLFragment, fragment_maps).commit();
                boolSession = false;
                InfoFragment.actual = InfoFragment.MAPA;
            }
            //  ValoracionWorkpod.boolReservaFinalizada=false;
        }
    }

    /**
     * Este método servirá para que si no estás conectado a internet, no se realice la conexión
     * con la BD, Si no estás conectado a internet, te salta el Toast, si lo estás,se realiza la conexión
     *
     * @param context contexto de la app
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void conectarseBDSesion(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //SI EL NETWORKINFO ES NULL O SI ISCONNECTED DEVUELVE FALSE ES QUE NO HAY INTERNET
        if (networkInfo == null || (networkInfo.isConnected() == false)) {
            Toast.makeText(this, "No estás conectado a internet", Toast.LENGTH_LONG).show();
        } else {
            dBSession();
        }
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
                InfoApp.SESION = sesion;
            });
            consultaSesion.start();
            //ESPERAMOS A QUE LA CONSULTA TERMINE PARA QUE NO SE ABRA EL FRAGMENT DE SESIÓN SIN QUE SE HAYA HECHO LA CONSULTA
            consultaSesion.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
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
            //EVITAMOS QUE SI EL USUARIO ESTÁ EN UNA SESIÓN Y LA DA AL BTN DEL MAPA, LE LLEVE AL MAPA
            if (boolSession) {
                volverAlFragmentSession();
            } else {
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
                // FRAGMENT DE SESION FINALIZADA
            /*Fragment_sesion_finalizada sesion_finalizada = new Fragment_sesion_finalizada();
            //INCROPORO EN EL LINEAR LAYOUT EL FRAGMENT INICIAL
            fTransaction.replace(R.id.LLFragment, sesion_finalizada);
            fTransaction.commit();
            boolLoc = true;*/
            }

        } else if (menuitem.getItemId() == R.id.inv_menu_user) {
            FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
            //GESTIONO EL INICIO DE UNA TRANSACCIÓN PARA CARGAR EL FRAGMENTO, CADA TRANSACCIÓN ES UN CAMBIO
            fTransaction = fragmentManager.beginTransaction();
            //CREAMOS UN OBJETO DEL FRAGMENTO
            Fragment_Menu_Usuario menuUsuario = new Fragment_Menu_Usuario();
            //INCROPORO EN EL LINEAR LAYOUT EL FRAGMENT INICIAL
            fTransaction.replace(R.id.LLFragment, menuUsuario);
            fTransaction.commit();
        } else if (menuitem.getItemId() == R.id.inv_support) {
            FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
            //GESTIONO EL INICIO DE UNA TRANSACCIÓN PARA CARGAR EL FRAGMENTO, CADA TRANSACCIÓN ES UN CAMBIO
            fTransaction = fragmentManager.beginTransaction();
            //CREAMOS UN OBJETO DEL FRAGMENTO
            Fragment_Support support = new Fragment_Support();
            //INCROPORO EN EL LINEAR LAYOUT EL FRAGMENT INICIAL
            fTransaction.replace(R.id.LLFragment, support);
            fTransaction.commit();
        }
    }

    /**
     * Método que permite volver al fragment inicial (el de localización)
     * cuando el usuario le da al botón de volver atrás del móvil
     */
    private void volverAlFragmentMaps() {
        //ESTABLECEMOS ESTE FRAGMENT POR DEFECTO CUADO ACCEDEMOS AL WORKPOD
        FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
        //GESTIONO EL INICIO DE UNA TRANSACCIÓN PARA CARGAR EL FRAGMENTO, CADA TRANSACCIÓN ES UN CAMBIO
        fTransaction = fragmentManager.beginTransaction();
        //BOOLEANO QUE CONTROLA QUE CUANDO EL USUARIO TIENE UNA RESERVA Y ACCEDE AL FRAGMENT MAPS, SE MUESTRE EL DIALOG_WORKPOD
        Fragment_Maps.miReserva = true;
        //INCROPORO EN EL LINEAR LAYOUT EL FRAGMENT INICIAL
        fTransaction.replace(R.id.LLFragment, fragment_maps).commit();
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
        InfoFragment.actual = InfoFragment.TRANSACCIONES;
    }

    public void volverAlFragmentSession() {
        try {
            fragment_sesion = new Fragment_sesion(InfoApp.SESION);
            //ESTABLECEMOS ESTE FRAGMENT POR DEFECTO CUADO ACCEDEMOS AL WORKPOD
            FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
            //GESTIONO EL INICIO DE UNA TRANSACCIÓN PARA CARGAR EL FRAGMENTO, CADA TRANSACCIÓN ES UN CAMBIO
            fTransaction = fragmentManager.beginTransaction();
            //INCROPORO EN EL LINEAR LAYOUT EL FRAGMENT INICIAL
            fTransaction.replace(R.id.LLFragment, fragment_sesion, fragment_sesion.getClass().getName()).commit();
            boolSession = false;
            btnNV.setSelectedItemId(0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void valoracionWorkpod() {
        boolValoracion = false;
        Intent activity = new Intent(this, ValoracionWorkpod.class);
        activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(activity);
    }

    private void volverAlMenu() {
        FragmentManager fragmentManager = WorkpodActivity.this.getSupportFragmentManager();
        //GESTIONO EL INICIO DE UNA TRANSACCIÓN PARA CARGAR EL FRAGMENTO, CADA TRANSACCIÓN ES UN CAMBIO
        fTransaction = fragmentManager.beginTransaction();
        //CREAMOS UN OBJETO DEL FRAGMENTO
        Fragment_Menu_Usuario menuUsuario = new Fragment_Menu_Usuario();
        //INCROPORO EN EL LINEAR LAYOUT EL FRAGMENT INICIAL
        fTransaction.replace(R.id.LLFragment, menuUsuario);
        fTransaction.commit();
    }

    //LISTENERS
    @Override
    public void onBackPressed() {

        if (InfoFragment.actual == InfoFragment.MENU || InfoFragment.actual == InfoFragment.VALORACION_WORKPOD && !boolSession) {
            volverAlFragmentMaps();
        } else if (InfoFragment.actual == InfoFragment.FIN_SESION) {
            valoracionWorkpod();
        } else if (InfoFragment.actual == InfoFragment.TRANSACTION_SESSION) {
            volverAlFragmentTransactionHistory();
        } else if (InfoFragment.actual == InfoFragment.MAPA || InfoFragment.actual == InfoFragment.SESSION) {
            //SUSTITUYO ONBACKPRESSED POR FINISH PARA QUE PASE LO QUE PASE, AL LLEGAR AQUÍ, SALGAMOS DE LA APP
            finish();
        } else {
            volverAlMenu();
        }
    }

    @Override
    public void finish() {
        InfoApp.USER = null;
        super.finish();
    }
}