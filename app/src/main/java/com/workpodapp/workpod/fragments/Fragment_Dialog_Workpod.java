package com.workpodapp.workpod.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.workpodapp.workpod.R;
import com.workpodapp.workpod.basic.Database;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.basic.Shared;
import com.workpodapp.workpod.data.Reserva;
import com.workpodapp.workpod.data.Sesion;
import com.workpodapp.workpod.data.Ubicacion;
import com.workpodapp.workpod.data.Workpod;
import com.google.android.gms.maps.model.LatLng;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Use the {@link Fragment_Dialog_Workpod#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Dialog_Workpod extends DialogFragment implements View.OnClickListener {

    //CONSTANTES
    private static final int TIEMPO_EMPIECE_CRONO = 1000;

    //INSTANCIAS DE LA CLASE DATA
    Ubicacion ubicacion;
    Workpod workpod;
    Reserva reserva;
    Sesion sesion;

    //XML
    private TextView tVNombreWorkpod;
    private TextView tVDireccion;
    private TextView tVCapacidad;
    private TextView tVPrecio;
    private TextView tVUltUso;
    private TextView tVUltUsoDato;
    private TextView tVUltLimpieza;
    private TextView tVUltLimpiezaDato;
    private TextView tVIlumincion;
    private TextView tVDescripcionWorkpod;
    private TextView tVComoLlegar;

    private ImageView iVComoLlegar;
    private ImageView iVFlechas_Informacion_Desripcion;
    private ImageView iVFlechas_Descripcion_Informacion;
    private ImageView iVUltUso;
    private ImageView iVUltLimpieza;
    private ImageView iVIluminacion;
    private ImageView iV_Icon_Capacidad;

    private Button btnReservarWorkpod;
    private ImageButton btnCancelarReserva;
    private Button btnAbrirAhora;

    private LinearLayout lLInfoWorkpod;
    private LinearLayout lLDescripcion;
    private LinearLayout lLEstadoWorkpod;
    private LinearLayout lLAbrirAhora;

    //DATOS DE LA TABLA WORKPOD
    private int numUsuarios;
    private double precio;
    private String direccion;
    private String descripcion;
    int idWorkpodUsuario;

    //VARIABLES
    private int centesimas;
    private long segundos;
    private long minutos;
    private Thread crono;
    private Handler handler = new Handler();
    private Shared<LatLng> posicion;
    private List<Reserva> lstReservas = new ArrayList<>();
    Location posicionWorkpod;
    Location posicionUsuario;
    private boolean abrirAhora;
    private boolean cambiarDistancia;
    private boolean visibleBtnCancelar = false;
    Fragment_Maps map;
    DisplayMetrics metrics;
    float width;
    private int usuarioSesion;

    // VARIABLE PARA ORDENAR LA DETENCION DE LOS HILOS
    private boolean finish = false;


    //CONSTRUCTOR CON INSTANCIA DE UBICACIÓN
    public Fragment_Dialog_Workpod() {
        ubicacion = new Ubicacion();
    }

    //CONSTRUCTOR CON INSTANCIA DE WORKPODS Y DIRECCION

    /**
     * Crea un fragment con la informacion del workpod que hay en la ubicacion
     *
     * @param workpod   Workpod del que obtener la informacion
     * @param ubicacion Ubicacion en la que se encuentra el workpod
     */
    public Fragment_Dialog_Workpod(Workpod workpod, Ubicacion ubicacion, Shared<LatLng> posicion) {
        this.ubicacion = ubicacion;
        this.workpod = workpod;
        this.posicion = posicion;
        this.abrirAhora = false;
        this.cambiarDistancia = false;
    }

    /**
     * Crea un fragment con la informacion del workpod que hay en la ubicacion
     *
     * @param workpod   Workpod del que obtener la informacion
     * @param ubicacion Ubicacion en la que se encuentra el workpod
     */
    public Fragment_Dialog_Workpod(Workpod workpod, Ubicacion ubicacion, Shared<LatLng> posicion, Fragment_Maps map) {
        this.workpod = workpod;
        this.ubicacion = ubicacion;
        this.posicion = posicion;
        this.abrirAhora = false;
        this.cambiarDistancia = false;
        this.map = map;
    }

    /**
     * Crea un fragment con la informacion del workpod que hay en la ubicacion
     * Solo usar si la ubicacion tiene un solo workpod
     *
     * @param ubicacion Ubicacion en la que se encuentra el workpod
     */
    public Fragment_Dialog_Workpod(Ubicacion ubicacion, Shared<LatLng> posicion, Fragment_Maps map) {
        this.ubicacion = ubicacion;
        this.workpod = ubicacion.getWorkpods().get(0);
        this.posicion = posicion;
        this.abrirAhora = false;
        this.cambiarDistancia = false;
        this.map = map;
    }


    public static Fragment_Dialog_Workpod newInstance(String param1, String param2) {
        Fragment_Dialog_Workpod fragment = new Fragment_Dialog_Workpod();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    //SOBREESCRITURAS
    //ESTA SOBREESCRITURA PERMITE CREAR UN DIALOGRESULT
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return createDialog();
    }

    private Dialog createDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //ESPECIFICAMOS DONDE VAMOS A CREAR (INFLAR) EL DIALOGRESULT
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_workpod, null);
        builder.setView(view);
        //INICIALIZAMOS LOS ELEMENTOS DEL XML
        lLInfoWorkpod = (LinearLayout) view.findViewById(R.id.LLInfoWorkpod);
        lLDescripcion = (LinearLayout) view.findViewById(R.id.LLDescripcion);
        lLEstadoWorkpod = (LinearLayout) view.findViewById(R.id.LLEstadoWorkpod);
        lLAbrirAhora = (LinearLayout) view.findViewById(R.id.LLAbrirAhora);

        tVPrecio = (TextView) view.findViewById(R.id.TVPrecio);
        tVNombreWorkpod = (TextView) view.findViewById(R.id.TVNombreWorkpod);
        tVDireccion = (TextView) view.findViewById(R.id.TVDireccion);
        tVCapacidad = (TextView) view.findViewById(R.id.TVCapacidad);
        tVUltUso = (TextView) view.findViewById(R.id.TVUltUso);
        tVUltUsoDato = (TextView) view.findViewById(R.id.TVUltUsoDato);
        tVUltLimpieza = (TextView) view.findViewById(R.id.TVUltLimpieza);
        tVUltLimpiezaDato = (TextView) view.findViewById(R.id.TVUltLimpiezaDato);
        tVIlumincion = (TextView) view.findViewById(R.id.TVIluminacion);
        tVComoLlegar = (TextView) view.findViewById(R.id.tVComoLlegar);

        iVComoLlegar = (ImageView) view.findViewById(R.id.iVComoLlegar);
        tVDescripcionWorkpod = (TextView) view.findViewById(R.id.TVDescripcionWorkpod);
        iVFlechas_Informacion_Desripcion = (ImageView) view.findViewById(R.id.IVFlechas_Informacion_Descripcion);
        iVFlechas_Descripcion_Informacion = (ImageView) view.findViewById(R.id.IVFlechas_Descripcion_Informacion);
        iVUltUso = (ImageView) view.findViewById(R.id.IVUltUso);
        iVUltLimpieza = (ImageView) view.findViewById(R.id.IVUltLimpieza);
        iVIluminacion = (ImageView) view.findViewById(R.id.IVIluminacion);
        iV_Icon_Capacidad = (ImageView) view.findViewById(R.id.IV_Icon_Capacidad);

        btnAbrirAhora = (Button) view.findViewById(R.id.BtnAbrirAhora);
        btnReservarWorkpod = (Button) view.findViewById(R.id.BtnReservarWorkpod);
        btnCancelarReserva = view.findViewById(R.id.btnCancelarReserva);

        //INICIALIZAMOS OTRAS VARIABLES
        inicializarCrono();

        // Toast.makeText(getActivity(),String.valueOf(posicion.latitude)+","+String.valueOf(posicion.longitude),Toast.LENGTH_LONG).show();

        //INICIALIZAMOS LA INSTANCIA DE RESERVA
        reserva = new Reserva();
        //INICIALIZAMOS LA INSTANCIA DE SESION
        sesion = new Sesion();

        //OBTENEMOS EL ID DEL WORKPOD QUE HA RESERVADO EL USUARIO
        if (InfoApp.USER != null && InfoApp.USER.getReserva() != null && !InfoApp.USER.getReserva().isCancelada())
            idWorkpodUsuario = InfoApp.USER.getReserva().getWorkpod();

        //VOLCAMOS DATOS DE LA BD
        volcarDatos();
        try {
            comprobarReserva();
         /*   workpod.getReserva().getId();
            InfoApp.USER.getReserva().getId();
            workpod.getReserva().getEstado();*/

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //LISTENERS
        btnReservarWorkpod.setOnClickListener(this);
        btnAbrirAhora.setOnClickListener(this);
        btnCancelarReserva.setOnClickListener(this);
        iVFlechas_Informacion_Desripcion.setOnClickListener(this);
        iVFlechas_Descripcion_Informacion.setOnClickListener(this);
        iVComoLlegar.setOnClickListener(this);
        tVComoLlegar.setOnClickListener(this);
        lLEstadoWorkpod.setOnClickListener(this);

        //COMPROBAMOS SI USUARIO ESTÁ REGISTRADO

        /* DESACTIVAR BTN CUANDO USUARIO NO ESTÉ REGISTRADO
         if(InfoApp.USER.getId()==0){
                usuarioNoRegistrado();
            }*/

        //GUARDAMOS EN UNA VARIABLE LA UBICACIÓN DEL WORKPOD
        posicionWorkpod = new Location("Posición Workpod");
        posicionWorkpod.setLatitude(ubicacion.getLat());
        posicionWorkpod.setLongitude(ubicacion.getLon());

        //ESCALAMOS ELEMENTOS
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels / metrics.density;
        escalarElementos(metrics);

        InfoFragment.DIALOGO_DESPLEGADO=true;

        //RETORNAMOS EL OBJETO BUILDER CON EL MÉTODO CREATE
        return builder.create();

    }

    //SOBREESCRITURAS
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_dialog_workpod, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.BtnAbrirAhora) {
            onClickBtnAbrirAhora();
        } else if (v.getId() == R.id.IVFlechas_Informacion_Descripcion) {
            onClickIVFlechas_Informacion_Descripcion();
        } else if (v.getId() == R.id.IVFlechas_Descripcion_Informacion) {
            onClickIVFlechas_Descripcion_Informacion();
        } else if (v.getId() == R.id.BtnReservarWorkpod) {
            onClickReservarWorkpod();
        } else if (v.getId() == R.id.iVComoLlegar) {
            onClickComoLlegar();
        } else if (v.getId() == R.id.tVComoLlegar) {
            onClickComoLlegar();
        } else if (v.getId() == R.id.LLEstadoWorkpod) {
            onClickReservarWorkpod();
        } else if (v.getId() == R.id.btnCancelarReserva) {
            onClickCancelarReserva();
        }
    }

    /**
     * Es el OnBackPressed de un DialogFragment
     *
     * @param dialog
     */
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if (minutos < 0) {
            //ECO DE RESERVA CANCELADA POR EL SISTEMA
            Toast.makeText(getActivity(), "Tiempo agotado, reserva cancelada", Toast.LENGTH_LONG).show();
        }

        if (map != null)
            map.actualizarMapa();
        //PARO EL HILO
        finish = true;
        //HABILITO EL BTN RESERVAR POR SI EL USUARIO HA ACCEDIDO DESDE EL HISTÓRICO
        btnReservarWorkpod.setEnabled(true);
        Fragment_Transaction_Session.desactivarBtnReservar = false;
        InfoFragment.DIALOGO_DESPLEGADO=false;
        super.onDismiss(dialog);
    }


    //MÉTODOS

    /**
     * Método que comprueba si el usuario tiene una reserva en un determinado workpod que aún no ha caducado.
     * Este Método modificará la interfaz del Fragment a reservado
     */
    private void comprobarReserva() throws InterruptedException {
        try {
            //SI LA RESERVA NO ES NULA Y EL ID DE ESTE WORKPOD COINICIDE CON EL DEL WORKPOD RESERVADO POR EL USUARIO
            if ((workpod.getReserva() != null) && (workpod.getReserva().getId() == InfoApp.USER.getReserva().getId())
                    && workpod.getReserva().getEstado().equalsIgnoreCase("RESERVADA") && !InfoApp.USER.getReserva().isCancelada()) {
                //CAMBIAMOS TEXTO Y COLOR DEL LAYOUT DEL BTN AL PULSARLO
                btnReservarWorkpod.setText("Reservado");
                // btnReservarWorkpod.setTextSize(10);
                lLEstadoWorkpod.setBackground(getActivity().getDrawable(R.drawable.rounded_back_button_green));
                //HACEMOS VISIBLE EL BTN DE ABRIR AHORA Y DE CANCELAR RESERVA
                lLAbrirAhora.setBackground(getActivity().getDrawable(R.drawable.rounded_border_button));
                lLAbrirAhora.setVisibility(View.VISIBLE);
                btnAbrirAhora.setVisibility(View.VISIBLE);
                //FIJAMOS EL ANCHO DE LOS LAYOUTS DE AMBOS BTNS
                lLEstadoWorkpod.getLayoutParams().width = 0;
                //GUARDAMOS EN ESTA VARIABLE LA FECHA EN LA QUE SE HIZO LA RESERVA
                ZonedDateTime fechaReservaWorkpod = workpod.getReserva().getFecha();
                //CALCULAMOS EL TIEMPO QUE LE QUEDA AL USUARIO PARA LLEGAR A LA CABINA
                long resto = (20 * 60) - Method.subsDate(ZonedDateTime.now(), fechaReservaWorkpod);
                //INICIALIZAMOS LAS VARIABLES CON EL TIEMPO QUE QUEDA
                this.minutos = resto / 60;
                this.segundos = resto % 60;
                //INICIALIZAMOS Y ARRANCAMOS EL HILO
                arrancarCronometro();
                //ECO DEL TIEMPO QUE LE QUEDA AL USUARIO PARA LLEGAR A LA CABINA
                Toast.makeText(getActivity(), "Tienes " + (resto / 60) + "min y " + (resto % 60) + "seg para llegar", Toast.LENGTH_LONG).show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    /**
     * Este método sirve de ante sala para el método de la clase Methods donde escalamos los elementos del xml.
     * En este método inicializamos las colecciones donde guardamos los elementos del xml que vamos a escalar y
     * donde especificamos el width que queremos (match_parent, wrap_content o ""(si no ponemos nada significa que
     * el elemento tiene unos dp definidos que queremos que se conserven tanto en dispositivos grandes como en pequeños.
     * También especificamos en la List el estilo de letra (bold, italic, normal) y el tamaño de la fuente del texto tanto
     * para dispositivos pequeños como para dispositivos grandes).
     * <p>
     * Como el método scale de la clase Methods no es un activity o un fragment no podemos inicializar nuestro objeto de la clase
     * DisplayMetrics con los parámetros reales de nuestro móvil, es por ello que lo inicializamos en este método.
     * <p>
     * En resumen, en este método inicializamos el metrics y las colecciones y se lo pasamos al método de la clase Methods
     *
     * @param metrics
     */
    private <T extends View> void escalarElementos(DisplayMetrics metrics) {
        //INICIALIZAMOS COLECCIONES
        List<T> lstView = new ArrayList<>();


        //LLENAMOS COLECCIONES
        lstView.add((T) btnReservarWorkpod);
        lstView.add((T) btnAbrirAhora);

        lstView.add((T) tVNombreWorkpod);
        lstView.add((T) tVPrecio);
        lstView.add((T) tVDireccion);
        lstView.add((T) tVDescripcionWorkpod);
        lstView.add((T) tVIlumincion);
        lstView.add((T) tVUltLimpieza);
        lstView.add((T) tVUltUsoDato);
        lstView.add((T) tVUltLimpiezaDato);
        lstView.add((T) tVUltUso);
        lstView.add((T) tVComoLlegar);
        lstView.add((T) tVCapacidad);

        lstView.add((T) iVFlechas_Informacion_Desripcion);
        lstView.add((T) iVFlechas_Descripcion_Informacion);
        lstView.add((T) iVUltLimpieza);
        lstView.add((T) iVUltUso);
        lstView.add((T) iVIluminacion);
        lstView.add((T) iV_Icon_Capacidad);


        Method.scaleViews(metrics, lstView);
        //escaladoParticular(metrics, 0);

     /*   if (width <= 320) {
            lLInfoWorkpod.getLayoutParams().width = 390;
        }*/
    }

    /**
     * En este método servirá ara adaptar los elementos del diálogo emergente del workpod a todo
     * lo que tiene acceso el usuario no registrado
     */
    private void usuarioNoRegistrado() {
        btnReservarWorkpod.setEnabled(false);
        btnAbrirAhora.setEnabled(false);
    }

    /**
     * Metodo que nos permitirá que al pulsar en la flecha, se oculte la descripción del workpod y muestren los iconos con la información de
     * último uso, última limpieza y si la luz es regulable
     */
    private void onClickIVFlechas_Descripcion_Informacion() {
        lLInfoWorkpod.setVisibility(LinearLayout.VISIBLE);
        iVFlechas_Informacion_Desripcion.setVisibility(View.VISIBLE);
        lLDescripcion.setVisibility(LinearLayout.GONE);
    }

    /**
     * Metodo que nos permitirá que al pulsar en la flecha, se oculten los iconos de
     * último uso, última limpieza y si la luz es regulable y se muestre la descripción del workpod
     */
    private void onClickIVFlechas_Informacion_Descripcion() {
        lLInfoWorkpod.setVisibility(LinearLayout.GONE);
        iVFlechas_Informacion_Desripcion.setVisibility(View.GONE);
        tVDescripcionWorkpod.setText(descripcion);
        lLDescripcion.setVisibility(View.VISIBLE);
    }

    /**
     * Nos lleva al fragment de finalizar sesión, como hay que pasarle los datos del workpod, en función de si hemos accedido al Dialog a través
     * de un item del LsV o a través de el icono de localización en el fragment de sesión del worpod de transacciones o en el marcador del fragment maps
     * donde solo hay un workpod, utilizaremos el objeto de la clase Workpod o el objeto de la clase Ubicacion
     */
    private void onClickBtnAbrirAhora() {

        if (abrirAhora) {
            try {
                direccion = ubicacion.getDireccion().toLongString();
                //HACEMOS EL INSERT DE SESION
                //UPDATE EN RESERVA, EN USO (LO CONSIDERA IGUAL QUE EN RESERVADO) "EN USO" Y CUANDO SE CIERRA "FINALIZADA"
                //CUANDO INICIAS SESION CON TU USUARIO, TE DESCARGA LA RESERVA, PUEDES COMPROBAR SI SU RESERVA ESTÁ EN USO PARA Q EL MAPA LE META EN
                //LA CABINA ABIERTA SIN USAR EL MAPA
                //UPDATE DE RESERVA
                if (reserva == null)
                    reserva = new Reserva();
                reserva.set(workpod.getReserva());
                //HACEMOS UN UPDATE PARA ACTUALIZAR EL ESTADO DE LA RESERVA
                reserva.setEstado("EN USO");
                Database<Reserva> update = new Database<>(Database.UPDATE, reserva);
                update.postRun(() -> {
                    if (update.getError().code > -1) {
                        // CAMBIAR EL WORKPOD EN LA LISTA DE WORKPODS
                        for (Workpod item : ubicacion.getWorkpods())
                            if (item.getId() == workpod.getId())
                                item.setReserva(reserva);
                        // ESTABLECER LA RESERVA DEL USUARIO
                        InfoApp.USER.setReserva(reserva);
                    }

                    sesion = new Sesion();
                    sesion.setEntrada(ZonedDateTime.now());
                    sesion.setUsuario(InfoApp.USER.getId());
                    sesion.setWorkpod(workpod);
                    sesion.setPrecio(0);
                    sesion.setDescuento(0);
                    sesion.setTiempo(0);
                    InfoApp.SESION = sesion;

                    Database<Sesion> insert = new Database<>(Database.INSERT, sesion);
                    insert.postRun(() -> {
                        Log.i("INSERT SESION", "Se ha insertado la sesion correctamente");
                    });
                    insert.start();
                    try {
                        insert.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                update.start();
                //NECESARIO PARA QUE TODO SE EJECUTE EN ORDEN Y SE HAGA EL INSERT Y LUEGO EL SELECT
                update.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dBSession();
            //LLAMAMOS AL FRAGMENT DE SESIÓN FINALIZADA
            Fragment_sesion fragmentSesion = new Fragment_sesion(InfoApp.SESION);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragmentSesion).commit();
            //CERRAMOS EL DIALOGO EMERGENTE
            dismiss();
        }
     /*   } catch (NullPointerException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Abrimos con un Intent google maps y le pasamos la ubicación y el nombre del workpod
     * todos los intents de Google Maps se llaman ACTION_VIEW.
     * Las llamadas setPackage("com.google.android.apps.maps")asegurarán que la aplicación Google Maps para Android maneje el Intent.
     * Si el paquete no está configurado, el sistema determinará qué aplicaciones pueden manejar el Intent
     * <p>
     * Con la Uri en geo van las coordenadas, en z el zoom y q define los lugares que resaltar en el mapa
     */
    public void onClickComoLlegar() {
        //METEMOS EN EL INTENT LAS COORDENADAS DEL WORKPOD
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:" + String.valueOf(ubicacion.getLat()) + "," +
                String.valueOf(ubicacion.getLon()) + "?z=16&q=" + String.valueOf(ubicacion.getLat()) + "," +
                String.valueOf(ubicacion.getLon())));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        //INICIAMOS LA ACTIVIDAD
        startActivity(intent);
    }

    public void onClickReservarWorkpod() {
        if (InfoApp.USER == null) {
            Toast.makeText(requireContext(), "Debes registrarte para poder realizar una reserva", Toast.LENGTH_LONG).show();
        } else if (InfoApp.USER.getReserva() != null && !InfoApp.USER.getReserva().isCancelada()) {
            Toast.makeText(requireContext(), "Ya tienes una reserva", Toast.LENGTH_SHORT).show();
        } else if (workpod.getReserva() == null && !workpod.isMantenimiento()) {
            reserva.setFecha(ZonedDateTime.now());
            //COGEMOS EL ID DEL USUARIO
            reserva.setUsuario(InfoApp.USER.getId());
            //COJO DICHO USUARIO PARA EL FUTURO INSERT EN SESIÓN
            usuarioSesion = InfoApp.USER.getId();
            //COGEMOS EL ID DEL WORKPOD
            reserva.setWorkpod(workpod.getId());
            //ESTABLECEMOS EL ESTADO DE LA RESERVA
            reserva.setEstado("RESERVADA");
            //HACEMOS EL INSERT
            Database<Reserva> insert = new Database<>(Database.INSERT, reserva);
            insert.postRunOnUI(requireActivity(), () -> {
                if (insert.getError().code > -1) {
                    try {
                        if (getActivity() != null) {
                            //CAMBIAMOS TEXTO Y COLOR DEL LAYOUT DEL BTN AL PULSARLO
                            btnReservarWorkpod.setText("Reservado");
                            lLEstadoWorkpod.setBackground(getActivity().getDrawable(R.drawable.rounded_back_button_green));
                            //HACEMOS VISIBLE EL BTN DE ABRIR AHORA
                            lLEstadoWorkpod.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                            //HACEMOS VISIBLE EL BTN DE ABRIR AHORA Y EL BOTON DE CANCELAR RESERVA
                            btnAbrirAhora.setVisibility(View.VISIBLE);
                            lLAbrirAhora.setBackground(getActivity().getDrawable(R.drawable.rounded_border_button));
                            lLAbrirAhora.setVisibility(View.VISIBLE);
                            //FIJAMOS EL ANCHO DE LOS LAYOUTS DE AMBOS BTNS
                            lLEstadoWorkpod.getLayoutParams().width = 0;
                            visibleBtnCancelar = false;
                            //CAMBIAMOS EL TAMAÑO DE LA FUENTE DEL BTN RESERVAR WORKPOD
                            escaladoParticular(metrics, 0);
                            //EMPIEZA EL CRONOMETRO
                            arrancarCronometro();
                        }
                        // CAMBIAR EL WORKPOD EN LA LISTA DE WORKPODS
                        for (Workpod item : ubicacion.getWorkpods())
                            if (item.getId() == workpod.getId())
                                item.setReserva(reserva);
                        // ESTABLECER LA RESERVA DEL USUARIO
                        InfoApp.USER.setReserva(reserva);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(getContext(), insert.getError().message, Toast.LENGTH_SHORT).show();
            });
            insert.start();
        }

    }

    public void onClickCancelarReserva() {
        if (reserva == null)
            reserva = new Reserva();
        reserva.set(workpod.getReserva());
        //HACEMOS UN UPDATE PARA ACTUALIZAR EL ESTADO DE LA RESERVA
        reserva.setEstado("CANCELADA");
        Database<Reserva> update = new Database<>(Database.UPDATE, reserva);
        update.postRunOnUI(requireActivity(), () -> {
            try {
                if (update.getError().code > -1) {
                    //PARAMOS CRONOMETRO Y LO INICIALIZAMOS DE NUEVO A 20 MIN
                    finish = true;
                    inicializarCrono();
                    //CAMBIAMOS TEXTO Y COLOR DEL LAYOUT DEL BTN AL PULSARLO
                    if (getActivity() != null) {
                        lLEstadoWorkpod.setBackground(getActivity().getDrawable(R.drawable.rounded_back_button));
                        lLEstadoWorkpod.getLayoutParams().width = 0;
                        btnReservarWorkpod.setText("Reservar");
                        escaladoParticular(metrics, 0);
                        //HACEMOS INVISIBLE EL BTN DE ABRIR AHORA Y EL BOTON DE CANCELAR RESERVA
                        btnAbrirAhora.setVisibility(View.GONE);
                        lLAbrirAhora.setVisibility(View.GONE);
                        btnCancelarReserva.setVisibility(View.GONE);
                    }
                    // CAMBIAR EL WORKPOD EN LA LISTA DE WORKPODS
                    for (Workpod item : ubicacion.getWorkpods())
                        if (item.getId() == workpod.getId())
                            item.setReserva(reserva);
                    // ESTABLECER LA RESERVA DEL USUARIO
                    InfoApp.USER.setReserva(reserva);

                } else
                    Toast.makeText(getContext(), update.getError().message, Toast.LENGTH_SHORT).show();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        });
        update.start();
    }

    /**
     * Método donde vamos a coger los datos de la BD que han llegado a este fragmento por los constructores y los vamos a depositar en los elementos
     * del XML del Dialog de Reservar Workpod. Se puede apreciar que filtramos, si hemos usado el constructor con el objeto workpod o con el
     * objeto ubicación, su explicación es la siguiente: Este Dialog se abre cuando pulsas en un item del LsV del cluster, si pulsas en un marcador
     * que solo contiene un workpod o cuando das al icono de localización en el fragmento de sesión del histórico de transacciones.
     * En el LsV, la List que se pasa es de Workpods, y no pude hacer una List de ubicaciones ya que solo hay una ubicación en un cluster
     * y una ubicación contiene todos los workpods y había que desglosar uno en cada item x eso el LsV es una List de Workpods q apuntan a una ubicación.
     * Por lo que cuando tengo q acceder al Dialog de Reservar Workpod desde un item del LsV tengo q hacerlo de un objeto workpod, si lo hago con un objeto
     * ubicacion, se machaca y solo salen los datos del primer item independientemente de el item que pulse.
     */
    private void volcarDatos() {
        //CAMPOS QUE NUNCA VARÍAN
        tVNombreWorkpod.setText(workpod.getNombre());
        tVCapacidad.setText(String.valueOf(workpod.getNumUsuarios()));
        tVDireccion.setText(ubicacion.getDireccion().toLongString());
        tVPrecio.setText(String.valueOf(String.format("%.2f", workpod.getPrecio())) + "€/min");
        tVUltUso.setText("Último uso: ");
        //SI WORKPOD NO TIENE FECHA ULT USO
        if (workpod.getUltimoUso() == null) {
            tVUltUsoDato.setText("");
            iVUltUso.setVisibility(View.GONE);
        } else {
            tVUltUsoDato.setText(String.valueOf(workpod.getUltimoUso().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

        }
        //SI ESTE WORKPOD NO FECHA DE ULT LIMPIEZA
        if (workpod.getLimpieza() == null) {
            tVUltLimpiezaDato.setText("");
            iVUltLimpieza.setVisibility(View.GONE);
        } else {
            tVUltLimpieza.setText("Última limpieza: ");
            tVUltLimpiezaDato.setText(String.valueOf(workpod.getLimpieza().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        }

        //SI ESTE WORKPOD POSEE ILUMINACIÓN REGULABLE
        if (workpod.isLuz()) {
            tVIlumincion.setText("Con iluminación regulable");
        } else {
            tVIlumincion.setText("Sin iluminación regulable");
        }

        //SI ESTE WORKPOD NO POSEE UNA DESCRIPCIÓN PARTICULAR, PONEMOS LA DE POR DEFECTO
        if (workpod.getDescripcion().equals("")) {
            descripcion = "Los workpods son cabinas con las que podrás obtener una grata experiencia teletrabajando, olvídate " +
                    "de los problemas que suponía trabajar en casa, workpod es una mini oficina personalizada en la que podrás" +
                    " trabajar sin problemas.";
        } else {
            descripcion = workpod.getDescripcion();
        }
        //SI EL WORKPOD ESTÁ EN MANETENIMIENTO
        if (workpod.isMantenimiento()) {
            btnReservarWorkpod.setText("Mantenimiento");
            //HACEMOS QUE EL BOTÓN OCUPE TODO EL ESPACIO POSIBLE
            lLEstadoWorkpod.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            //CAMBIAMOS EL COLOR DEL LAYOUT DEL BOTÓN DEL ESTADO DE WORKPOD A NARANJA
            lLEstadoWorkpod.setBackground(getActivity().getDrawable(R.drawable.rounded_back_button_orange));
            //CAMBIAMOS EL COLOR DE ABRIR AHORA A BLANCO Y LO OCULTAMOS (SI NO LO CAMBIAMOS A BLANCO, APARECE UN PUNTO AZUL)
            lLAbrirAhora.setBackground(getActivity().getDrawable(R.color.white));
            //OCULTAMOS EL BOTÓN ABRIR AHORA
            btnAbrirAhora.setVisibility(View.GONE);
        }//SI EL WORKPOD ESTÁ RESERVADO
        else if (workpod.getReserva() != null && workpod.getReserva().getEstado().equalsIgnoreCase("Reservada")
                && !workpod.getReserva().isCancelada()) {
            btnReservarWorkpod.setText("Reservado");
            //HACEMOS QUE EL BOTÓN OCUPE TODO EL ESPACIO POSIBLE
            lLEstadoWorkpod.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            //CAMBIAMOS EL COLOR DEL LAYOUT DEL BOTÓN DEL ESTADO DE WORKPOD A ROJO
            lLEstadoWorkpod.setBackground(getActivity().getDrawable(R.drawable.rounded_back_button_red));
            //CAMBIAMOS EL COLOR DE ABRIR AHORA A BLANCO Y LO OCULTAMOS (SI NO LO CAMBIAMOS A BLANCO, APARECE UN PUNTO AZUL)
            lLAbrirAhora.setBackground(getActivity().getDrawable(R.color.white));
            //OCULTAMOS EL BOTÓN ABRIR AHORA
            btnAbrirAhora.setVisibility(View.GONE);
        } else if (workpod.getReserva() != null && workpod.getReserva().getEstado().equalsIgnoreCase("en uso")
                && !workpod.getReserva().isCancelada()) {
            btnReservarWorkpod.setText("Workpod en uso");
            //HACEMOS QUE EL BOTÓN OCUPE TODO EL ESPACIO POSIBLE
            lLEstadoWorkpod.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            //CAMBIAMOS EL COLOR DEL LAYOUT DEL BOTÓN DEL ESTADO DE WORKPOD A ROJO
            lLEstadoWorkpod.setBackground(getActivity().getDrawable(R.drawable.rounded_back_button_red));
            //CAMBIAMOS EL COLOR DE ABRIR AHORA A BLANCO Y LO OCULTAMOS (SI NO LO CAMBIAMOS A BLANCO, APARECE UN PUNTO AZUL)
            lLAbrirAhora.setBackground(getActivity().getDrawable(R.color.white));
            //OCULTAMOS EL BOTÓN ABRIR AHORA
            btnAbrirAhora.setVisibility(View.GONE);
        }

    }

    /**
     * Inicializamos cronómetro
     */
    private void inicializarCrono() {
        this.centesimas = 100;
        this.segundos = 60;
        this.minutos = 20;
    }

    private void arrancarCronometro() throws InterruptedException {
        //INICIALIZAMOS Y ARRANCAMOS HILO
        crono = cronometro();
        crono.start();

    }

    public Thread cronometro() throws InterruptedException {
        //PROGRAMAMOS LOS HILOS
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    escaladoParticular(metrics, 0);

                    Thread.sleep(TIEMPO_EMPIECE_CRONO);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //RESTAR LA FECHA CTUAL CON LA FECHA DE LA RESERVA Y ESO ME DARÁ EL TIEMPO CON METOHD.SUBDATE
                //LA FECHA DE LA RESERVA ESTÁ EN LA BD Y EL NOW SE VA ACTUALIZANDO
                // Method.subsDate(ZonedDateTime.now(),//fecha reserva ) me devuelve los segundos(parsearlo a minutos)
                while (!finish) {
                    try {
                        centesimas -= 1;
                        //LE QUITAMOS UNA UNIDAD AL SEGUNDO
                        if (centesimas <= 0) {
                            segundos--;
                            centesimas = 100;
                            cambiarDistancia = true;

                        }
                        //LE QUITAMOS UNA UNIDAD AL MINUTO
                        if ((segundos <= 0) || (segundos == 60) && (minutos > 0)) {
                            minutos--;
                            segundos = 59;
                        }
                        //DEBE USARSE HANDLER.POST SIEMPRE QUE QUERAMOS REALIZAR UN SUBPROCESO EN LA INTERFAZ DE USUARIO
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!visibleBtnCancelar) {
                                        btnCancelarReserva.setVisibility(View.VISIBLE);
                                        //VOLVEMOS A ESCALAR EL BTN PARA QUE QUEPA TANTO EL BTN DE CANCELAR COMO EL CRONOMETRO
                                        escaladoParticular(metrics, 1);
                                        visibleBtnCancelar = true;
                                    }
                                    if (cambiarDistancia) {
                                        if (posicion.resource != null) {
                                            //CALCULAMOS LA DISTANCIA ENTRE EL USUARIO Y LA CABINA DE WORKPOD
                                            posicionUsuario = new Location("Posición Usuario");
                                            posicionUsuario.setLatitude(posicion.resource.latitude);
                                            posicionUsuario.setLongitude(posicion.resource.longitude);
                                            //CALCULAMOS LA DISTANCIA ENTRE AMBAS POSICIONES
                                            float distance = posicionUsuario.distanceTo(posicionWorkpod);
                                            //HACEMOS ECO DE LA POSICIÓN EN EL BTN
                                          /*  if (distance > 100000) {
                                                btnAbrirAhora.setText("Lejos");
                                            } else if (distance > 1000) {
                                                btnAbrirAhora.setText(String.format("%.2f", (distance / 1000)) + "km");
                                            } else if (distance < 1000 && distance > 150) {
                                                btnAbrirAhora.setText((String.format("%.2f", (distance)) + "m"));
                                            } else if (distance < 150) {*/
                                                btnAbrirAhora.setText("¡Abrir Ahora!");
                                                //CAMBIAMOS VALOR BOOLEANO QUE CONTROLA IR AL FRAGMENT SESION FINALIZADA
                                                abrirAhora = true;
                                           //5 }
                                            cambiarDistancia = false;
                                        } else {
                                            btnAbrirAhora.setText("Lejos");
                                        }
                                    }

                                    if (!finish) {
                                        String cadMinutos = "", cadSegundos = "", cadCentesimas = "";
                                        //Añado 0´s delante para los milisegundos cuando corresponda
                                        if (segundos < 10) {
                                            cadSegundos = "0" + segundos;
                                        } else {
                                            cadSegundos = "" + segundos;
                                        }
                                        if (minutos < 10) {
                                            cadMinutos = "0" + minutos;
                                        } else {
                                            cadMinutos = "" + minutos;
                                        }
                                        //Incorporo las cadenas en el campo de texto
                                        btnReservarWorkpod.setText(cadMinutos + ":" + cadSegundos);
                                        if (segundos < 0) {
                                            Thread.sleep(50);
                                        }
                                    } else {
                                        btnReservarWorkpod.setText("Reservar");
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        //SI SE ACABA EL TIEMPO, SE CIERRA EL FRAGMENT
                        if (minutos < 0) {
                            dismiss();
                        }
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        btnReservarWorkpod.setText("Reservar");
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish = false;
            }
        });
    }


    private void escaladoParticular(DisplayMetrics metrics, int n) {
        try {
            if ((width <= (1500 / metrics.density)) && (width > (750 / metrics.density))) {
                if (btnReservarWorkpod.getText().equals("Reservado")) {
                    btnReservarWorkpod.setTextSize(21);
                } else if (btnReservarWorkpod.getText().equals("Reservar"))
                    btnReservarWorkpod.setTextSize(24);
                if (n == 1) {
                    btnReservarWorkpod.setTextSize(20);
                }
            } else if ((width <= (750 / metrics.density)) && (width > (550 / metrics.density))) {
                if (btnReservarWorkpod.getText().equals("Reservado")) {
                    btnReservarWorkpod.setTextSize(19);
                } else if (btnReservarWorkpod.getText().equals("Reservar"))
                    btnReservarWorkpod.setTextSize(24);
                if (n == 1) {
                    btnReservarWorkpod.setTextSize(18);
                }
            } else if (width <= (550 / metrics.density)) {
                if (btnReservarWorkpod.getText().equals("Reservado")) {
                    btnReservarWorkpod.setTextSize(19);
                } else if (btnReservarWorkpod.getText().equals("Reservar"))
                    btnReservarWorkpod.setTextSize(24);
                if (n == 1) {
                    btnReservarWorkpod.setTextSize(16);
                }
            }
        } catch (NullPointerException e) {
            metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            width = metrics.widthPixels / metrics.density;
        }
    }

    /**
     * Coge la última sesión para hacerte el cobro
     */
    private void dBSession() {
        try {
            Database<Sesion> consultaSesion = new Database<>(Database.SELECTUSER, new Sesion());
            consultaSesion.postRun(() -> {
                for (Sesion session : consultaSesion.getLstSelect()) {
                    /*if (sesion==null || session.getEntrada().compareTo(sesion.getEntrada()) > 0) POR SI DESORDENAN LA BD
                    si alguien coge los datos, los exporta y lo guarda en otra bd ahi si q es susceptible de q se desordene*/
                    sesion = session;
                }
                InfoApp.SESION = sesion;
            });
            consultaSesion.start();
            //ESPERAMOS A QUE LA CONSULTA TERMINE PARA QUE NO SE ABRA EL FRAGMENT DE SESIÓN SIN QUE SE HAYA HECHO LA CONSULTA
            consultaSesion.join();
            InfoApp.SESION = sesion;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que se utilizará cuando el usuario accede a la app desde el histórico. El btn de reservar estará desactivado y solo pondrá
     * su estado
     */
    private void desactivarBtnReservar() {
     /*   try {
            if (Fragment_Transaction_Session.desactivarBtnReservar) {
                btnReservarWorkpod.setEnabled(false);
                if (workpod.getReserva() == null) {
                    btnReservarWorkpod.setText("Disponible");
                } else if ((workpod.getReserva() != null) && workpod.getReserva().getEstado().equalsIgnoreCase("RESERVADA")) {
                    btnReservarWorkpod.setText("Reservado");
                    //HACEMOS QUE EL BOTÓN OCUPE TODO EL ESPACIO POSIBLE
                    lLEstadoWorkpod.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                    //CAMBIAMOS EL COLOR DEL LAYOUT DEL BOTÓN DEL ESTADO DE WORKPOD A ROJO
                    lLEstadoWorkpod.setBackground(getActivity().getDrawable(R.drawable.rounded_back_button_red));
                    //CAMBIAMOS EL COLOR DE ABRIR AHORA A BLANCO Y LO OCULTAMOS (SI NO LO CAMBIAMOS A BLANCO, APARECE UN PUNTO AZUL)
                    lLAbrirAhora.setBackground(getActivity().getDrawable(R.color.white));
                    //OCULTAMOS EL BOTÓN ABRIR AHORA
                    btnAbrirAhora.setVisibility(View.GONE);
                    finish = true;
                    btnCancelarReserva.setVisibility(View.GONE);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
*/
    }
}