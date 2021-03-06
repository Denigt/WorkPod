package com.example.workpod.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.workpod.R;
import com.example.workpod.ValoracionWorkpod;
import com.example.workpod.WorkpodActivity;
import com.example.workpod.basic.Method;
import com.example.workpod.data.Reserva;
import com.example.workpod.data.Sesion;
import com.example.workpod.data.Ubicacion;
import com.example.workpod.data.Workpod;
import com.example.workpod.scale.Scale_Buttons;
import com.example.workpod.scale.Scale_TextView;

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
    private TextView tVWifi;
    private TextView tVSesionCapacidad;
    private TextView tVSesionDireccion;
    private TextView tVTiempoTranscurrido;
    private TextView tVSesionDireccionTitulo;

    //COLECCIONES
    List<Scale_Buttons> lstBtn;
    List<Scale_TextView> lstTv;

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
        tVSesionCapacidad = view.findViewById(R.id.TVSesionCapacidad);
        tVSesionDireccion = view.findViewById(R.id.TVSesionDireccion);
        tVTiempoTranscurrido = view.findViewById(R.id.TVTiempoTranscurrido);
        tVWifi = view.findViewById(R.id.TVWifi);
        tVSesionDireccionTitulo = view.findViewById(R.id.TVSesionDireccionTitulo);
        //PARO CRON??METRO PARA QUE NO SE DUPLIQUE
        this.cerrarWorkpod = false;
        this.precio = 0.0;
        try {
            fechaEntrada = sesion.getEntrada();
            tiempoSesion = Method.subsDate(ZonedDateTime.now(), fechaEntrada);
            //INICIALIZAMOS CRONOMETRO
            this.centesimas = 0;
            horas = Math.round(tiempoSesion / 3600);
            minutos = Math.round((tiempoSesion - (3600 * horas)) / 60);
            segundos = Math.round(tiempoSesion - ((horas * 3600) + (minutos * 60)));

            //ARRANCAMOS CRONOMETRO
            tVSesionCapacidad.setText(String.valueOf(sesion.getWorkpod().getNumUsuarios()));
            tVSesionDireccion.setText(String.valueOf(sesion.getDireccion().toLongString()));
            crono = cronometro();
            crono.start();

            ValoracionWorkpod.boolReservaFinalizada=false;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //CAMBIAMOS DE COLOR A LOS BOTONES (POR LA API 21)
        btnCerrarWorPod.setBackgroundColor(Color.parseColor("#DA4B4B"));
        btnContactarSoporte.setBackgroundColor(Color.parseColor("#C3A240"));

        valoresWorkpod();

        //LISTENERS
        btnCerrarWorPod.setOnClickListener(this);
        btnContactarSoporte.setOnClickListener(this);

        //ESCALAMOS ELEMENTOS
        escalarElementos();

        //CONTROLAMOS QUE UNA VEZ ENTRADA EN LA SESI??N, AL DARLE PARA ATR??S NO VUELVAS AL MAPA
        WorkpodActivity.boolLoc = true;
        WorkpodActivity.boolSession = true;

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

    private void onClickContactarSoporte() {
        fragment_support fragmentSupport = new fragment_support();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragmentSupport).commit();
        //CAMBIAMOS LA SELECCI??N AL ICONO DE SOPORTE
        WorkpodActivity.btnNV.setSelectedItemId(R.id.inv_support);
        WorkpodActivity.boolLoc = false;
    }

    /**
     * Abre el Dialog emergente para cerrar el workpod o que continue la sesei??n si el usuario se ha equivocado al pulsar dicho btn.
     */
    private void onClickCerrarWorkpod() {
        //ABRIMOS DIALOG EMERGENTE PARA QUE EL USUARIO DECIDA SI SALIR
        Fragment_Dialog_Cerrar_Workpod fragmentDialogCerrarWorkpod = new Fragment_Dialog_Cerrar_Workpod(sesion, reserva, ubicacion);
        fragmentDialogCerrarWorkpod.show(getActivity().getSupportFragmentManager(), "Dialog Cerrar Workpod");

    }

    //M??TODOS

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
     * Este m??todo sirve de ante sala para el m??todo de la clase Methods donde escalamos los elementos del xml.
     * En este m??todo inicializamos las colecciones donde guardamos los elementos del xml que vamos a escalar y
     * donde especificamos el width que queremos (match_parent, wrap_content o ""(si no ponemos nada significa que
     * el elemento tiene unos dp definidos que queremos que se conserven tanto en dispositivos grandes como en peque??os.
     * Tambi??n especificamos en la List el estilo de letra (bold, italic, normal) y el tama??o de la fuente del texto tanto
     * para dispositivos peque??os como para dispositivos grandes).
     * <p>
     * Como el m??todo scale de la clase Methods no es un activity o un fragment no podemos inicializar nuestro objeto de la clase
     * DisplayMetrics con los par??metros reales de nuestro m??vil, es por ello que lo inicializamos en este m??todo.
     * <p>
     * En resumen, en este m??todo inicializamos el metrics y las colecciones y se lo pasamos al m??todo de la clase Methods
     */
    private void escalarElementos() {
        //INICIALIZAMOS COLECCIONES
        this.lstTv = new ArrayList<>();
        this.lstBtn = new ArrayList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LLENAMOS COLECCIONES
        lstBtn.add(new Scale_Buttons(btnCerrarWorPod, "", "normal", 20, 20, 20));
        lstBtn.add(new Scale_Buttons(btnContactarSoporte, "", "normal", 20, 20, 20));

        lstTv.add(new Scale_TextView(tVWifi, "wrap_content", "bold", 18, 18, 18));
        lstTv.add(new Scale_TextView(tVSesionCapacidad, "wrap_content", "bold", 18, 18, 18));
        lstTv.add(new Scale_TextView(tVSesionDireccionTitulo, "wrap_content", "bold", 18, 18, 18));
        lstTv.add(new Scale_TextView(tVSesionDireccion, "wrap_content", "bold", 18, 18, 18));
        lstTv.add(new Scale_TextView(tVTiempoTranscurrido, "wrap_content", "bold", 55, 55, 55));

        Method.scaleTv(metrics, lstTv);
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
                                    //A??ado 0??s delante para los milisegundos cuando corresponda
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

    @Override
    public void onDestroy() {
        //EVITA QUE SE DUPLIQUEN LOS HILOS
        cerrarWorkpod = true;
        super.onDestroy();
    }
}