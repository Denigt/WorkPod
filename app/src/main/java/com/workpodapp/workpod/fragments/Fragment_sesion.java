package com.workpodapp.workpod.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.workpodapp.workpod.PagoActivity;
import com.workpodapp.workpod.R;
import com.workpodapp.workpod.ValoracionWorkpod;
import com.workpodapp.workpod.WorkpodActivity;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.data.Reserva;
import com.workpodapp.workpod.data.Sesion;
import com.workpodapp.workpod.data.Ubicacion;
import com.workpodapp.workpod.data.Workpod;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_sesion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_sesion extends Fragment implements View.OnClickListener {

    //XML
    private Button btnCerrarWorPod;
    private Button btnContactarSoporte;

    private ImageView iVWifi;
    private ImageView iVPassword;
    private ImageView iVAssistent;

    private TextView tVWifi;
    private TextView tVPassword;
    private TextView tVEstamosAqui;
    private TextView tVTiempoTranscurridoTitulo;
    private TextView tVReportarProblema;
    private TextView tVLinkSoporte;
    private TextView tVSesionCapacidad;
    private TextView tVSesionDireccion;
    private TextView tVTiempoTranscurrido;
    private TextView tVSesionDireccionTitulo;

    //BD
    Ubicacion ubicacion;
    Workpod workpod;
    Sesion sesion;
    Reserva reserva;
    String direccion;
    //CREE SESION, INSERT FECHA ENTRADA, WORKPOD, USUARIO,

    //VARIABLES CRONOMETRO
    private int centesimas;
    private int segundos;
    private int minutos;
    private int horas;
    private Thread crono;
    private Handler handler = new Handler();

    public static boolean cerrarWorkpod;


    //VARIABLES SESION
    ZonedDateTime fechaEntrada;
    private double precio;
    private long tiempoSesion;

    public Fragment_sesion() {
        // Required empty public constructor
    }

    //CONSTRUCTOR CON WORKPOD
    public Fragment_sesion(Sesion sesion) {
        try {
            this.sesion = sesion;
            this.workpod = sesion.getWorkpod();
            this.direccion = sesion.getDireccion().toLongString();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public static Fragment_sesion newInstance(String param1, String param2) {
        Fragment_sesion fragment = new Fragment_sesion();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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

        View view = inflater.inflate(R.layout.fragment_sesion, container, false);

        //INICIALIZAMOS ELEMENTOS DEL XML
        btnCerrarWorPod = view.findViewById(R.id.BtnCerrarWorPod);
        btnContactarSoporte = view.findViewById(R.id.BtnContactarSoporte);

        iVAssistent=view.findViewById(R.id.IVAssistent);
        iVPassword=view.findViewById(R.id.IVPassword);
        iVWifi=view.findViewById(R.id.IVWifi);

        tVPassword=view.findViewById(R.id.TVPassword);
        tVEstamosAqui=view.findViewById(R.id.TVEstamosAqui);
        tVLinkSoporte=view.findViewById(R.id.TVLinkSoporte);
        tVReportarProblema=view.findViewById(R.id.TVReportarProblema);
        tVLinkSoporte=view.findViewById(R.id.TVLinkSoporte);
        tVTiempoTranscurridoTitulo=view.findViewById(R.id.TVTiempoTranscurridoTitulo);
        tVSesionCapacidad = view.findViewById(R.id.TVSesionCapacidad);
        tVSesionDireccion = view.findViewById(R.id.TVSesionDireccion);
        tVTiempoTranscurrido = view.findViewById(R.id.TVTiempoTranscurrido);
        tVWifi = view.findViewById(R.id.TVWifi);
        tVSesionDireccionTitulo = view.findViewById(R.id.TVSesionDireccionTitulo);

        //ARRANCAMOS EL HILO QUE HARÁ DE CONTADOR
        arrancarCrono();

        //CAMBIAMOS DE COLOR A LOS BOTONES (POR LA API 21)
        btnCerrarWorPod.setBackgroundColor(Color.parseColor("#DA4B4B"));
        btnContactarSoporte.setBackgroundColor(Color.parseColor("#C3A240"));

        valoresWorkpod();

        //LISTENERS
        btnCerrarWorPod.setOnClickListener(this);
        btnContactarSoporte.setOnClickListener(this);

        //ESCALAMOS ELEMENTOS
        escalarElementos();

        //CONTROLAMOS QUE UNA VEZ ENTRADA EN LA SESIÓN, AL DARLE PARA ATRÁS NO VUELVAS AL MAPA
        WorkpodActivity.boolSession = true;
        InfoFragment.actual = InfoFragment.SESSION;

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BtnCerrarWorPod) {
            onClickCerrarWorkpod();
        } else if (v.getId() == R.id.BtnContactarSoporte) {
            onClickContactarSoporte();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            //SI LA APP ENTRA EN ONPAUSE, SI SE PARASE EL CRONO, LO DETECTARÍAMOS PORQUE EL BOOLEANO ESTARÍA A TRUE Y LO VOLVERÍAMOS A ARRANCAR
            if (this.cerrarWorkpod) {
                arrancarCrono();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void onClickContactarSoporte() {
        Fragment_Support fragmentSupport = new Fragment_Support();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragmentSupport).commit();
        //CAMBIAMOS LA SELECCIÓN AL ICONO DE SOPORTE
        WorkpodActivity.btnNV.setSelectedItemId(R.id.inv_support);
    }

    /**
     * Abre el Dialog emergente para cerrar el workpod o que continue la seseión si el usuario se ha equivocado al pulsar dicho btn.
     */
    private void onClickCerrarWorkpod() {
        //ABRIMOS DIALOG EMERGENTE PARA QUE EL USUARIO DECIDA SI SALIR
        /*Fragment_Dialog_Cerrar_Workpod fragmentDialogCerrarWorkpod = new Fragment_Dialog_Cerrar_Workpod(sesion, reserva, ubicacion);
        fragmentDialogCerrarWorkpod.show(getActivity().getSupportFragmentManager(), "Dialog Cerrar Workpod");*/

        // ABRIMOS EL ACTIVITY PARA REALIZAR EL PAGO
        Intent activity = new Intent(requireActivity(), PagoActivity.class);
        startActivity(activity);

    }

    //MÉTODOS

    /**
     * Metodo donde asignaremos el valor del workpod a los elementos del xml
     */
    private void valoresWorkpod() {
        try {
            if (workpod == null) {
                tVSesionDireccion.setText(ubicacion.getDireccion().toLongString());
                tVWifi.setText(ubicacion.getWorkpods().get(0).getNombre());
                tVTiempoTranscurrido.setText("00:00");
                //ESTOS IF SON ESTETICOS, ES PARA QUE PONGA PERSONA O PERSONAS
                if (ubicacion.getWorkpods().get(0).getNumUsuarios() == 1) {
                    tVSesionCapacidad.setText("Capacidad: " + String.valueOf(ubicacion.getWorkpods().get(0).getNumUsuarios()) + " persona");
                } else {
                    tVSesionCapacidad.setText("Capacidad: " + String.valueOf(ubicacion.getWorkpods().get(0).getNumUsuarios()) + " personas");
                }

            } else {
                tVSesionDireccion.setText(direccion);
                tVWifi.setText(workpod.getNombre());
                tVTiempoTranscurrido.setText("00:00");
                //ESTOS IF SON ESTETICOS, ES PARA QUE PONGA PERSONA O PERSONAS
                if (workpod.getNumUsuarios() == 1) {
                    tVSesionCapacidad.setText("Capacidad: " + String.valueOf(workpod.getNumUsuarios()) + " persona");
                } else {
                    tVSesionCapacidad.setText("Capacidad: " + String.valueOf(workpod.getNumUsuarios()) + " personas");
                }
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
     */
    private <T extends View> void escalarElementos() {
        //INICIALIZAMOS COLECCIONES
        List<T> lstView = new ArrayList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LLENAMOS COLECCIONES
        lstView.add((T) btnCerrarWorPod);
        lstView.add((T) btnContactarSoporte);

        lstView.add((T) iVWifi);
        lstView.add((T) iVPassword);
        lstView.add((T) iVAssistent);

        lstView.add((T) tVEstamosAqui);
        lstView.add((T) tVPassword);
        lstView.add((T) tVReportarProblema);
        lstView.add((T) tVLinkSoporte);
        lstView.add((T) tVTiempoTranscurridoTitulo);
        lstView.add((T) tVWifi);
        lstView.add((T) tVSesionCapacidad);
        lstView.add((T) tVSesionDireccionTitulo);
        lstView.add((T) tVSesionDireccion);
        lstView.add((T) tVTiempoTranscurrido);

        Method.scaleViews(metrics, lstView);
    }

    public Thread cronometro() throws InterruptedException {
        //PROGRAMAMOS LOS HILOS
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (!cerrarWorkpod) {
                    try {
                        centesimas++;
                        //LE QUITAMOS UNA UNIDAD AL SEGUNDO
                        if (centesimas >= 99) {
                            segundos++;
                            centesimas = 00;
                        }
                        //LE QUITAMOS UNA UNIDAD AL MINUTO
                        if (segundos >= 60) {
                            minutos++;
                            segundos = 0;
                        }

                        if (minutos >= 60) {
                            horas++;
                            minutos = 0;
                            segundos = 0;
                        }
                        //DEBE USARSE HANDLER.POST SIEMPRE QUE QUERAMOS REALIZAR UN SUBPROCESO EN LA INTERFAZ DE USUARIO
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String cadHoras = "", cadMinutos = "", cadSegundos = "";
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
                                    if (horas < 10) {
                                        cadHoras = "0" + horas;
                                    } else {
                                        cadHoras = "" + horas;
                                    }
                                    //Incorporo las cadenas en el campo de texto
                                    if (horas != 0) {
                                        tVTiempoTranscurrido.setText(cadHoras + ":" + cadMinutos + ":" + cadSegundos);
                                    } else {
                                        tVTiempoTranscurrido.setText(cadMinutos + ":" + cadSegundos);
                                    }

                                    if (segundos < 0) {
                                        Thread.sleep(50);
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }


                        });

                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //reinicioReserva();
            }
        });
    }

    /**
     * Inicializamos la fecha de entrada, calculamos el tiempo que hay entre la fecha de entrada y la actual y lo mostramos por pantalla. También iniciailizamos
     * el hilo y lo arrancamos
     */
    private void arrancarCrono() {
        try {
            this.precio = 0.0;
            fechaEntrada = sesion.getEntrada();
            tiempoSesion = Method.subsDate(ZonedDateTime.now(), fechaEntrada);
            //INICIALIZAMOS CRONOMETRO
            this.centesimas = 0;
            horas = Math.round(tiempoSesion / 3600);
            minutos = Math.round((tiempoSesion - (3600 * horas)) / 60);
            segundos = Math.round(tiempoSesion - ((horas * 3600) + (minutos * 60)));

            //ARRANCAMOS CRONOMETRO
            tVSesionCapacidad.setText("Capacidad: " + String.valueOf(sesion.getWorkpod().getNumUsuarios()));
            tVSesionDireccion.setText(String.valueOf(sesion.getDireccion().toLongString()));
            cerrarWorkpod = false;
            crono = cronometro();
            crono.start();

            ValoracionWorkpod.boolReservaFinalizada = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        //EVITA QUE SE DUPLIQUEN LOS HILOS
        cerrarWorkpod = true;
        super.onDestroy();
    }
}