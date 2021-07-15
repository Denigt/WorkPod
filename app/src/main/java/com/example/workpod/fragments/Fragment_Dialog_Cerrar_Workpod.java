package com.example.workpod.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workpod.R;
import com.example.workpod.ValoracionWorkpod;
import com.example.workpod.basic.Database;
import com.example.workpod.basic.InfoApp;
import com.example.workpod.basic.Method;
import com.example.workpod.data.Reserva;
import com.example.workpod.data.Sesion;
import com.example.workpod.data.Ubicacion;
import com.example.workpod.data.Workpod;
import com.example.workpod.scale.Scale_Buttons;
import com.example.workpod.scale.Scale_Image_View;
import com.example.workpod.scale.Scale_TextView;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Fragment_Dialog_Cerrar_Workpod extends DialogFragment implements View.OnClickListener {

    //XML
    private Button btnSi;
    private Button btnNo;
    private TextView tVFragDiagCerrarWorkpodPregunta;

    //VARIABLES SESION
    private double precioSesion;
    ZonedDateTime salida;
    private double precioWorkpod;
    private long tiempoSesion;
    private double tiempo;

    //VARIABLES PARA ESCALADO
    private List<Scale_Buttons>lstBtn;
    private List<Scale_TextView>lstTv;
    DisplayMetrics metrics;
    float width;


    //VARIABLES CRONÓMETRO
    private int centesimas;
    private double segundos;
    private double minutos;
    private double horas;

    //BD
    Workpod workpod;
    Reserva reserva;
    Ubicacion ubicacion;
    Sesion sesion;

    private boolean controlador;



    private ImageView iVFDCerrarWorkpodSalir;

    public Fragment_Dialog_Cerrar_Workpod(Sesion sesion, Reserva reserva, Ubicacion ubicacion) {
        this.sesion=sesion;
        this.workpod = sesion.getWorkpod();
        this.ubicacion=ubicacion;
        this.reserva=reserva;
        this.centesimas=0;
        this.segundos=0;
        this.minutos=0;
        this.horas=0;
        this.tiempo=0.0;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_cerrar_workpod, container, false);
        return view;
    }

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
        View view = inflater.inflate(R.layout.fragment_dialog_cerrar_workpod, null);
        builder.setView(view);

        //INSTANCIAMOS ELEMENTOS DEL XML
        btnNo = view.findViewById(R.id.BtnNo);
        btnSi = view.findViewById(R.id.BtnSi);
        tVFragDiagCerrarWorkpodPregunta=view.findViewById(R.id.TVFragDiagCerrarWorkpodPregunta);
        iVFDCerrarWorkpodSalir = view.findViewById(R.id.IVFDCerrarWorkpodSalir);
        //INICIALIZAMOS VARIABLES
        this.precioSesion = 0.0;
        this.controlador=false;
        //EVENTOS DE LOS CONTROLES
        btnNo.setOnClickListener(this);
        btnSi.setOnClickListener(this);
        iVFDCerrarWorkpodSalir.setOnClickListener(this);

        //ESCALAMOS ELEMENTOS
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels / metrics.density;
        escalarElementos(metrics);

        //RETORNAMOS EL OBJETO BUILDER CON EL MÉTODO CREATE
        return builder.create();
    }

    //SOBRESECRITURAS
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BtnNo) {
            onClickBtnNo();
        } else if (v.getId() == R.id.IVFDCerrarWorkpodSalir) {
            onClickIVFDCerrarWorkpodSalir();
        } else if (v.getId() == R.id.BtnSi) {
            onClickBtnSi();
        }
    }

    /**
     * Al darle al btn Si, nos vamos a la parte del cuestionario.
     */
    private void onClickBtnSi() {
        try{
            //PARAMOS EL HILO
            Fragment_sesion.cerrarWorkpod = true;
            //UPDATE A LA TABLA RESERVA
            if (reserva == null)
                reserva = new Reserva();

            reserva.set(InfoApp.USER.getReserva());
            //HACEMOS UN UPDATE PARA ACTUALIZAR EL ESTADO DE LA RESERVA
            reserva.setEstado("FINALIZADA");
            InfoApp.RESERVA=reserva;
            // ESTABLECER LA RESERVA DEL USUARIO
            InfoApp.USER.setReserva(reserva);
            Database<Reserva> update = new Database<>(Database.UPDATE, reserva);
            update.postRun( () -> {
                if (update.getError().code > -1) {
                    try {
                        // CAMBIAR EL WORKPOD EN LA LISTA DE WORKPODS
                        for (Workpod item : ubicacion.getWorkpods())
                            if (item.getId() == workpod.getId())
                                item.setReserva(reserva);

                        //CONTROLAMOS QUE NO SE VA A PASAR DE ACTIVITY HASTA QUE SE HAYA ACTUALIZADO LA RESERVA DEL USURIO
                        controlador=true;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                }
            });
            update.start();
            //HACEMOS EL INSERT DE SESION
            finiquitarSesion();
            //CONTROLAMOS QUE NO SE VA A PASAR DE ACTIVITY HASTA QUE SE HAYA ACTUALIZADO LA RESERVA DEL USURIO
            Intent activity = new Intent(getActivity(), ValoracionWorkpod.class);
            activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void finiquitarSesion()  {

        try{
            //INICIALIZAR LA SALIDA
            sesion.setSalida(ZonedDateTime.now());
            //CALCULAMOS PRECIO
            tiempoSesion = Method.subsDate(sesion.getSalida(), sesion.getEntrada());
            //INICIALIZAMOS CRONOMETRO
            this.centesimas = 0;
            horas= Math.floor(tiempoSesion/3600) ;
            minutos = Math.floor(((tiempoSesion-(3600*horas))/60));
            segundos = Math.floor(tiempoSesion%60);
            //CALCULAMOS PRECIO DE LA SESION
            if(horas==0){
                this.precioSesion=(minutos*sesion.getWorkpod().getPrecio())+ ((segundos*sesion.getWorkpod().getPrecio())/60);
            }else{
                this.precioSesion=(horas*60*sesion.getWorkpod().getPrecio()+ minutos*sesion.getWorkpod().getPrecio())+ ((segundos*sesion.getWorkpod().getPrecio())/60);
            }
            //SE LO PASAMOS AL OBJETO SESION
            sesion.setPrecio(precioSesion);
            this.tiempo=horas + (minutos/60);
            sesion.setTiempo(tiempo);

            //ACTUALIZAMOS SESION
            Database<Sesion> update = new Database<>(Database.UPDATE, sesion);
            update.start();
            update.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }


    //MÉTODOS

    /**
     * Cerramos el fragment dialog al pulsar el btn No.
     */
    private void onClickBtnNo() {
        dismiss();
    }

    /**
     * Cerramos el fragment dialog al pulsar la x.
     */
    private void onClickIVFDCerrarWorkpodSalir() {
        dismiss();
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
    private void escalarElementos(DisplayMetrics metrics) {
        //INICIALIZAMOS COLECCIONES
        this.lstBtn = new ArrayList<>();
        this.lstTv = new ArrayList<>();

        //LLENAMOS COLECCIONES
        lstBtn.add(new Scale_Buttons(btnSi, "wrap_content", "normal", 20, 20, 20));
        lstBtn.add(new Scale_Buttons(btnNo, "wrap_content", "normal", 20, 20, 20));

        lstTv.add(new Scale_TextView(tVFragDiagCerrarWorkpodPregunta, "wrap_content", "bold", 15, 17, 18));

        Method.scaleBtns(metrics, lstBtn);
        Method.scaleTv(metrics, lstTv);
    }
}