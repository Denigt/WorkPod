package com.example.workpod;

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

import com.example.workpod.data.Workpod;
import com.example.workpod.fragments.Fragment_sesion;

public class Fragment_Dialog_Cerrar_Workpod extends DialogFragment implements View.OnClickListener {

    //XML
    private Button btnSi;
    private Button btnNo;

    //VARIABLES SESION
    private double precio;

    //VARIABLES CRONÓMETRO
    private long segundos;
    private long minutos;

    //BD
    Workpod workpod;

    private ImageView iVFDCerrarWorkpodSalir;

    public Fragment_Dialog_Cerrar_Workpod(Workpod workpod, long minutos, long segundos) {
        this.workpod=workpod;
        this.minutos=minutos;
        this.segundos=segundos;
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
        this.precio=0.0;

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
        Fragment_sesion.cerrarWorkpod=true;
        //HACEMOS EL INSERT DE SESION
        insertSesion();
        Intent activity = new Intent(getActivity(), ValoracionWorkpod.class);
        activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(activity);
    }

    private void insertSesion() {
        precioSesion();
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
        precio=minutos*(workpod.getPrecio())+(segundos*workpod.getPrecio())/60;
        Toast.makeText(getActivity(),"Precio: "+String.format("%.2f",precio)+"€",Toast.LENGTH_LONG).show();
    }
}