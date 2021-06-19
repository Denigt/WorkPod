package com.example.workpod.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workpod.R;
import com.example.workpod.WorkpodActivity;
import com.example.workpod.basic.Database;
import com.example.workpod.basic.InfoApp;
import com.example.workpod.basic.Method;
import com.example.workpod.data.Reserva;
import com.example.workpod.data.Ubicacion;
import com.example.workpod.data.Workpod;
import com.example.workpod.scale.Scale_Buttons;
import com.example.workpod.scale.Scale_TextView;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Use the {@link Fragment_Dialog_Workpod#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Dialog_Workpod extends DialogFragment implements View.OnClickListener {

    //INSTANCIAS DE LA CLASE DATA
    Ubicacion ubicacion;
    Workpod workpod;
    Reserva reserva;

    //XML
    private TextView tVNombreWorkpod;
    private TextView tVDireccion;
    private TextView tVCapacidad;
    private TextView tVPrecio;
    private TextView tVUltUso;
    private TextView tVUltLimpieza;
    private TextView tVIlumincion;
    private TextView tVDescripcionWorkpod;

    private ImageView iVComoLlegar;
    private ImageView iVFlechas_Informacion_Desripcion;
    private ImageView iVFlechas_Descripcion_Informacion;
    private ImageView iVUltUso;
    private ImageView iVUltLimpieza;

    private Button btnReservarWorkpod;
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

    //COLECCIONES
    List<Scale_Buttons> lstBtn;
    List<Scale_TextView>lstTv;


    //CONSTRUCTOR CON INSTANCIA DE UBICACIÓN
    public Fragment_Dialog_Workpod() {
        ubicacion = new Ubicacion();
    }

    //CONSTRUCTOR CON INSTANCIA DE WORKPODS Y DIRECCION
    /**
     * Crea un fragment con la informacion del workpod que hay en la ubicacion
     * @param workpod Workpod del que obtener la informacion
     * @param ubicacion Ubicacion en la que se encuentra el workpod
     */
    public Fragment_Dialog_Workpod(Workpod workpod, Ubicacion ubicacion) {
        this.workpod = workpod;
        this.ubicacion = ubicacion;
    }

    //CONSTRUCTOR CON INSTANCIA DE UBICACION
    /**
     * Crea un fragment con la informacion del workpod que hay en la ubicacion
     * Solo usar si la ubicacion tiene un solo workpod
     * @param ubicacion Ubicacion en la que se encuentra el workpod
     */
    public Fragment_Dialog_Workpod(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
        this.workpod = ubicacion.getWorkpods().get(0);
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
        tVUltLimpieza = (TextView) view.findViewById(R.id.TVUltLimpieza);
        tVIlumincion = (TextView) view.findViewById(R.id.TVIluminacion);

        iVComoLlegar = (ImageView) view.findViewById(R.id.iVComoLlegar);
        tVDescripcionWorkpod = (TextView) view.findViewById(R.id.TVDescripcionWorkpod);
        iVFlechas_Informacion_Desripcion = (ImageView) view.findViewById(R.id.IVFlechas_Informacion_Descripcion);
        iVFlechas_Descripcion_Informacion = (ImageView) view.findViewById(R.id.IVFlechas_Descripcion_Informacion);
        iVUltUso = (ImageView) view.findViewById(R.id.IVUltUso);
        iVUltLimpieza = (ImageView) view.findViewById(R.id.IVUltLimpieza);

        btnAbrirAhora = (Button) view.findViewById(R.id.BtnAbrirAhora);
        btnReservarWorkpod = (Button) view.findViewById(R.id.BtnReservarWorkpod);

        //INICIALIZAMOS LA INSTANCIA DE RESERVA
        reserva = new Reserva();

        //ESCALAMOS ELEMENTOS
        escalarElementos();

        //VOLCAMOS DATOS DE LA BD
        volcarDatos();

        //LISTENERS
        btnReservarWorkpod.setOnClickListener(this);
        btnAbrirAhora.setOnClickListener(this);
        iVFlechas_Informacion_Desripcion.setOnClickListener(this);
        iVFlechas_Descripcion_Informacion.setOnClickListener(this);
        iVComoLlegar.setOnClickListener(this);

        //COMPROBAMOS SI USUARIO ESTÁ REGISTRADO

        /* DESACTIVAR BTN CUANDO USUARIO NO ESTÉ REGISTRADO
         if(InfoApp.USER.getId()==0){
                usuarioNoRegistrado();
            }*/


        //RETORNAMOS EL OBJETO BUILDER CON EL MÉTODO CREATE
        return builder.create();
    }

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
        //INICIALIZAMOS COLECCIONES
        this.lstBtn=new ArrayList<>();
        this.lstTv=new ArrayList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LLENAMOS COLECCIONES
        lstBtn.add(new Scale_Buttons(btnReservarWorkpod,"wrap_content","normal",20,25));
        lstBtn.add(new Scale_Buttons(btnAbrirAhora,"","normal",15,20));

        lstTv.add(new Scale_TextView(tVNombreWorkpod,"wrap_content","bold",40,55));
        lstTv.add(new Scale_TextView(tVPrecio,"wrap_content","bold",13,25));
        lstTv.add(new Scale_TextView(tVDireccion,"wrap_content","normal",20,20));
        lstTv.add(new Scale_TextView(tVDescripcionWorkpod,"wrap_content","normal",15,15));
        lstTv.add(new Scale_TextView(tVIlumincion,"wrap_content","normal",17,17));
        lstTv.add(new Scale_TextView(tVUltLimpieza,"wrap_content","normal",15,17));
        lstTv.add(new Scale_TextView(tVUltUso,"wrap_content","normal",17,17));

        Method.scaleButtons(metrics, lstBtn);
        Method.scaleTv(metrics, lstTv);
    }


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
            onClickReservarWorkpod((Button) v);
        }else if(v.getId()==R.id.iVComoLlegar){
            onClickComoLlegar();
        }
    }

    //MÉTODOS

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
        lLDescripcion.setVisibility(LinearLayout.GONE);
    }

    /**
     * Metodo que nos permitirá que al pulsar en la flecha, se oculten los iconos de
     * último uso, última limpieza y si la luz es regulable y se muestre la descripción del workpod
     */
    private void onClickIVFlechas_Informacion_Descripcion() {
        lLInfoWorkpod.setVisibility(LinearLayout.GONE);
        tVDescripcionWorkpod.setText(descripcion);
        lLDescripcion.setVisibility(View.VISIBLE);
    }

    /**
     * Nos lleva al fragment de finalizar sesión, como hay que pasarle los datos del workpod, en función de si hemos accedido al Dialog a través
     * de un item del LsV o a través de el icono de localización en el fragment de sesión del worpod de transacciones o en el marcador del fragment maps
     * donde solo hay un workpod, utilizaremos el objeto de la clase Workpod o el objeto de la clase Ubicacion
     */
    private void onClickBtnAbrirAhora() {
        //SI WORKPOD==NULL ESTAMOS ACCEDIENDO MEDIANTE UN ITEM DEL LSV
        if (workpod == null) {
            //LLAMAMOS AL FRAGMENT DE SESIÓN FINALIZADA
            Fragment_sesion_finalizada fragmentSesionFinalizada = new Fragment_sesion_finalizada(ubicacion);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragmentSesionFinalizada).commit();
            //CONTROLAMOS QUE AL SALIR DE LA SESIÓN FINALIZADA, VOLVAMOS AL FRAGMENT INICIAL
            WorkpodActivity.boolLoc = false;
            WorkpodActivity.boolfolder = false;
            //CERRAMOS EL DIALOGO EMERGENTE
            dismiss();
        }//SI WORPOD!=NULL, ACCEDEMOS DE UN MARCADOR DONDE HAY UN SOLO WORKPOD O DESDE EL FRAGMENT DE SESION DEL HISTÓRICO DE TRANSACCIONES
        else {
            //LLAMAMOS AL FRAGMENT DE SESIÓN FINALIZADA
            Fragment_sesion_finalizada fragmentSesionFinalizada = new Fragment_sesion_finalizada(workpod, direccion);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragmentSesionFinalizada).commit();
            //CONTROLAMOS QUE AL SALIR DE LA SESIÓN FINALIZADA, VOLVAMOS AL FRAGMENT INICIAL
            WorkpodActivity.boolLoc = false;
            WorkpodActivity.boolfolder = false;
            //CERRAMOS EL DIALOGO EMERGENTE
            dismiss();
        }
    }

    /**
     * Abrimos con un Intent google maps y le pasamos la ubicación y el nombre del workpod
     * todos los intents de Google Maps se llaman ACTION_VIEW.
     * Las llamadas setPackage("com.google.android.apps.maps")asegurarán que la aplicación Google Maps para Android maneje el Intent.
     * Si el paquete no está configurado, el sistema determinará qué aplicaciones pueden manejar el Intent
     *
     * Con la Uri en geo van las coordenadas, en z el zoom y q define los lugares que resaltar en el mapa
     */
    public void onClickComoLlegar(){
        //METEMOS EN EL INTENT LAS COORDENADAS DEL WORKPOD
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:"+String.valueOf(ubicacion.getLat())+","+
                String.valueOf(ubicacion.getLon())+"?z=16&q="+String.valueOf(ubicacion.getLat())+","+
                String.valueOf(ubicacion.getLon())));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        //INICIAMOS LA ACTIVIDAD
        startActivity(intent);
    }

    public void onClickReservarWorkpod(Button btn){
        if (InfoApp.USER == null){
            Toast.makeText(requireContext(), "Debes registrarte para poder realizar una reserva", Toast.LENGTH_LONG).show();
        }else if (InfoApp.USER.getReserva() != null && !InfoApp.USER.getReserva().isCancelada()) {
            Toast.makeText(requireContext(), "Ya tienes una reserva", Toast.LENGTH_SHORT).show();
        }else if (workpod.getReserva() == null && !workpod.isMantenimiento()) {
            Reserva reserva = new Reserva();
            reserva.setFecha(ZonedDateTime.now());
            //COGEMOS EL ID DEL USUARIO
            reserva.setUsuario(InfoApp.USER.getId());
            //COGEMOS EL ID DEL WORKPOD
            reserva.setWorkpod(workpod.getId());
            //ESTABLECEMOS EL ESTADO DE LA RESERVA
            reserva.setEstado("RESERVADA");
            //HACEMOS EL INSERT
            Database<Reserva> insert = new Database<>(Database.INSERT, reserva);
            insert.postRunOnUI(requireActivity(), () -> {
                if (insert.getError().code > -1) {
                    //CAMBIAMOS TEXTO Y COLOR DEL LAYOUT DEL BTN AL PULSARLO
                    btn.setText("Reservado");
                    lLEstadoWorkpod.setBackground(getActivity().getDrawable(R.drawable.rounded_back_button_green));
                    //HACEMOS VISIBLE EL BTN DE ABRIR AHORA
                    lLEstadoWorkpod.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    btnAbrirAhora.setVisibility(View.VISIBLE);
                    //ECO DEL TIEMPO HASTA CADUCAR RESERVA
                    Toast.makeText(getActivity(),"Tienes 20 min para llegar",Toast.LENGTH_LONG).show();
                    // CAMBIAR EL WORKPOD EN LA LISTA DE WORKPODS
                    for (Workpod item: ubicacion.getWorkpods())
                        if(item.getId() == workpod.getId())
                            item.setReserva(reserva);
                } else
                    Toast.makeText(getContext(), insert.getError().message, Toast.LENGTH_SHORT).show();
            });
            insert.start();
        }
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
        //SI WORKPOD NO TIENE FECHA ULT USO
        if (workpod.getUltimoUso() == null) {
            tVUltUso.setText("");
            iVUltUso.setVisibility(View.GONE);
        } else {
            tVUltUso.setText("Último uso " + String.valueOf(workpod.getUltimoUso().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

        }
        //SI ESTE WORKPOD NO FECHA DE ULT LIMPIEZA
        if (workpod.getLimpieza() == null) {
            tVUltLimpieza.setText("");
            iVUltLimpieza.setVisibility(View.GONE);
        }else{
            tVUltLimpieza.setText("Última limpieza " + String.valueOf(workpod.getLimpieza().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
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
        else if (workpod.getReserva() != null) {
            btnReservarWorkpod.setText("Reservado");
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

    private void  distanciaTiempo(){

    }
}