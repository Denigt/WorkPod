package com.example.workpod.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.time.ZonedDateTime;

public class Fragment_Dialog_Cerrar_Workpod extends DialogFragment implements View.OnClickListener {

    //XML
    private Button btnSi;
    private Button btnNo;

    //VARIABLES SESION
    private double precioSesion;
    ZonedDateTime salida;
    private double precioWorkpod;
    private long tiempoSesion;
    private double tiempo;


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
        iVFDCerrarWorkpodSalir = view.findViewById(R.id.IVFDCerrarWorkpodSalir);
        //INICIALIZAMOS VARIABLES
        this.precioSesion = 0.0;

        //EVENTOS DE LOS CONTROLES
        btnNo.setOnClickListener(this);
        btnSi.setOnClickListener(this);
        iVFDCerrarWorkpodSalir.setOnClickListener(this);

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
        //PARAMOS EL HILO
        Fragment_sesion.cerrarWorkpod = true;
        //UPDATE A LA TABLA RESERVA
        if (reserva == null)
            reserva = new Reserva();

        reserva.set(InfoApp.USER.getReserva());
        //HACEMOS UN UPDATE PARA ACTUALIZAR EL ESTADO DE LA RESERVA
        reserva.setEstado("FINALIZADA");
        InfoApp.RESERVA=reserva;
        Database<Reserva> update = new Database<>(Database.UPDATE, reserva);
        update.postRunOnUI(requireActivity(), () -> {
            if (update.getError().code > -1) {
                try {
                    // CAMBIAR EL WORKPOD EN LA LISTA DE WORKPODS
                    for (Workpod item : ubicacion.getWorkpods())
                        if (item.getId() == workpod.getId())
                            item.setReserva(reserva);
                    // ESTABLECER LA RESERVA DEL USUARIO
                    InfoApp.USER.setReserva(reserva);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        });
        update.start();

        //HACEMOS EL INSERT DE SESION
        finiquitarSesion();
        Intent activity = new Intent(getActivity(), ValoracionWorkpod.class);
        activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(activity);
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

    private void precioSesion() {

        Toast.makeText(getActivity(), "Precio: " + String.format("%.2f", precioSesion) + "€", Toast.LENGTH_LONG).show();
    }
}